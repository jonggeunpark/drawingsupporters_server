package com.drawing.drawing.exception;

public class EmailDuplicateException extends RuntimeException {
    public EmailDuplicateException(String message){
        super("Email Duplicate Exception " + message);
        System.out.println(getMessage());
    }
}
