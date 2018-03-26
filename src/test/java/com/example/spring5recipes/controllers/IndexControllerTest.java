package com.example.spring5recipes.controllers;

import com.example.spring5recipes.repositories.RecipeRepository;
import com.example.spring5recipes.services.RecipeServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.Model;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class IndexControllerTest {

    private IndexController indexController;
    @Autowired
    private RecipeRepository recipeRepository;
    private Model model;
    @Test
    public void WhenCallThenReturnIndex() {
        indexController = new IndexController(new RecipeServiceImpl(recipeRepository));

        final String indexPage = indexController.getIndexPage(model);

        assertEquals(indexPage, "index");
    }

}