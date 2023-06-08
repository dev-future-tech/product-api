package org.flower.productapi;

import java.util.List;
import java.util.Optional;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ProductService {

    @Resource
    ProductRepository productRepository;

    Long saveProduct(Product product) {
        ProductDAO saved = productRepository.saveAndFlush(toDAO(product));

        return saved.getProductId();
    }

    Optional<Product> getProductById(Long productId) throws ProductNotFoundException {
        return this.productRepository.findById(productId).map(dao -> {
            Product found = toDTO(dao);
            return Optional.of(found);
        }).orElseThrow(() -> new ProductNotFoundException("Error getting product by id"));
    }

    long getTotalProductCount() {
        return this.productRepository.count();
    }

    public List<Product> getProducts(int count, int page) {
        Page<ProductDAO> results = this.productRepository.findAll(Pageable.ofSize(count).withPage(page));
        return results.map(this::toDTO).toList();
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