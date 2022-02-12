package org.flower.productapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/product/v1")
public class ProductController {

    private Logger log = LoggerFactory.getLogger(ProductController.class);

    @Resource
    ProductRepository productRepository;

    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<Void> createProduct(@RequestBody Product product) {
        log.info("Saving product {} with description {}", product.getName(), product.getDescription());
        ProductDAO saved = productRepository.saveAndFlush(toDAO(product));

        return ResponseEntity.created(URI.create(String.format("/product/v1/%d", saved.getProductId()))).build();
    }

    @GetMapping("/{product_id}")
    public ResponseEntity<Product> getProduct(@PathVariable("product_id") long productId) {
        return this.productRepository.findById(productId).map(dao -> {
            Product found = new Product();
            found.setProductId(dao.getProductId());
            found.setName(dao.getName());
            found.setSku(dao.getSku());
            found.setDescription(dao.getDescription());
            return ResponseEntity.ok(found);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(@RequestParam(defaultValue = "10", name = "count") int count,
                                                     @RequestParam(defaultValue = "0", name = "page") int page) {

        long totalProducts = this.productRepository.count();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Total-Products", String.valueOf(totalProducts));
        Page<ProductDAO> results = this.productRepository.findAll(Pageable.ofSize(count).withPage(page));
        List<Product> toReturn = results.map(this::toDTO).toList();
        return ResponseEntity.ok().headers(headers).body(toReturn);
    }

    private ProductDAO toDAO(Product product) {
        ProductDAO dao = new ProductDAO();
        dao.setName(product.getName());
        dao.setDescription(product.getDescription());
        dao.setSku(product.getSku());
        return dao;
    }
    private Product toDTO(ProductDAO dao) {
        Product product = new Product();
        product.setProductId(dao.getProductId());
        product.setDescription(dao.getDescription());
        product.setSku(dao.getSku());
        product.setName(dao.getName());
        return product;
    }
}
