package org.flower.productapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/product-request/v1")
public class ProductRequestController {

    private final Logger log = LoggerFactory.getLogger(ProductRequestController.class);

    ProductRequestService service;
    public ProductRequestController(ProductRequestService aService) {
        this.service = aService;
    }

    @PostMapping
    public ResponseEntity<Void> submitProductRequest(@RequestBody ProductRequest productRequest) {
        log.debug("Received product request: {}", productRequest.getName());
        Long requestId = this.service.saveProductRequest(productRequest);

        return ResponseEntity.created(URI.create(String.format("/product/v1/%d", requestId))).build();
    }

    @PostMapping("/approve/{requestId}")
    public ResponseEntity<Void> approveProductRequest(Long requestId) {
        return ResponseEntity.accepted().build();
    }
    @GetMapping("/{requestId}")
    public ResponseEntity<ProductRequest> getProductRequest(@PathVariable("requestId") Long requestId) {
        return this.service.getProductRequestById(requestId)
                .map(productRequest -> {
                    log.debug("Found request {}", productRequest.getRequestId());
                    return ResponseEntity.ok(productRequest);
                }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProductRequest>> getMyRequests() {
        List<ProductRequest> requests = this.service.getProductRequests();
        return ResponseEntity.ok(requests);
    }
}
