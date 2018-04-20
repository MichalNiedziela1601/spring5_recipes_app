import React, {Component} from 'react';
import './RecipeList.css';
import axios from 'axios';

export default class RecipeList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            recipes: []
        }
    }

    componentDidMount() {
       return axios.get("http://localhost:8080/api/").then((res) => {
            return this.setState({recipes: res.data});
        })
            .catch(err => {
                console.error('Error: ' + err)
            });
    }

    render() {

        const recipes = this.state.recipes ? this.state.recipes.map(recipe =>
            <tr key={recipe.id}>
                <td>{recipe.description}</td>
                <td>View</td>
                <td>Update</td>
                <td>Delete</td>
            </tr>
        ) : [];
        return (
             <div className="recipe-list container" id="recipe-list"><table>
                 <thead>
                 <tr>
                     <th>Recipe</th>
                     <th>View</th>
                     <th>Update</th>
                     <th>Delete</th>
                 </tr>
                 </thead>
                 <tbody>
                 {recipes}
                 </tbody>
             </table></div>
        )
    }


}