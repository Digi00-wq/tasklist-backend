package com.example.demo.exception;

public class LabelNotFoundException extends RuntimeException {
    public LabelNotFoundException(Long id) {
        super("Label not found with id: " + id);
    }
}
