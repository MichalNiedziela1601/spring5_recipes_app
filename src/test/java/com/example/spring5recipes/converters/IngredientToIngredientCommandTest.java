package com.example.spring5recipes.converters;

import com.example.spring5recipes.commands.IngredientCommand;
import com.example.spring5recipes.domain.Ingredient;
import com.example.spring5recipes.domain.UnitOfMeasure;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class IngredientToIngredientCommandTest {

    public final static Long LONG_ID = 1L;
    public final static String DESCRIPTION = "description";
    public final static BigDecimal AMOUNT = new BigDecimal(2);
    public final static Long UOM_ID = 1L;

    private IngredientToIngredientCommand converter;


    @Before
    public void setUp() throws Exception {
        converter = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    }

    @Test
    public void testNullSource() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testNotNullSOurce() throws Exception {
        assertNotNull(converter.convert(new Ingredient()));
    }

    @Test
    public void testWithNullUOM() throws Exception {

        Ingredient ingredient = new Ingredient();
        ingredient.setId(LONG_ID);
        ingredient.setAmount(AMOUNT);
        ingredient.setDescription(DESCRIPTION);
        ingredient.setUom(null);

        IngredientCommand command = converter.convert(ingredient);

        assertEquals(LONG_ID, command.getId());
        assertEquals(AMOUNT, command.getAmount());
        assertEquals(DESCRIPTION, command.getDescription());
        assertNull(command.getUnitOfMeasure());
    }

    @Test
    public void testWithNotNullUom() throws Exception {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(LONG_ID);
        ingredient.setAmount(AMOUNT);
        ingredient.setDescription(DESCRIPTION);
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId(UOM_ID);
        ingredient.setUom(uom);

        IngredientCommand command = converter.convert(ingredient);

        assertNotNull(command.getUnitOfMeasure());
        assertEquals(LONG_ID, command.getId());
        assertEquals(AMOUNT, command.getAmount());
        assertEquals(DESCRIPTION, command.getDescription());
        assertEquals(UOM_ID, command.getUnitOfMeasure().getId());

    }
}