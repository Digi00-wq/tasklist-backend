package com.example.demo.exception;

import java.time.Instant;

public record ErrorResponse(Instant now, int status, String message, String error) {

}
