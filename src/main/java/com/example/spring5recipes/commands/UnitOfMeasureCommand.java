package com.example.spring5recipes.commands;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class UnitOfMeasureCommand {
    private Long id;
    private String description;
}
