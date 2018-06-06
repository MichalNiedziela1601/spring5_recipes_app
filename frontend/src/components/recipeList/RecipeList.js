import React, {Component} from 'react';
import './RecipeList.css';
import axios from 'axios';
import RecipeListItem from '../recipeListItem/RecipeListItem';
import {Link} from "react-router-dom";
import {config} from '../config';

export default class RecipeList extends Component {



    constructor(props) {
        super(props);
        this.state = {
            recipes: []
        }
    }

    componentDidMount() {
       return axios.get(config.URL).then((res) => {
            this.setState({recipes: res.data});
        })
            .catch(err => {
                console.error('Error: ' + err)
            });
    }

    render() {

        const recipes = this.state.recipes ? this.state.recipes.map(recipe =>
            <tr key={recipe.id}>
                <td><RecipeListItem recipe={recipe}/></td>
                <td><Link to={`recipe/update/${recipe.id}`}>Update</Link></td>
                <td>Delete</td>
            </tr>
        ) : [];
        return (
             <div className="recipe-list container" id="recipe-list">
                 <Link to={'/recipe/new'}><button className='btn btn-primary'>Add new</button></Link>
                 <table className="table">
                 <thead>
                 <tr>
                     <th>Recipe</th>
                     <th>Update</th>
                     <th>Delete</th>
                 </tr>
                 </thead>
                 <tbody>
                 {recipes}
                 </tbody>
             </table>
             </div>
        )
    }


}