package com.example.socialmediaapi.exceptions;




public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }

    public static BadRequestException fileIsNotValid(String message) {
        return new BadRequestException(message);
    }

    public static BadRequestException fileIsEmpty(String fileName) {
        String message = String.format("File: %s is empty", fileName);
        return new BadRequestException(message);
    }

    public static BadRequestException fileIsNotImage(String fileName) {
        String message = String.format("File: %s is not an image.", fileName);
        return new BadRequestException(message);
    }

    public static BadRequestException fileIsTooLarge(String fileName) {
        String message = String.format("File: %s cannot be bigger then 20 MB", fileName);
        return new BadRequestException(message);
    }
}
