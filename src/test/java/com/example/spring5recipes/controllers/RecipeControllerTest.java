package com.example.spring5recipes.controllers;

import com.example.spring5recipes.commands.IngredientCommand;
import com.example.spring5recipes.commands.RecipeCommand;
import com.example.spring5recipes.commands.UnitOfMeasureCommand;
import com.example.spring5recipes.domain.Recipe;
import com.example.spring5recipes.exceptions.NotFoundException;
import com.example.spring5recipes.services.CategoryService;
import com.example.spring5recipes.services.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecipeControllerTest {

    private RecipeController controller;

    @Mock
    RecipeService recipeService;

    @Mock
    CategoryService categoryService;

    MockMvc mvc;

    JacksonTester<RecipeCommand> jsonTester;

    @Autowired
    ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        controller = new RecipeController(recipeService, categoryService);
        objectMapper = new ObjectMapper();
        JacksonTester.initFields(this, objectMapper);

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
                .andExpect(jsonPath("$", is("For input string: \"asd\"")));
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
    public void givenRecipeCommandWhenPassToSaveThenReturnNewRecipe() throws Exception {
        RecipeCommand testRecipeCommand = new RecipeCommand();
        testRecipeCommand.setId(2L);
        testRecipeCommand.setDescription("description");

        final String recipeCommandJson = jsonTester.write(testRecipeCommand).getJson();

        when(recipeService.saveRecipeCommand(any())).thenReturn(testRecipeCommand);

        mvc.perform(post("/recipe/new").contentType(MediaType.APPLICATION_JSON)
                .content(recipeCommandJson)
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.description", is("description")));
    }

    @Test
    public void testPostNewRecipeFormValidationFail() throws Exception {

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        Set<IngredientCommand> ingredientCommandSet = new HashSet<>();
        IngredientCommand ing1 = new IngredientCommand();
        ing1.setRecipeId(recipeCommand.getId());
        ing1.setId(1L);
        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(1L);
        unitOfMeasureCommand.setDescription("Pint");
        ing1.setUom(unitOfMeasureCommand);
        ing1.setDescription("Beer");
        ing1.setAmount(new BigDecimal(3));
        ingredientCommandSet.add(ing1);

        recipeCommand.setServings(3);
        recipeCommand.setCookTime(3);
        recipeCommand.setPrepTime(50);
        recipeCommand.setUrl("http://example.com");
        recipeCommand.setIngredients(ingredientCommandSet);


        String jsonRecipe = jsonTester.write(recipeCommand).getJson();

        when(recipeService.saveRecipeCommand(any())).thenReturn(recipeCommand);

        mvc.perform(post("/recipe/new").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonRecipe)
        )
                .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.errors[0].name", is("description")))
        .andExpect(jsonPath("$.errors[0].errorDescription", is("must not be blank")))
        ;

    }

    @Test
    public void givenIdRecipeWhenUrlRecipeUpdateThenReturnRecipeCommand() throws Exception {

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);
        recipeCommand.setDescription("Taco");

        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        mvc.perform(get("/recipe/update/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("Taco")));
    }

    @Test
    public void givenRecipeIdWhenDeleteMethodThenDeleteRecipe() throws Exception {

        mvc.perform(delete("/recipe/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(recipeService, times(1)).deleteById(anyLong());
    }

}