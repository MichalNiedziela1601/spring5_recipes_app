import React, {Component} from 'react';
import RecipeForm from "./recipeForm/RecipeForm";

export default class RecipeUpdate extends Component {

    render() {
        return (
            <div>
                <RecipeForm update={true} id={this.props.match.params.id}/>
            </div>
        )
    }

}