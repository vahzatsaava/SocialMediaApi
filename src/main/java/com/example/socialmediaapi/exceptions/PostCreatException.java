package com.example.socialmediaapi.exceptions;

import lombok.Data;

@Data
public class PostCreatException extends RuntimeException {
    private final String message;

    public PostCreatException(String message) {
        this.message = message;
    }


}
