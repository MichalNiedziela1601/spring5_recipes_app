import React, {Component} from 'react';
import RecipeForm from "../recipeForm/RecipeForm";

export default class NewRecipe extends Component {

    constructor(props) {
        super(props);
        this.state = {
            recipe: {
                description: '',
                cookTime: 0,
                prepTime: 0,
                servings: 0,
                source: '',
                url: '',
                difficulty: '',
                ingredients: [],
                directions: '',
                categories: [],
                notes: {
                    recipeNotes: ''
                }
            }
        }
    }

    render() {
        return (
            <RecipeForm update={false} recipe={this.state.recipe}/>
        )
    }

}