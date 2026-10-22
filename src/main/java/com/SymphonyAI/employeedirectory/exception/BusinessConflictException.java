package com.SymphonyAI.employeedirectory.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessConflictException extends RuntimeException{

    private String errorDetails;

    public BusinessConflictException(String message) {
        super(message);
    }

    public BusinessConflictException(String message, String errorDetails) {
        super(message);

        this.errorDetails = errorDetails;
    }
}
