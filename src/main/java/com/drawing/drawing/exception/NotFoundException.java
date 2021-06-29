package com.drawing.drawing.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message){
        super("Not Found Exception " + message);
        System.out.println(getMessage());
    }
}
