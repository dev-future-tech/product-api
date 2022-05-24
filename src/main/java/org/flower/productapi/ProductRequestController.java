package org.flower.productapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/product-request/v1")
public class ProductRequestController {

    private final Logger log = LoggerFactory.getLogger(ProductRequestController.class);

    ProductRequestService service;
    public ProductRequestController(ProductRequestService _service) {
        this.service = _service;
    }

    @PostMapping
    public ResponseEntity<Void> submitProductRequest(@RequestBody ProductRequest productRequest) {
        Long requestId = this.service.saveProductRequest(productRequest);

        return ResponseEntity.created(URI.create(String.format("/product/v1/%d", requestId))).build();
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ProductRequest> getProductRequest(@PathVariable("requestId") Long requestId) {
        ProductRequest productRequest = this.service.getProductRequestById(requestId);

        if(productRequest != null) {
            log.debug("Found request {}", productRequest.getRequestId());
            return ResponseEntity.ok(productRequest);
        } else {
            log.debug("Did not find product request for {}", requestId);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductRequest>> getMyRequests() {
        List<ProductRequest> requests = this.service.getProductRequests();
        return ResponseEntity.ok(requests);
    }
}
