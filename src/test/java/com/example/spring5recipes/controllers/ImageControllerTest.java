package com.example.spring5recipes.controllers;

import com.example.spring5recipes.commands.RecipeCommand;
import com.example.spring5recipes.services.ImageService;
import com.example.spring5recipes.services.RecipeService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ImageControllerTest {

    private ImageController imageController;

    @Mock
    RecipeService recipeService;

    @Mock
    ImageService imageService;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        imageController = new ImageController(imageService,recipeService);

        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
    }

    @Test
    public void testUploadImageForm() throws Exception {

        RecipeCommand command = new RecipeCommand();
        command.setId(1L);

        when(recipeService.findCommandById(anyLong())).thenReturn(command);

        mockMvc.perform(get("/recipe/1/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).findCommandById(anyLong());
    }

    @Test
    public void testHandleImagePost() throws Exception {
        MockMultipartFile file = new MockMultipartFile("imagefile", "testing.txt", "text/plain", "Spring image".getBytes());

        mockMvc.perform(multipart("/recipe/1/image").file(file))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location","/recipe/show/1"));

        verify(imageService,times(1)).saveImageFile(anyLong(), any());
    }

    @Test
    public void testRenderImage() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        String s = "test image";
        Byte[] imageBytes = new Byte[s.getBytes().length];

        int i = 0;

        for(Byte b : s.getBytes()) {
            imageBytes[i++] = b;
        }
        recipeCommand.setImage(imageBytes);

        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        byte[] responseImage = response.getContentAsByteArray();

        Assert.assertEquals(s.getBytes().length,responseImage.length);

    }
}