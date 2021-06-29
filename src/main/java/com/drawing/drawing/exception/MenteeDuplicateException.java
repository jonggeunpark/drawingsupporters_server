package com.drawing.drawing.exception;

public class MenteeDuplicateException extends RuntimeException {
    public MenteeDuplicateException(String message){
        super("Mentee Duplicate Exception " + message);
        System.out.println(getMessage());
    }
}
