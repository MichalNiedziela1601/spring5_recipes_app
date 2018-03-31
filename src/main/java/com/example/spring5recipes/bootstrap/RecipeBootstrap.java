package com.example.spring5recipes.bootstrap;

import com.example.spring5recipes.domain.*;
import com.example.spring5recipes.repositories.CategoryRepository;
import com.example.spring5recipes.repositories.RecipeRepository;
import com.example.spring5recipes.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
        guacRecipe.setCookTime(0);
        guacRecipe.setDifficulty(Difficulty.EASY);
        guacRecipe.setDirections("Guacamole directions");


        Notes guacNotes = new Notes();
        guacNotes.setRecipeNotes("Guac Notes");

        guacNotes.setRecipe(guacRecipe);
        guacRecipe.setNotes(guacNotes);
        guacRecipe.addIngredients(new Ingredient( "ripe avokados", new BigDecimal(2), eachUom));
        guacRecipe.getCategories().add(americanCategory);
        guacRecipe.getCategories().add(mexicanCategory);

        recipes.add(guacRecipe);

        Recipe tacos = new Recipe();
        tacos.setDescription("Taco");
        tacos.setPrepTime(20);
        tacos.setCookTime(0);
        tacos.setDifficulty(Difficulty.EASY);
        tacos.setDirections("Prepare chicken breast on the fry pan. Dice paprika, onions and lettuce");
        Notes tacosNotes = new Notes();
        tacosNotes.setRecipeNotes("Tacos is very easy mecixan dishes");

        tacos.setNotes(tacosNotes);
        tacos.addIngredients(new Ingredient( "chicken breast", new BigDecimal(2), pintUom));
        tacos.addIngredients(new Ingredient("paprika", new BigDecimal(1), eachUom));
        tacos.addIngredients(new Ingredient("onion", new BigDecimal(2), eachUom));

        tacos.getCategories().add(mexicanCategory);

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


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        recipeRepository.saveAll(getRecipes());
        log.debug("Loading Bootstrap recipes");
    }
}
