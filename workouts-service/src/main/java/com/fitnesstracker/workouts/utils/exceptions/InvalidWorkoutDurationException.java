package com.fitnesstracker.workouts.utils.exceptions;

public class InvalidWorkoutDurationException extends RuntimeException {
    public InvalidWorkoutDurationException() {}

    public InvalidWorkoutDurationException(String message) {
        super(message);
    }

    public InvalidWorkoutDurationException(Throwable cause) { super(cause); }

    public InvalidWorkoutDurationException(String message, Throwable cause) { super(message, cause); }
}
