import React from 'react';
import RecipeListItem from '../recipeListItem/RecipeListItem';

describe("RecipeListItem", () => {

    it("should convert time to hours and minutes", () => {
        const recipe = {
            id: 1,
            description: 'Test',
            cookTime: 67,
            notes: {
                recipeNotes: 'Test recipe notes'
            },
            directions: 'TEst directions',
            servings: 4
        };
        let recipeListItem = new RecipeListItem(recipe);
        let [h, m] = recipeListItem.convertTime(recipe.cookTime);

        expect(h).toEqual(1);
        expect(m).toEqual(7);
    });

    it("should convert time to minutes", () => {
        const recipe = {
            id: 1,
            description: 'Test',
            cookTime: 57,
            notes: {
                recipeNotes: 'Test recipe notes'
            },
            directions: 'TEst directions',
            servings: 4
        };
        let recipeListItem = new RecipeListItem(recipe);
        let [h, m] = recipeListItem.convertTime(recipe.cookTime);

        expect(h).toEqual(0);
        expect(m).toEqual(57);
    });

    it("should slice notes ", () => {
        const recipe = {
            id: 1,
            description: 'Test',
            cookTime: 67,
            notes: {
                recipeNotes: 'Test recipe notes with more then 125 characters to show convert text. Test recipe notes with more then 125 characters to show convert text.'
            },
            directions: 'TEst directions',
            servings: 4
        };
        let recipeListItem = new RecipeListItem(recipe);
        const tex  = recipeListItem.notesSlice(recipe.notes.recipeNotes);

        expect(tex).toEqual('Test recipe notes with more then 125 characters to show convert text. Test recipe notes with more then 125 characters to show...');
    })
})