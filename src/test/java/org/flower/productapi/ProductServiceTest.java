package org.flower.productapi;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductAPIApplication.class)
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Test
    @Transactional
    public void testGetProductById() {
        Product product = new Product();
        product.setName("New Product");
        product.setDescription("A brand new product");
        product.setSku("123412sf");

        Long productId = productService.saveProduct(product);

        assertThat(productId).isNotNull();
    }
}
