package com.example.spring5recipes.controllers;

import com.example.spring5recipes.commands.IngredientCommand;
import com.example.spring5recipes.commands.RecipeCommand;
import com.example.spring5recipes.commands.UnitOfMeasureCommand;
import com.example.spring5recipes.services.IngredientService;
import com.example.spring5recipes.services.RecipeService;
import com.example.spring5recipes.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class IngredientController {

    private RecipeService recipeService;
    private IngredientService ingredientService;
    private UnitOfMeasureService unitOfMeasureService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService, UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredients")
    public String showRecipeIngredients(@PathVariable String recipeId, Model model) {
        model.addAttribute("recipe",recipeService.findCommandById(Long.valueOf(recipeId)));

        return "recipe/ingredients/list";
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredients/{id}/show")
    public String showRecipeIngredient(@PathVariable String recipeId, @PathVariable String id, Model model) {

        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));

        return "recipe/ingredients/show";
    }

    @GetMapping
    @RequestMapping("recipe/{recipeId}/ingredients/{id}/update")
    public String updateIngredientForm(@PathVariable String recipeId, @PathVariable String id, Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));
        model.addAttribute("uoms", unitOfMeasureService.getUnitOfMeasures());

        return "recipe/ingredients/form";
    }

    @PostMapping
    @RequestMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdateIngredients(@ModelAttribute IngredientCommand command) {
        IngredientCommand savedIngredient = ingredientService.saveIngredientCommand(command);

        return "redirect:/recipe/" + savedIngredient.getRecipeId() + "/ingredients/" + savedIngredient.getId() + "/show";
    }

    @GetMapping("recipe/{recipeId}/ingredients/new")
    public String newIngredientForm(@PathVariable String recipeId, Model model) {
        RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(recipeId));
        IngredientCommand command = new IngredientCommand();
        command.setRecipeId(Long.valueOf(recipeId));

        model.addAttribute("ingredient", command);
        command.setUom(new UnitOfMeasureCommand());
        model.addAttribute("uoms", unitOfMeasureService.getUnitOfMeasures());

        return "recipe/ingredients/form";
    }


}
