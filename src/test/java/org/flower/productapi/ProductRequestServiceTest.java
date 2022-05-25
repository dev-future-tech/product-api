package org.flower.productapi;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
@Testcontainers
@SpringBootTest
@ContextConfiguration(classes = RabbitMQTestConfiguration.class)
public class ProductRequestServiceTest {

    private final Logger log = LoggerFactory.getLogger(ProductRequestServiceTest.class);

    @Container
    public static GenericContainer<?> rabbit = new GenericContainer<>("rabbitmq:3-management")
            .withExposedPorts(5672, 15672);

    List<ProductRequestDAO> saved;

    @Autowired
    ProductRequestRepository repository;

    @Autowired
    ProductRequestService service;

    @BeforeEach
    public void setUp() {
        if(saved == null) {
            log.debug("Setting up stored ProductRequests...");
            ProductRequestDAO dao1 = new ProductRequestDAO();
            dao1.setProductName("Long Socks");
            dao1.setProductBrand("Rio 1987");
            dao1.setProductSize("L");
            dao1.setRequestedBy("Bob Peters");

            ProductRequestDAO saved1 = repository.save(dao1);

            ProductRequestDAO dao2 = new ProductRequestDAO();
            dao2.setProductName("Jolt Coffee Pods");
            dao2.setProductBrand("NesCoffee");
            dao2.setProductSize("500g");
            dao2.setRequestedBy("Bob Peters");

            ProductRequestDAO saved2 = repository.save(dao2);

            ProductRequestDAO dao3 = new ProductRequestDAO();
            dao3.setProductName("Pipilong Stockings");
            dao3.setProductBrand("Fictional haberdashery");
            dao3.setProductSize("Petite");
            dao3.setRequestedBy("Mary Johnson");

            ProductRequestDAO saved3 = repository.save(dao3);

            log.debug("Storing local instances in `saved`");
            this.saved = new ArrayList<>(Arrays.asList(saved1, saved2, saved3));
        }
    }

    @Test
    public void testGetProductRequests() {
        List<ProductRequest> requests = service.getProductRequests();
        Assertions.assertThat(requests).isNotNull().hasSize(3);
    }
}
