package com.example.demo.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Object id) {
        super("id: " + id + " not found");
    }
}
