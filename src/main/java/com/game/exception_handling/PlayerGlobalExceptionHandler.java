package com.game.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PlayerGlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<PlayerIncorrectData> handleException(NoSuchPlayerException exception){
        PlayerIncorrectData data = new PlayerIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<PlayerIncorrectData> handleException(Exception exception){
        PlayerIncorrectData data = new PlayerIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
