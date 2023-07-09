package org.flower.productapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class ProductAPIApplicationTests {

    @Autowired
    ProductController productController;


    @Test
    void contextLoads() {
        assertThat(productController).isNotNull();
    }

    @Container
    public static GenericContainer<?> rabbit = new GenericContainer<>("rabbitmq:3-management")
            .withExposedPorts(5672, 15672);

    @Container
    public static GenericContainer<?> zookeeper = new GenericContainer<>("bitnami/zookeeper:3.8.1-debian-11-r52")
            .withExposedPorts(2181,8080)
            .withEnv("ALLOW_ANONYMOUS_LOGIN", "yes")
            .withEnv("ZOO_PORT_NUMBER", "2181")
            .withEnv("ENVIRONMENT", "local")
            .withEnv("ZOO_TICK_TIME", "5000")
            .withReuse(false);

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.port", () -> rabbit.getMappedPort(5672));
        registry.add("spring.rabbitmq.host", () -> rabbit.getHost());
        registry.add("spring.cloud.zookeeper.connect-string", () -> String.format("%s:%d", zookeeper.getHost(), zookeeper.getMappedPort(2181)));
    }

}
