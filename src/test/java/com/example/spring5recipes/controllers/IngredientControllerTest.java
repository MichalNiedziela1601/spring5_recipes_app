package com.example.spring5recipes.controllers;

import com.example.spring5recipes.commands.IngredientCommand;
import com.example.spring5recipes.commands.RecipeCommand;
import com.example.spring5recipes.exceptions.NotFoundException;
import com.example.spring5recipes.services.IngredientService;
import com.example.spring5recipes.services.RecipeService;
import com.example.spring5recipes.services.UnitOfMeasureService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IngredientControllerTest {

    private IngredientController controller;

    @Mock
    RecipeService recipeService;

    @Mock
    IngredientService ingredientService;

    @Mock
    UnitOfMeasureService unitOfMeasureService;

    MockMvc mockMvc;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        controller = new IngredientController(recipeService, ingredientService, unitOfMeasureService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerAdviceHandler())
                .build();
    }

    @Test
    public void tesIngredientsListPage() throws Exception {

        RecipeCommand command = new RecipeCommand();
        when(recipeService.findCommandById(anyLong())).thenReturn(command);

        mockMvc.perform(get("/recipe/1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredients/list"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testShowIngredient() throws Exception {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(2L);

        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);

        mockMvc.perform(get("/recipe/1/ingredients/2/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredients/show"))
                .andExpect(model().attributeExists("ingredient"));
    }

    @Test
    public void testIngredientNotFound() throws Exception {
        IngredientCommand command = new IngredientCommand();
        command.setId(1L);

        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(),anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform((get("/recipe/1/ingredients/2/show")))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    public void testBadRequestIngredient() throws Exception {
        mockMvc.perform(get("/recipe/1/ingredients/dfg/show"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

    @Test
    public void testUpdateIngredientForm() throws Exception {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(2L);

        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);

        mockMvc.perform(get("/recipe/1/ingredients/2/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredients/form"))
                .andExpect(model().attributeExists("ingredient"));
    }

    @Test
    public void testSaveOrUpdateIngredientCommand() throws Exception {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(4L);
        ingredientCommand.setRecipeId(2L);

        when(ingredientService.saveIngredientCommand(any())).thenReturn(ingredientCommand);

        mockMvc.perform(post("/recipe/2/ingredient")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("id","")
        .param("description", "ingredient"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/2/ingredients/4/show"));
    }

    @Test
    public void testSaveIngredientValidationFail() throws Exception {
        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(4L);
        ingredientCommand.setRecipeId(2L);

        when(ingredientService.saveIngredientCommand(any())).thenReturn(ingredientCommand);

        mockMvc.perform(post("/recipe/2/ingredient")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id",""))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredients/form"));
    }

    @Test
    public void testDeleteIngredient() throws Exception {

        mockMvc.perform(get("/recipe/1/ingredients/2/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/ingredients"));

        verify(ingredientService, times(1)).deleteById(anyLong(), anyLong());
    }
}