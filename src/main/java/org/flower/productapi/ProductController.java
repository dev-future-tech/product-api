package org.flower.productapi;

import java.net.URI;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/product/v1")
public class ProductController {

    private Logger log = LoggerFactory.getLogger(ProductController.class);

    @Resource
    ProductService productService;

    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<Void> createProduct(@RequestBody Product product) {
        log.info("Saving product {} with description {}", product.getName(), product.getDescription());
        Long savedId = productService.saveProduct(product);

        return ResponseEntity.created(URI.create(String.format("/product/v1/%d", savedId))).build();
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<Product> getProduct(@PathVariable("product_id") long productId) throws ProductNotFoundException{
        return this.productService.getProductById(productId).map(dao -> {
            return ResponseEntity.ok(dao);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(@RequestParam(defaultValue = "10", name = "count") int count,
                                                     @RequestParam(defaultValue = "0", name = "page") int page) {

        long totalProducts = this.productService.getTotalProductCount();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Total-Products", String.valueOf(totalProducts));
        List<Product> toReturn = this.productService.getProducts(count, page);
        return ResponseEntity.ok().headers(headers).body(toReturn);
    }

}
