package com.example.spring5recipes.controllers;

import com.example.spring5recipes.commands.RecipeCommand;
import com.example.spring5recipes.services.ImageService;
import com.example.spring5recipes.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Controller
public class ImageController {

    private final ImageService imageService;
    private final RecipeService recipeService;

    public ImageController(ImageService imageService, RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping("recipe/{id}/image")
    public String showUploadForm(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(id)));
        return "recipe/uploadimageform";
    }

    @PostMapping("recipe/{id}/image")
    public String handleUploadImage(@PathVariable String id, @RequestParam("imagefile")MultipartFile file) {
        imageService.saveImageFile(Long.valueOf(id), file);

        return "redirect:/recipe/show/"+ id;
    }

    @GetMapping("recipe/{id}/recipeimage")
    public void renderImageFromDB(@PathVariable String id, HttpServletResponse response) throws IOException {
        RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(id));
        int imageLength = 0;
        Byte[] image;

        if(recipeCommand.getImage() != null && recipeCommand.getImage().length > 0) {
            imageLength = recipeCommand.getImage().length;
            image = recipeCommand.getImage();
        } else {
            Byte[] defaultImage = convertInputStream(readFileFromResource("static/defaultImage.jpg"));
            imageLength = defaultImage != null ? defaultImage.length : 0;
            image = defaultImage;
        }
        byte[] byteArray = new byte[imageLength];

        int i = 0;
        for(Byte b : image) {
            byteArray[i++] = b;
        }

        response.setContentType("image/jpeg");
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        IOUtils.copy(inputStream, response.getOutputStream());
    }

    private InputStream readFileFromResource(String name) {
        Resource resource = new ClassPathResource(name);
        try {
            return resource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Byte[] convertInputStream(InputStream stream) {

        try {
            byte[] target = new byte[stream.available()];
            stream.read(target);
            Byte[] fileBytes = new Byte[target.length];
            int i = 0;
            for (byte t : target) {
                fileBytes[i++] = t;
            }
            return fileBytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
