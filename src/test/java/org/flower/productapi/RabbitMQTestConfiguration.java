package org.flower.productapi;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@TestConfiguration
@Testcontainers
@ContextConfiguration(classes = RabbitMQTestConfiguration.class)
public class RabbitMQTestConfiguration {
    @Bean
    Queue queue() {
        return new Queue("product-requests", false);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange("test-product-exchange");
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("product.requests.#");
    }
}
