package com.example.socialmediaapi.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class AppError {
    private final Integer status;
    private final String message;
    private final Date timeStamp;

    public AppError(Integer status, String message) {
        this.message = message;
        this.status = status;
        this.timeStamp = new Date();
    }

}
