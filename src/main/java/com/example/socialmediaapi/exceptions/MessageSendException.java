package com.example.socialmediaapi.exceptions;

import jakarta.persistence.PersistenceException;
import lombok.Data;

@Data
public class MessageSendException extends PersistenceException {
    private final String message;

    public MessageSendException(String message) {
        this.message = message;
    }
}
