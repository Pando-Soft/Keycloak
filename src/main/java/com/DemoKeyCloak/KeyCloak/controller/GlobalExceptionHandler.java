package com.DemoKeyCloak.KeyCloak.controller;

import com.DemoKeyCloak.KeyCloak.model.common.ErrorResponse;
import com.DemoKeyCloak.KeyCloak.model.common.exception.KeyclaokRuntimeException;
import com.DemoKeyCloak.KeyCloak.model.common.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;
import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(KeyclaokRuntimeException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(KeyclaokRuntimeException ex, Locale locale) {
        ErrorResponse errorResponse = new ErrorResponse(ZonedDateTime.now(), messageSource.getMessage(ex.getMessage(), null, locale), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
