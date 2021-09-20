package com.game.exception_handling;

public class NoSuchPlayerException extends RuntimeException{
    public NoSuchPlayerException(String message) {
        super(message);
    }
}
