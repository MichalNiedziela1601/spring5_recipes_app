import React, {Component} from 'react';
import axios from 'axios';
import './recipeDetails.css';
import TextUtills from "../../utils/TextUtils";
import {config} from "../config";

export default class RecipeDetails extends Component {

    constructor(props) {
        super(props);
        this.state = {
            recipe: {},
            id: this.props.match.params.id
        }
    }

    componentDidMount() {
        return axios.get(`${config.URL}recipe/show/${this.props.match.params.id}`).then((res) => {
            this.setState({recipe: res.data});
        })
            .catch(err => console.log('Error: ', err));
    }

    render() {
        const img_url = config.URL + "recipe/" + this.state.id + "/recipeimage";

        const categories = this.state.recipe.categories ? this.state.recipe.categories.map((category, ind, arr) =>
            arr.length - 1 === ind ? category.description  : category.description + ', '
        ) : [];
        const ingredients = this.state.recipe.ingredients ? this.state.recipe.ingredients.map(ingredient =>
            <li key={ingredient.id}>{ingredient.amount} {ingredient.uom.description} {ingredient.description}</li>
        ) : [];

        const cookTimeArr = TextUtills.convertTime(this.state.recipe.cookTime);
        const prepTimeArr = TextUtills.convertTime(this.state.recipe.prepTime);
        const prepTime  = TextUtills.timeRender(prepTimeArr);
        const cookTime  = TextUtills.timeRender(cookTimeArr);

        return (
            <div className="container recipe-details">
                <div className="row">
                    <div className="col-md-12 text-center recipe-title">
                        <h2 className="page-header text-uppercase">{this.state.recipe.description}</h2>
                    </div>
                    <div className="col-md-6">


                        <div className="text-left">
                            <div className="info text-uppercase">
                                <div><span className="font-weight-bold">category: </span> <span className="margin-right-1">{categories}</span>
                                <span className="font-weight-bold">difficulty:</span> <span className="margin-right-1">{this.state.recipe.difficulty}</span>
                                <span className="font-weight-bold">Servings:</span> <span className="margin-right-1">{this.state.recipe.servings}</span></div>
                                <div><span className="font-weight-bold">Preparation Time:</span> <span className="margin-right-1">{prepTime}</span>
                                <span className="font-weight-bold">cooking Time:</span> <span className="margin-right-1">{cookTime}</span></div>
                            </div>
                            <div id="recipe-notes">
                                {this.state.recipe.notes ? this.state.recipe.notes.recipeNotes : ''}
                            </div>

                            <div className="recipe-ingredients">
                                <div className="title"><h4 className="text-uppercase">Ingredients</h4></div>
                                <ul>{ingredients}</ul>
                            </div>




                        </div>

                    </div>

                    <div className="col-md-6">
                        <img src={img_url} className="full-width img-responsive"/>
                    </div>

                    <div className="col-md-12">
                        <h6><b>Directions</b></h6>
                        <p>{this.state.recipe.directions}</p>
                    </div>
                </div>

            </div>
        )
    }
}