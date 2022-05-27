package org.flower.productapi;

import java.util.List;
import java.util.ArrayList;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.Mockito;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest(ProductController.class)
@ContextConfiguration(classes = RabbitMQTestConfiguration.class)
@Testcontainers
public class ProductControllerTest {

    @Container
    public static GenericContainer<?> rabbit = new GenericContainer<>("rabbitmq:3-management")
            .withExposedPorts(5672, 15672);

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.port", () -> rabbit.getMappedPort(5672));
        registry.add("spring.rabbitmq.host", () -> rabbit.getContainerIpAddress());
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        List<Product> results = new ArrayList<>();
        Product product = new Product();
        product.setProductId(100L);
        product.setName("New Product");
        product.setDescription("A brand new product");
        product.setSku("123412sf");

        results.add(product);

        when(productService.getProducts(Mockito.anyInt(), Mockito.anyInt())).thenReturn(results);
        this.mockMvc.perform(get("/product/v1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"));
	}
}