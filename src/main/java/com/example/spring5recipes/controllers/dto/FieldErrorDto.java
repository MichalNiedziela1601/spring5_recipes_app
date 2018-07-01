package com.example.spring5recipes.controllers.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FieldErrorDto {

    private String name;
    private String errorDescription;

    public FieldErrorDto(String name, String errorDescription) {
        this.name = name;
        this.errorDescription = errorDescription;
    }
}
