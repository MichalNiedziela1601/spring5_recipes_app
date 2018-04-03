package com.example.spring5recipes.converters;

import com.example.spring5recipes.commands.NotesCommand;
import com.example.spring5recipes.domain.Notes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NotesCommandToNotesTest {

    public static final Long ID = 1L;
    public static final String RECIPE_NOTES = "notes";

    private NotesCommandToNotes converter;

    @Before
    public void setUp() throws Exception {
        converter = new NotesCommandToNotes();
    }

    @Test
    public void testNullSource() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testNotNullSource() throws Exception {
        assertNotNull(converter.convert(new NotesCommand()));
    }

    @Test
    public void testConvertNOtesCommandToNotes() throws Exception {
        NotesCommand source = new NotesCommand();
        source.setId(ID);
        source.setRecipeNotes(RECIPE_NOTES);

        Notes result = converter.convert(source);
        assertEquals(ID, result.getId());
        assertEquals(RECIPE_NOTES, result.getRecipeNotes());
    }
}