package com.example.spring5recipes.controllers;

import org.junit.Test;

import static org.junit.Assert.*;

public class IndexControllerTest {

    private IndexController indexController;
    @Test
    public void WhenCallThenReturnIndex() {
        indexController = new IndexController();

        final String indexPage = indexController.getIndexPage();

        assertEquals(indexPage, "index");
    }

}