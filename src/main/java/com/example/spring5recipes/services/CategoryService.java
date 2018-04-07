package com.example.spring5recipes.services;

import com.example.spring5recipes.domain.Category;

import java.util.Set;

public interface CategoryService {

    Set<Category> getCategories();
}
