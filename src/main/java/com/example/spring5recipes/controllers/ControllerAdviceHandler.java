package com.example.spring5recipes.controllers;

import com.example.spring5recipes.controllers.dto.FieldErrorDto;
import com.example.spring5recipes.controllers.dto.ValidationErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class ControllerAdviceHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDto> handleBadRequest(MethodArgumentNotValidException ex) {
        log.error("Handling MethodArgumentNotValidException");
        BindingResult bindingResult = ex.getBindingResult();

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<FieldErrorDto> fieldErrorDtos = new ArrayList<>(fieldErrors.size());
        for(FieldError error  : fieldErrors) {
            fieldErrorDtos.add(new FieldErrorDto(error.getField(), error.getDefaultMessage()));
        }

        return new ResponseEntity<ValidationErrorDto>(new ValidationErrorDto(fieldErrorDtos), HttpStatus.BAD_REQUEST);

    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> handleNumberFormatException(Exception ex) {
        log.error("Handling NumberFormatException");
        String exception = ex.getMessage();
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }
}
