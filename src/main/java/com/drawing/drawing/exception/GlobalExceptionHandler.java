package com.drawing.drawing.exception;

import com.drawing.drawing.constants.Message;
import com.drawing.drawing.constants.ResponseMessage;
import com.drawing.drawing.constants.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<Message> NotFoundException(NotFoundException e) {
        Message message = new Message(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND);
        return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    private ResponseEntity<Message> UnauthorizedException(UnauthorizedException e) {
        Message message = new Message(StatusCode.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);
        return new ResponseEntity<>(message,HttpStatus.UNAUTHORIZED);
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

    // 500
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception ex) {
        log.info(ex.getClass().getName());
        log.error("error", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
