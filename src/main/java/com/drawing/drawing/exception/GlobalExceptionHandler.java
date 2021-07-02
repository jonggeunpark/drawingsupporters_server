package com.drawing.drawing.exception;

import com.drawing.drawing.constants.Message;
import com.drawing.drawing.constants.ResponseMessage;
import com.drawing.drawing.constants.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<Message> NotFoundException(NotFoundException e) {
        Message message = new Message(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND);
        return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailDuplicateException.class)
    public ResponseEntity<Message> MenteeDuplicateException(EmailDuplicateException e) {
        Message message = new Message(StatusCode.BAD_REQUEST, ResponseMessage.DUPLICATE_EMAIL);
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NicknameDuplicateException.class)
    public ResponseEntity<Message> NicknameDuplicateException(NicknameDuplicateException e) {
        Message message = new Message(StatusCode.BAD_REQUEST, ResponseMessage.DUPLICATE_NICKNAME);
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Message> invalidPasswordException(InvalidPasswordException e) {
        Message message = new Message(StatusCode.BAD_REQUEST, ResponseMessage.INVALID_PASSWORD);
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

}
