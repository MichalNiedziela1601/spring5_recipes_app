package com.example.spring5recipes.services;

import com.example.spring5recipes.domain.Recipe;
import com.example.spring5recipes.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImageFile(Long recipeId, MultipartFile file) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if(recipeOptional.isPresent()) {
            try {
                Recipe recipe = recipeOptional.get();

                Byte[] byteObject = new Byte[file.getBytes().length];

                int i =0;

                for(byte b : file.getBytes()) {
                    byteObject[i++] = b;
                }

                recipe.setImage(byteObject);
                recipeRepository.save(recipe);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
