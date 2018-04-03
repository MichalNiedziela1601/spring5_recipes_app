package com.example.spring5recipes.converters;

import com.example.spring5recipes.commands.IngredientCommand;
import com.example.spring5recipes.domain.Ingredient;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IngredientCommandToIngredientTest {

    public static final Long LONG_ID = 1L;
    public static final String DESCRIPTION = "description";

    private IngredientCommandToIngredient converter;

    @Before
    public void setUp() throws Exception {
        converter = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @Test
    public void whenNullSourceThenReturnNull() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void whenNotNullSourceThenReturnCategory() throws Exception {
        assertNotNull(converter.convert(new IngredientCommand()));
    }

    @Test
    public void testConverter() {
        IngredientCommand command = new IngredientCommand();
        command.setId(LONG_ID);
        command.setDescription(DESCRIPTION);

        Ingredient result = converter.convert(command);

        assertEquals(LONG_ID, result.getId());
        assertEquals(DESCRIPTION, result.getDescription());
    }

}