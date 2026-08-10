package com.vladislav.exception;

public class HabitNotFoundException extends RuntimeException {
    public HabitNotFoundException(String message) {
        super(message);
    }
}
