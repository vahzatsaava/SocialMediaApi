package com.example.socialmediaapi.exceptions;

import lombok.Data;

@Data
public class UnauthorizedException extends RuntimeException {
    private String message;

    public UnauthorizedException(String message) {
        this.message = message;
    }
}
