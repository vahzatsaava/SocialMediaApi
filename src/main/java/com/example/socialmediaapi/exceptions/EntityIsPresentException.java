package com.example.socialmediaapi.exceptions;

import jakarta.persistence.PersistenceException;

public class EntityIsPresentException extends PersistenceException {
    private String message;
   public EntityIsPresentException(String message){
       this.message = message;
   }
}
