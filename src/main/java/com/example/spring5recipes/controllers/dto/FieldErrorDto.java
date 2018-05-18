package com.example.spring5recipes.controllers.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FieldErrorDto {

    private String name;
    private String errorDescription;

    public FieldErrorDto(String name, String errorDescription) {
        this.name = name;
        this.errorDescription = errorDescription;
    }
}
