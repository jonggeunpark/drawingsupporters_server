package com.drawing.drawing.exception;

public class NicknameDuplicateException extends RuntimeException {
    public NicknameDuplicateException(String message){
        super("Nickname Duplicate Exception " + message);
        System.out.println(getMessage());
    }
}
