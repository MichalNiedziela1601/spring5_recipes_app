package com.example.spring5recipes.controllers.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ValidationErrorDto {

    private List<FieldErrorDto> errors;

    public ValidationErrorDto(List<FieldErrorDto> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "ValidationErrorDto{" +
                "errors=" + errors +
                '}';
    }
}
