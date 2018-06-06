import React, {Component} from 'react';
import axios from 'axios';
import RecipeForm from "../recipeForm/RecipeForm";
import {config} from "../config";

export default class RecipeUpdate extends Component {

    constructor(props) {
        super(props);
        this.state = {
            recipe: {}
        }
    }

    componentWillMount() {
        return axios.get(`${config.URL}recipe/update/${this.props.match.params.id}`)
            .then(res => this.setState({recipe: res.data}))
    }

    render() {
        return (
            <div>
                <RecipeForm update={true} id={this.props.match.params.id} recipe={this.state.recipe}/>
            </div>
        )
    }

}