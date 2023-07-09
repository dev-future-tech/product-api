package org.flower.productapi;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ProductRequestController.class)
@ContextConfiguration(classes = RabbitMQTestConfiguration.class)
@MockBean(ProductRequestService.class)
public class ProductRequestControllerTest {

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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRequestService service;

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.port", () -> rabbit.getMappedPort(5672));
        registry.add("spring.rabbitmq.host", () -> rabbit.getHost());
        registry.add("spring.cloud.zookeeper.connect-string", () -> String.format("%s:%d", zookeeper.getHost(), zookeeper.getMappedPort(2181)));
    }

    @Test
    public void shouldCreateProductRequest() throws Exception {

        ProductRequest result = new ProductRequest();
        result.setRequestId(1209L);
        result.setName("New product");
        result.setBrand("Modern brands");
        result.setSize("100Ml");

        when(service.getProductRequestById(Mockito.anyLong())).thenReturn(Optional.of(result));

        this.mockMvc.perform(get("/product-request/v1/1209"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void testGetMyRequests() throws Exception {
        ProductRequest req1 = new ProductRequest();
        req1.setRequestId(5657L);
        req1.setName("Long Socks");
        req1.setBrand("Rio 1987");
        req1.setSize("L");

        ProductRequest req2 = new ProductRequest();
        req2.setRequestId(3453L);
        req2.setName("Jolt Coffee Pods");
        req2.setBrand("NesCoffee");
        req2.setSize("500g");

        ProductRequest req3 = new ProductRequest();
        req3.setRequestId(9876L);
        req3.setName("Pipilong Stockings");
        req3.setBrand("Fictional haberdashery");
        req3.setSize("Petite");

        List<ProductRequest> results = new ArrayList<>(Arrays.asList(req1, req2, req3));

        when(service.getProductRequests()).thenReturn(results);

        this.mockMvc.perform(get("/product-request/v1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(3)));
    }

}
