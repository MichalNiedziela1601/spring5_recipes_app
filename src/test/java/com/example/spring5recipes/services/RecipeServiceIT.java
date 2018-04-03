package com.example.spring5recipes.services;


import com.example.spring5recipes.commands.RecipeCommand;
import com.example.spring5recipes.converters.RecipeCommandToRecipe;
import com.example.spring5recipes.converters.RecipeToRecipeCommand;
import com.example.spring5recipes.domain.Recipe;
import com.example.spring5recipes.repositories.RecipeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecipeServiceIT {

    public static final String NEW_DESCRIPTION = "new recipe";

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Autowired
    RecipeToRecipeCommand recipeToRecipeCommand;

    @Test
    @Transactional
    public void testSaveRecipeCommand() throws Exception {
        //given
        Iterable<Recipe> recipes = recipeRepository.findAll();
        Recipe testRecipe = new Recipe();
        RecipeCommand testRecipeCommand = new RecipeCommand();

        //when
        testRecipeCommand.setDescription(NEW_DESCRIPTION);
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(testRecipeCommand);

        //then
        assertEquals(NEW_DESCRIPTION, savedRecipeCommand.getDescription());
        assertEquals(testRecipe.getId(), savedRecipeCommand.getId());
        assertEquals(testRecipe.getCategories().size(), savedRecipeCommand.getCategories().size());
        assertEquals(testRecipe.getIngredients().size(), savedRecipeCommand.getIngredients().size());

    }
}
