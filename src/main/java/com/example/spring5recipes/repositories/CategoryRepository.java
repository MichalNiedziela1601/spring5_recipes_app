package com.example.spring5recipes.repositories;

import com.example.spring5recipes.domain.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
