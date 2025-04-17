package com.fitnesstracker.users.utils.exceptions;

public class InvalidCaloricIntakeException extends RuntimeException {

    public InvalidCaloricIntakeException() {}

    public InvalidCaloricIntakeException(String message) {
        super(message);
    }

    public InvalidCaloricIntakeException(Throwable cause) {
        super(cause);
    }

    public InvalidCaloricIntakeException(String message, Throwable cause) {
        super(message, cause);
    }
}