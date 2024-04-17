package org.fhv.amongus.amongus.exceptions;

public class PlayerAlreadyExistException extends RuntimeException{
    public PlayerAlreadyExistException(String message) {
        super(message);
    }
}
