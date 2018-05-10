package com.example.spring5recipes.controllers;


import com.example.spring5recipes.commands.RecipeCommand;
import com.example.spring5recipes.domain.Recipe;
import com.example.spring5recipes.exceptions.NotFoundException;
import com.example.spring5recipes.services.CategoryService;
import com.example.spring5recipes.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Slf4j
@Controller
public class RecipeController {

    private final RecipeService recipeService;
    private final CategoryService categoryService;

    public RecipeController(RecipeService recipeService, CategoryService categoryService) {
        this.recipeService = recipeService;
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "recipe/show/{id}")
    @ResponseBody
    public ResponseEntity<Recipe> findById(@PathVariable String id) {
        Recipe recipe = recipeService.findById(new Long(id));
        return new ResponseEntity<Recipe>(recipe, HttpStatus.OK);
    }


    @PostMapping
    @RequestMapping("recipe/new")
    public ResponseEntity<RecipeCommand> saveOrUpdate(@Valid @RequestBody RecipeCommand command, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });
            return new ResponseEntity<RecipeCommand>(command, HttpStatus.BAD_REQUEST);
        }

        RecipeCommand savedRecipe = recipeService.saveRecipeCommand(command);

        return new ResponseEntity<RecipeCommand>(savedRecipe, HttpStatus.OK);
    }

    @RequestMapping("recipe/update/{id}")
    public String updateRecipe(@PathVariable String id, Model model) {
        RecipeCommand command = recipeService.findCommandById(Long.valueOf(id));

        model.addAttribute("recipe", command);
        model.addAttribute("categories", categoryService.getCategories());

        return "recipe/form";
    }

    @RequestMapping("recipe/delete/{id}")
    public String deleteRecipe(@PathVariable String id) {
        recipeService.deleteById(Long.valueOf(id));
        return "redirect:/";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("404error");
        modelAndView.addObject("exception", e);
        return modelAndView;
    }


}
