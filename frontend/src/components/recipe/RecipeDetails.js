import React, {Component} from 'react';
import axios from 'axios';

export default class RecipeDetails extends Component {

    constructor(props) {
        super(props);
        this.state = {
            recipe: {},
            id: this.props.match.params.id
        }
    }

    componentDidMount() {
        return axios.get('http://localhost:8080/api/recipe/show/' + this.props.match.params.id).then((res) => {
            this.setState({recipe: res.data});
        })
            .catch(err => console.log('Error: ', err));
    }

    render() {
        const img_url = "http://localhost:8080/api/recipe/" + this.state.id + "/recipeimage";

        const categories = this.state.recipe.categories ? this.state.recipe.categories.map(category =>
            <li key={category.id}>{category.description}</li>
        ) : [];
        const ingredients = this.state.recipe.ingredients ? this.state.recipe.ingredients.map(ingredient =>
            <li key={ingredient.id}>{ingredient.amount} {ingredient.uom.description} {ingredient.description}</li>
        ) : [];
        return (
            <div className="container">
                <div className="row">
                    <div className="col-md-6">
                        <h2 className="page-header text-uppercase">{this.state.recipe.description}</h2>

                        <div className="text-left">
                            <h6><b>Categories:</b></h6>
                            <ul>{categories}</ul>
                            <h6><b>Difficulty: </b>{this.state.recipe.difficulty}</h6>
                            <h6><b>Servings:</b> {this.state.recipe.servings}</h6>
                            <h6><b>Cook Time:</b> {this.state.recipe.cookTime}</h6>
                            <h6><b>Preparation Time:</b> {this.state.recipe.prepTime}</h6>
                            <h6><b>Source:</b> {this.state.recipe.source}</h6>
                            <h6><b>URL:</b> {this.state.recipe.url}</h6>

                            <h6><b>Ingredients</b></h6>
                            <ul>{ingredients}</ul>

                            <h6><b>Directions</b></h6>
                            <p>{this.state.recipe.directions}</p>

                            <h6><b>Notes</b></h6>
                            <p>{this.state.recipe.notes ? this.state.recipe.notes.recipeNotes : ''}</p>

                        </div>

                    </div>

                    <div className="col-md-6">
                        <img src={img_url} className="full-width img-responsive"/>
                    </div>
                </div>

            </div>
        )
    }
}