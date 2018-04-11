package com.example.spring5recipes.services;

import com.example.spring5recipes.commands.CategoryCommand;

import java.util.Set;

public interface CategoryService {

    Set<CategoryCommand> getCategories();
}
