package com.example.spring5recipes.controllers;


import com.example.spring5recipes.commands.RecipeCommand;
import com.example.spring5recipes.domain.Recipe;
import com.example.spring5recipes.services.CategoryService;
import com.example.spring5recipes.services.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RecipeController {

    private final RecipeService recipeService;
    private final CategoryService categoryService;

    public RecipeController(RecipeService recipeService, CategoryService categoryService) {
        this.recipeService = recipeService;
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "recipe/show/{id}")
    public String findById(@PathVariable String id, Model model) {
        Recipe recipe = recipeService.findById(new Long(id));
        model.addAttribute("recipe", recipe);
        return "recipe/show";
    }

    @RequestMapping("recipe/new")
    public String addNewRecipe(Model model) {
        model.addAttribute("recipe", new RecipeCommand());
        model.addAttribute("categories", this.categoryService.getCategories());

        return "recipe/form";
    }

    @PostMapping
    @RequestMapping("recipe")
    public String saveOrUpdate(@ModelAttribute RecipeCommand command) {
        RecipeCommand savedRecipe = recipeService.saveRecipeCommand(command);

        return "redirect:/recipe/show/" + savedRecipe.getId();
    }
}
