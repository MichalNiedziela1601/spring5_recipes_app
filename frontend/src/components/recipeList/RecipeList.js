import React, {Component} from 'react';
import './RecipeList.css';
import axios from 'axios';
import RecipeListItem from '../recipeListItem/RecipeListItem';

export default class RecipeList extends Component {



    constructor(props) {
        super(props);
        this.state = {
            recipes: []
        }
    }

    componentDidMount() {
       return axios.get("http://localhost:8080/api/").then((res) => {
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
                <td>Update</td>
                <td>Delete</td>
            </tr>
        ) : [];
        return (
             <div className="recipe-list container" id="recipe-list">
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