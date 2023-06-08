package org.flower.productapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
public class ProductRequestService {

    private Logger log = LoggerFactory.getLogger(ProductRequestService.class);

    @Resource
    ProductRequestRepository requestRepository;

    @Resource
    ObjectMapper objectMapper;

    @Resource
    RabbitTemplate rabbitTemplate;

    Optional<ProductRequest> getProductRequestById(Long productRequestId) {
        return this.requestRepository.findById(productRequestId).map(this::convertDAO);
    }

    List<ProductRequest> getProductRequests() {
        return requestRepository.findAll()
                .stream()
                .map(this::convertDAO)
                .toList();
    }

    Long saveProductRequest(ProductRequest productRequest) {

        ProductRequestDAO newRequest = new ProductRequestDAO();
        newRequest.setProductName(productRequest.getName());
        newRequest.setProductBrand(productRequest.getBrand());
        newRequest.setProductSize(productRequest.getSize());
        newRequest.setApproved(false);
        newRequest.setRequestedBy("user1");

        log.debug("Saving product request...");
        ProductRequestDAO saved = this.requestRepository.save(newRequest);

        try {
            log.debug("Product request saved, pushing id {} to the queue...", saved.getRequestId());

            ProductApprovalMessage request = new ProductApprovalMessage();
            request.setProductId(saved.getRequestId());

            String response = objectMapper.writeValueAsString(request);

            Message message= MessageBuilder.withBody(response.getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .build();
            this.rabbitTemplate.convertAndSend("product-exchange", "product.requests.items", message);
        } catch(Exception e) {
            log.error("Error sending message to queue", e);
        }
        return saved.getRequestId();
    }

    private ProductRequest convertDAO(ProductRequestDAO dao) {
        ProductRequest request = new ProductRequest();
        request.setRequestId(dao.getRequestId());
        request.setName(dao.getProductName());
        request.setBrand(dao.getProductBrand());
        request.setSize(dao.getProductSize());
        return request;
    }

}
