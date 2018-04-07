package com.example.spring5recipes.controllers;

import com.example.spring5recipes.commands.RecipeCommand;
import com.example.spring5recipes.domain.Recipe;
import com.example.spring5recipes.services.CategoryService;
import com.example.spring5recipes.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void whenPassIdThenReturnRecipe() throws Exception {

        Set<Recipe> recipeSet = new HashSet<>();
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipeSet.add(recipe);

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        String id = "1";
        String result = controller.findById(id, model);

        assertEquals("recipe/show", result);
        verify(model, times(1)).addAttribute(eq("recipe"), argumentCaptor.capture());
    }

    @Test
    public void givenIdRecipeWhenUrlRecipeShowThenReturnRecipe() throws Exception {
        mvc.perform(get("/recipe/show/1"))
                .andExpect(status().isOk())
        .andExpect(view().name("recipe/show"));
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
        .param("description","description")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/show/2"));
    }

    @Test
    public void testGetUpdateView() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(2L);

        mvc.perform(get("/recipe/update/2"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/form"));
    }

}