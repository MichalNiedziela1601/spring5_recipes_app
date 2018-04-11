package com.example.spring5recipes.services;

import com.example.spring5recipes.commands.CategoryCommand;
import com.example.spring5recipes.converters.CategoryToCategoryCommand;
import com.example.spring5recipes.repositories.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private CategoryToCategoryCommand categoryToCategoryCommand = new CategoryToCategoryCommand();

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Set<CategoryCommand> getCategories() {
        return StreamSupport.stream(categoryRepository.findAll().spliterator(), false).map(categoryToCategoryCommand::convert).collect(Collectors.toSet());
    }
}
