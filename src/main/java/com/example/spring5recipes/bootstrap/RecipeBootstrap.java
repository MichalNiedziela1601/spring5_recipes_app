package com.example.spring5recipes.bootstrap;

import com.example.spring5recipes.domain.*;
import com.example.spring5recipes.repositories.CategoryRepository;
import com.example.spring5recipes.repositories.RecipeRepository;
import com.example.spring5recipes.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public RecipeBootstrap(RecipeRepository recipeRepository, CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }



    private List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>(2);

        Optional<UnitOfMeasure> eachUomOptional = getOptionalUOM("Each");

        Optional<UnitOfMeasure> tablespoonUomOptional = getOptionalUOM("Tablespoon");

        Optional<UnitOfMeasure> teaspoonUomOptional = getOptionalUOM("Teaspoon");

        Optional<UnitOfMeasure> cupUomOptional = getOptionalUOM("Cup");

        Optional<UnitOfMeasure> pichUomOptional = getOptionalUOM("Pinch");

        Optional<UnitOfMeasure> ounceUomOptional = getOptionalUOM("Ounce");

        Optional<UnitOfMeasure> pintUomOptional = getOptionalUOM("Pint");

        Optional<UnitOfMeasure> dashUomOptional = getOptionalUOM("Dash");

        UnitOfMeasure eachUom = eachUomOptional.get();
        UnitOfMeasure tablespoonUom = tablespoonUomOptional.get();
        UnitOfMeasure teaspoonUom = teaspoonUomOptional.get();
        UnitOfMeasure cupUom = cupUomOptional.get();
        UnitOfMeasure pichUom = pichUomOptional.get();
        UnitOfMeasure ounceUom = ounceUomOptional.get();
        UnitOfMeasure pintUom = pintUomOptional.get();
        UnitOfMeasure dashUom = dashUomOptional.get();

        Category americanCategory = getCategoryByDescription("American");
        Category mexicanCategory = getCategoryByDescription("Mexican");

        Recipe guacRecipe = new Recipe();
        guacRecipe.setDescription("Perfect guacamole");
        guacRecipe.setPrepTime(10);
        guacRecipe.setCookTime(10);
        guacRecipe.setServings(2);
        guacRecipe.setDifficulty(Difficulty.EASY);
        guacRecipe.setDirections("Guacamole directions. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");


        Notes guacNotes = new Notes();
        guacNotes.setRecipeNotes("Guac Notes. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");

        guacNotes.setRecipe(guacRecipe);
        guacRecipe.setNotes(guacNotes);
        guacRecipe.addIngredients(new Ingredient( "ripe avokados", new BigDecimal(2), eachUom));
        guacRecipe.getCategories().add(americanCategory);
        guacRecipe.getCategories().add(mexicanCategory);

        saveImageToRecipe("static/guacamole.jpg", guacRecipe);

        recipes.add(guacRecipe);

        Recipe tacos = new Recipe();
        tacos.setDescription("Taco");
        tacos.setPrepTime(20);
        tacos.setCookTime(90);
        tacos.setDifficulty(Difficulty.EASY);
        tacos.setServings(4);
        tacos.setDirections("Prepare chicken breast on the fry pan. Dice paprika, onions and lettuce. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        Notes tacosNotes = new Notes();
        tacosNotes.setRecipeNotes("Tacos is very easy mecixan dishes. Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");

        tacos.setNotes(tacosNotes);
        tacos.addIngredients(new Ingredient( "chicken breast", new BigDecimal(2), pintUom));
        tacos.addIngredients(new Ingredient("paprika", new BigDecimal(1), eachUom));
        tacos.addIngredients(new Ingredient("onion", new BigDecimal(2), eachUom));

        tacos.getCategories().add(mexicanCategory);
        saveImageToRecipe("static/taco.jpg", tacos);

        recipes.add(tacos);
        return recipes;
    }

    private Optional<UnitOfMeasure> getOptionalUOM(String description) {
        Optional<UnitOfMeasure> uomOptional = unitOfMeasureRepository.findByDescription(description);
        if(!uomOptional.isPresent()) {
            throw new RuntimeException("Expected UOM not found");
        }
        return uomOptional;
    }

    private Category getCategoryByDescription(String description) {
        Optional<Category> categoryOptional = categoryRepository.findByDescription(description);

        if(!categoryOptional.isPresent()) {
            throw new RuntimeException("Expected Category not found");
        }

        return categoryOptional.get();
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

    private void saveImageToRecipe(String name, Recipe recipe) {
        InputStream stream = readFileFromResource(name);
        Byte[] image = convertInputStream(stream);
        recipe.setImage(image);
    }


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        recipeRepository.saveAll(getRecipes());
        log.debug("Loading Bootstrap recipes");
    }
}
