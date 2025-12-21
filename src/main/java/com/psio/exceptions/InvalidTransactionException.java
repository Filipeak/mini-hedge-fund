package com.psio.exceptions;

public class InvalidTransactionException extends RuntimeException {
    public InvalidTransactionException(String message) {
        super(message);
    }

    //currently does nothing only usage is commented out in Wallet
}
