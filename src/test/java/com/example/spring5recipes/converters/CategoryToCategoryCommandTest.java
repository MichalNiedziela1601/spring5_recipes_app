package com.example.spring5recipes.converters;

import com.example.spring5recipes.commands.CategoryCommand;
import com.example.spring5recipes.domain.Category;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryToCategoryCommandTest {

    public static final Long LONG_ID = 1L;
    public static final String DESCRIPTION = "description";

    private CategoryToCategoryCommand converter;

    @Before
    public void setUp() throws Exception {
        converter = new CategoryToCategoryCommand();
    }

    @Test
    public void whenNullSourceThenReturnNull() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void whenNotNullSourceThenReturnCategory() throws Exception {
        assertNotNull(converter.convert(new Category()));
    }

    @Test
    public void testConverter() {
        Category command = new Category();
        command.setId(LONG_ID);
        command.setDescription(DESCRIPTION);

        CategoryCommand result = converter.convert(command);

        assertEquals(LONG_ID, result.getId());
        assertEquals(DESCRIPTION, result.getDescription());
    }

}