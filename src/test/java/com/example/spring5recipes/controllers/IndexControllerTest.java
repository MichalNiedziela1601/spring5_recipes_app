package com.example.spring5recipes.controllers;

import com.example.spring5recipes.domain.Recipe;
import com.example.spring5recipes.services.RecipeService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
public class IndexControllerTest {

    IndexController indexController;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Mock
    RecipeService recipeService;

    @Before
    public void setUp() throws Exception {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
        this.mockMvc = builder.build();
//        MockitoAnnotations.initMocks(this);
        indexController = new IndexController(recipeService);
    }

    @Test
    public void testMOckMcv() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", Matchers.hasSize(2)));
    }

    @Test
    public void getIndexPage() throws Exception {

        //given
        Set<Recipe> recipes = new HashSet<>();
        Recipe r1 = new Recipe();
        r1.setId(1L);
        Recipe r2 = new Recipe();
        r2.setId(2L);
        recipes.add(r1);
        recipes.add(r2);

        when(recipeService.getRecipes()).thenReturn(recipes);

        //when
        ResponseEntity<List<Recipe>> indexPage = indexController.getIndexPage();

        //then
        assertEquals(2, indexPage.getBody().size());


        verify(recipeService, times(1)).getRecipes();
//        verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
//        Set<Recipe> setInController = argumentCaptor.getValue();
//        assertEquals(2, setInController.size());
    }
}