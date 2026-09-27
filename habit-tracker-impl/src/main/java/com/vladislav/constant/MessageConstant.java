package com.vladislav.constant;

public final class MessageConstant {

    private MessageConstant() {
    }

    public static final String HABIT_NOT_FOUND_EXCEPTION = "Habit with id: %d is not found";
    public static final String RECORD_ALREADY_EXISTS_EXCEPTION = "Habit with id: %d is already marked today";
    public static final String USERNAME_ALREADY_EXISTS_EXCEPTION = "User with username: %s is already exists";
    public static final String INVALID_CREDENTIAL = "Invalid username or password";
}
