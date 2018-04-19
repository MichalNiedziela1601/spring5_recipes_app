package com.example.spring5recipes.controllers;

import com.example.spring5recipes.domain.Recipe;
import com.example.spring5recipes.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
public class IndexController {

    private final RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"","/","/index"})
    public ResponseEntity<List<Recipe>> getIndexPage()
    {
        Set<Recipe> recipes = recipeService.getRecipes();
        return new ResponseEntity<List<Recipe>>(createRecipesList(recipes), HttpStatus.OK);
    }

    private List<Recipe> createRecipesList(Set<Recipe> recipeSet) {
        List<Recipe> recipeList = new ArrayList<>();

        for(Recipe recipe : recipeSet) {
            recipeList.add(recipe);
        }

        return recipeList;
    }

}
