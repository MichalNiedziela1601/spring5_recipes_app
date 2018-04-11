package com.example.spring5recipes.services;

import com.example.spring5recipes.commands.UnitOfMeasureCommand;
import com.example.spring5recipes.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.example.spring5recipes.domain.UnitOfMeasure;
import com.example.spring5recipes.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class UnitOfMeasureServiceImplTest {

    private UnitOfMeasureService unitOfMeasureService;
    private UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        unitOfMeasureService = new UnitOfMeasureServiceImpl(unitOfMeasureRepository, unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    public void getUnitOfMeasures() throws Exception {

        Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();
        UnitOfMeasure unit1 = new UnitOfMeasure();
        unit1.setId(1L);
        unitOfMeasures.add(unit1);

        UnitOfMeasure unit2 = new UnitOfMeasure();
        unit2.setId(2L);
        unitOfMeasures.add(unit2);

        when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasures);

        Set<UnitOfMeasureCommand> listOfUoms = unitOfMeasureService.getUnitOfMeasures();
        assertEquals(2, listOfUoms.size());
    }

}