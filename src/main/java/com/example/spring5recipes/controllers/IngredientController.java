package com.example.spring5recipes.controllers;

import com.example.spring5recipes.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class IngredientController {

    private RecipeService recipeService;

    public IngredientController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredients")
    public String showRecipeIngredients(@PathVariable String recipeId, Model model) {
        model.addAttribute("recipe",recipeService.findCommandById(Long.valueOf(recipeId)));

        return "recipe/ingredients/list";
    }
}
