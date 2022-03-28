package org.flower.productapi;

public class ProductNotFoundException extends Exception {

    @Override
    public ProductNotFoundException(String message) {
        super(message);
    }
}