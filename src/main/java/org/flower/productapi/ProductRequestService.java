package org.flower.productapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ProductRequestService {

    private Logger log = LoggerFactory.getLogger(ProductRequestService.class);

    @Resource
    ProductRequestRepository requestRepository;


    @Resource
    RabbitTemplate rabbitTemplate;

    ProductRequest getProductRequestById(Long productRequestId) {
        return null;
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

        ProductRequestDAO saved = this.requestRepository.save(newRequest);

        try {
            log.debug("Product request saved, pushing id {} to the queue...", saved.getRequestId());

            String requestStr = String.format("Request Id: %d", saved.getRequestId());
            this.rabbitTemplate.convertAndSend("product-exchange", "product.requests.items", requestStr);
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
