package org.flower.productapi;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ProductRequestService {

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

        this.rabbitTemplate.convertAndSend(saved.getRequestId());

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
