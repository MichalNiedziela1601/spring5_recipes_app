package com.example.spring5recipes.controllers;

import com.example.spring5recipes.commands.UnitOfMeasureCommand;
import com.example.spring5recipes.services.UnitOfMeasureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController()
public class UomsController {

    private UnitOfMeasureService unitOfMeasureService;

    public UomsController(UnitOfMeasureService unitOfMeasureService) {
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping(value = "uoms")
    public ResponseEntity<Set<UnitOfMeasureCommand>> getUoms() {
        Set<UnitOfMeasureCommand> uoms = this.unitOfMeasureService.getUnitOfMeasures();
        return new ResponseEntity<Set<UnitOfMeasureCommand>>(uoms, HttpStatus.OK);
    }
}
