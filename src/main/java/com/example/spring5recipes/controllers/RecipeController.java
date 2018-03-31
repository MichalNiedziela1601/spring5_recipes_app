package com.example.spring5recipes.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RecipeController {


    @RequestMapping(value = "recipe/show/{id}")
    public String findById(@PathVariable String id, Model model) {
        return null;
    }
}
