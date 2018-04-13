package com.example.spring5recipes.services;

import com.example.spring5recipes.commands.RecipeCommand;
import com.example.spring5recipes.converters.RecipeCommandToRecipe;
import com.example.spring5recipes.converters.RecipeToRecipeCommand;
import com.example.spring5recipes.domain.Recipe;
import com.example.spring5recipes.exceptions.NotFoundException;
import com.example.spring5recipes.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    RecipeServiceImpl recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }

    @Test
    public void getRecipes() throws Exception {
        Recipe recipe = new Recipe();
        HashSet<Recipe> recipesData = new HashSet<>();
        recipesData.add(recipe);

        when(recipeService.getRecipes()).thenReturn(recipesData);

        Set<Recipe> recipes = recipeService.getRecipes();

        assertEquals(recipes.size(), 1);
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    public void findById() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);

        Optional<Recipe> recipe1 = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(recipe1);

        Recipe recipeReturned = recipeService.findById(1L);

        assertNotNull("Null recipe returned", recipeReturned);
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, never()).findAll();
    }

    @Test(expected = NotFoundException.class)
    public void testFindByIdNotFoundException() throws Exception {
        Optional<Recipe> recipeOptional = Optional.empty();

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        Recipe recipe = recipeService.findById(1L);
    }

    @Test
    public void findCommandByIdTest() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(2L);

        Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(2L);

        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        RecipeCommand recipeCommandById = recipeService.findCommandById(2L);

        assertNotNull(recipeCommandById);
        verify(recipeRepository, times(1)).findById(anyLong());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    public void givenRecipeIdWhenDeleteByIdThenDeleteRecipe() throws Exception {
        Long recipeId = 2L;

        recipeService.deleteById(recipeId);

        verify(recipeRepository, times(1)).deleteById(anyLong());
    }
}