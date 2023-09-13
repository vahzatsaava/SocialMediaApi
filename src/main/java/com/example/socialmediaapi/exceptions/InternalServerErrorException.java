package com.example.socialmediaapi.exceptions;

public class InternalServerErrorException extends RuntimeException{
    public InternalServerErrorException(String message) {
        super(message);
    }
    public static InternalServerErrorException failedToSaveImage() {
        return new InternalServerErrorException("An exception occurred when uploading photo");
    }


}
