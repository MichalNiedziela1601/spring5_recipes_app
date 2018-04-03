package com.example.spring5recipes.converters;

import com.example.spring5recipes.commands.CategoryCommand;
import com.example.spring5recipes.domain.Category;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryCommandToCategoryTest {

    public static final Long LONG_ID = 1L;
    public static final String DESCRIPTION = "description";

    private CategoryCommandToCategory converter;
    @Before
    public void setUp() throws Exception {
        converter = new CategoryCommandToCategory();
    }

    @Test
    public void whenNullSourceThenReturnNull() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void whenNotNullSourceThenReturnCategory() throws Exception {
        assertNotNull(converter.convert(new CategoryCommand()));
    }

    @Test
    public void testConverter() {
        CategoryCommand command = new CategoryCommand();
        command.setId(LONG_ID);
        command.setDescription(DESCRIPTION);

        Category result = converter.convert(command);

        assertEquals(LONG_ID, result.getId());
        assertEquals(DESCRIPTION, result.getDescription());
    }

}