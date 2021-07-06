package com.drawing.drawing.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message){
        super("Not Authorized Exception " + message);
        System.out.println(getMessage());
    }
}
