package com.fitnesstracker.meals.utils.exceptions;

public class InvalidCaloriesException extends RuntimeException {
    public InvalidCaloriesException() {}

    public InvalidCaloriesException(String message) {
        super(message);
    }

    public InvalidCaloriesException(Throwable cause) {super(cause);}

    public InvalidCaloriesException(String message, Throwable cause) {super(message, cause);}
}
