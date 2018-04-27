package com.example.spring5recipes.controllers;

import com.example.spring5recipes.commands.RecipeCommand;
import com.example.spring5recipes.domain.Recipe;
import com.example.spring5recipes.exceptions.NotFoundException;
import com.example.spring5recipes.services.CategoryService;
import com.example.spring5recipes.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecipeControllerTest {

    private RecipeController controller;

    @Mock
    Model model;

    @Mock
    RecipeService recipeService;

    @Mock
    CategoryService categoryService;

    MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        controller = new RecipeController(recipeService, categoryService);

        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerAdviceHandler())
                .build();
    }


    @Test
    public void testBadRecipeIdGIveStatusNOtFound() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        when(recipeService.findById(anyLong())).thenThrow(NotFoundException.class);

        mvc.perform(get("/recipe/show/1"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    public void testBadRequestGivenNumberFormatException() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        when(recipeService.findById(any())).thenThrow(NumberFormatException.class);

        mvc.perform(get("/recipe/show/asd"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

    @Test
    public void givenIdRecipeWhenUrlRecipeShowThenReturnRecipe() throws Exception {

        Recipe command = new Recipe();
        command.setId(1L);
        command.setDescription("Taco");

        when(recipeService.findById(anyLong())).thenReturn(command);

        mvc.perform(get("/recipe/show/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.description", is("Taco")));
    }

    @Test
    public void testGetNewRecipeView() throws Exception {
        mvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/form"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void givenRecipeCommandWhenPassToSaveThenReturnNewRecipe() throws Exception {
        RecipeCommand testRecipeCommand = new RecipeCommand();
        testRecipeCommand.setId(2L);
        testRecipeCommand.setDescription("description");

        when(recipeService.saveRecipeCommand(any())).thenReturn(testRecipeCommand);

        mvc.perform(post("/recipe").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "description")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/show/2"));
    }

    @Test
    public void testPostNewRecipeFormValidationFail() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        when(recipeService.saveRecipeCommand(any())).thenReturn(recipeCommand);

        mvc.perform(post("/recipe").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/form"));

    }

    @Test
    public void testGetUpdateView() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(2L);

        mvc.perform(get("/recipe/update/2"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/form"));
    }

    @Test
    public void givenRecipeIdWhenDeleteMethodThenDeleteRecipe() throws Exception {

        mvc.perform(delete("/recipe/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(recipeService, times(1)).deleteById(anyLong());
    }

}