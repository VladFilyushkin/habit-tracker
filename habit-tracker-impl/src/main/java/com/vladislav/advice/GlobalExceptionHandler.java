package com.vladislav.advice;

import com.vladislav.exception.HabitNotFoundException;
import com.vladislav.exception.RecordAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HabitNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleHabitNotFoundException(HabitNotFoundException habitNotFoundException) {
        return habitNotFoundException.getMessage();
    }

    @ExceptionHandler(RecordAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleRecordAlreadyExistsException(RecordAlreadyExistsException recordAlreadyExistsException) {
        return recordAlreadyExistsException.getMessage();
    }
}
