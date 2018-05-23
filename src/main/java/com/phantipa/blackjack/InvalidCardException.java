package com.phantipa.blackjack;

public class InvalidCardException extends Exception {

    CardErrorCode code;

    public InvalidCardException(String message,CardErrorCode code) {
        super(message);
        this.code = code;
    }

}
