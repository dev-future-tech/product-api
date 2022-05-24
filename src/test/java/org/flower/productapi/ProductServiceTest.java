package org.flower.productapi;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = RabbitMQTestConfiguration.class)
@SpringBootTest
@Testcontainers
public class ProductServiceTest {
    @Container
    public static GenericContainer<?> rabbit = new GenericContainer<>("rabbitmq:3-management")
            .withExposedPorts(5672, 15672);

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.port", () -> rabbit.getMappedPort(5672));
        registry.add("spring.rabbitmq.host", () -> rabbit.getContainerIpAddress());
    }

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
