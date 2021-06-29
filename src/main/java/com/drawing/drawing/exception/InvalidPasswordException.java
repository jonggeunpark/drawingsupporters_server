package com.drawing.drawing.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message){
        super("Invalid Password Exception " + message);
        System.out.println(getMessage());
    }
}
