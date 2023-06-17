package org.flower.productapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"!test"})
public class ProductRequestValidatedConsumer {

    private Logger log = LoggerFactory.getLogger(ProductRequestValidatedConsumer.class);

    @RabbitListener(queues="product-approvals")
    public void receiveValidatedProducts(ApprovedProductMessage productValidation) {
        log.debug("Received product request validation {}, approved? {}", productValidation.getProductId(),
                productValidation.isApproved());
    }
}
