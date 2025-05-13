package com.fitnesstracker.dailylogs.utils.exceptions;

public class ExistingLogDateException extends RuntimeException {
    public ExistingLogDateException() {}

    public ExistingLogDateException(String message) { super(message); }

    public ExistingLogDateException(Throwable cause) { super(cause); }

    public ExistingLogDateException(String message, Throwable cause) { super(message, cause); }
}
