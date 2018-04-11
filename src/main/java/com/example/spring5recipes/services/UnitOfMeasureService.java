package com.example.spring5recipes.services;

import com.example.spring5recipes.commands.UnitOfMeasureCommand;

import java.util.Set;

public interface UnitOfMeasureService {

    Set<UnitOfMeasureCommand> getUnitOfMeasures();
}
