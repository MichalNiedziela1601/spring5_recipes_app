package com.example.spring5recipes.services;

import com.example.spring5recipes.commands.IngredientCommand;
import com.example.spring5recipes.converters.IngredientCommandToIngredient;
import com.example.spring5recipes.converters.IngredientToIngredientCommand;
import com.example.spring5recipes.domain.Ingredient;
import com.example.spring5recipes.domain.Recipe;
import com.example.spring5recipes.exceptions.NotFoundException;
import com.example.spring5recipes.repositories.RecipeRepository;
import com.example.spring5recipes.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(
            IngredientCommandToIngredient ingredientCommandToIngredient,
            IngredientToIngredientCommand ingredientToIngredientCommand,
            RecipeRepository recipeRepository,
            UnitOfMeasureRepository unitOfMeasureRepository
    ) {
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if(!recipeOptional.isPresent()) {
            log.error("Recipe not found: " + recipeId);
            throw new NotFoundException("Recipe not found");
        }

        Recipe recipe = recipeOptional.get();

        Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients()
                .stream()
                .filter(e -> e.getId().equals(ingredientId))
                .map(ingredient -> ingredientToIngredientCommand.convert(ingredient))
                .findFirst();

        if(!ingredientCommandOptional.isPresent()) {
            log.error("Ingredient not found: " + ingredientId);
            throw new NotFoundException("Ingredient not found");
        }

        return ingredientCommandOptional.get();
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand) {

        Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientCommand.getRecipeId());

        if(!recipeOptional.isPresent()) {
            log.debug("Recipe not found: " + ingredientCommand.getRecipeId());
            return new IngredientCommand();
        } else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe.getIngredients()
                    .stream().filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                    .findFirst();

            if(ingredientOptional.isPresent()) {
                Ingredient ingredientFound = ingredientOptional.get();
                log.info("ingredient found: " + ingredientFound);
                ingredientFound.setId(ingredientCommand.getId());
                ingredientFound.setDescription(ingredientCommand.getDescription());
                ingredientFound.setAmount(ingredientCommand.getAmount());
                ingredientFound.setUom(
                        unitOfMeasureRepository.findById(ingredientCommand.getUom().getId())
                .orElseThrow(() -> new RuntimeException("UOM not found")));
                ingredientFound.setRecipe(recipe);
            } else {
                Ingredient ingredient = ingredientCommandToIngredient.convert(ingredientCommand);
                ingredient.setRecipe(recipe);
                recipe.addIngredients(ingredient);
            }

            Recipe savedRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream().filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
                    .findFirst();

            if(!savedIngredientOptional.isPresent()) {
                savedIngredientOptional = savedRecipe.getIngredients()
                        .stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(ingredientCommand.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(ingredientCommand.getAmount()))
                        .filter(recipeIngredient -> recipeIngredient.getUom().getId().equals(ingredientCommand.getUom().getId()))
                        .findFirst();
            }

            return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
        }

    }

    @Override
    public void deleteById(long recipeId, long ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if(!recipeOptional.isPresent()) {
            throw new RuntimeException("Recipe not found: " + recipeId);
        }

        Recipe recipe = recipeOptional.get();
        Optional<Ingredient> ingredientFound = recipe.getIngredients().stream().filter(ingredient -> ingredient.getId().equals(ingredientId)).findFirst();
        if(ingredientFound.isPresent()) {
            Ingredient ingredientToDelete = ingredientFound.get();
            ingredientToDelete.setRecipe(null);
            recipe.getIngredients().remove(ingredientFound.get());
            recipeRepository.save(recipe);
        }

    }
}
