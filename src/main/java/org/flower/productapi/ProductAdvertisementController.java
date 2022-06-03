package org.flower.productapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/product/v1/advertisements")
public class ProductAdvertisementController {

    @Value("${pod.name}")
    String productPodName;
    private final Logger log = LoggerFactory.getLogger(ProductAdvertisementController.class);

    @GetMapping
    public ResponseEntity<String> getAds() {
        log.debug("Getting ads from the other container...");
        RestTemplate template = new RestTemplate();
        String ads = template.getForObject(String.format("http://%s:7080/product-util/v1", productPodName), String.class);

        return ResponseEntity.ok(ads);
    }
}
