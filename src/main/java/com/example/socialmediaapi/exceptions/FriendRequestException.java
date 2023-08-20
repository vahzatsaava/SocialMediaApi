package com.example.socialmediaapi.exceptions;

import jakarta.persistence.PersistenceException;
import lombok.Data;

@Data
public class FriendRequestException extends PersistenceException {
    private String message;

    public FriendRequestException(String message) {
        this.message = message;
    }
}
