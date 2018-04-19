import React, {Component} from 'react';
import logo from './logo.svg';
import './App.css';
import axios from 'axios';

class App extends Component {


    constructor(props) {
        super(props);
        this.state = {
            recipes: []
        }
    }

    componentDidMount() {
        axios.get("http://localhost:8080/api/").then((res) => {
            console.log(res.data);
            this.setState({recipes: res.data});
        })
            .catch(err => {
                console.log('Error: ' + err)
            });
    }

    render() {
        const recipes = this.state.recipes.map(recipe =>
            <li key={recipe.id}>
                <div className="recipe">
                    <div className="recipe-title">
                        {recipe.description}
                    </div>
                    <div className="recipe-body">
                        {recipe.notes.recipeNotes}
                    </div>
                </div>
            </li>
        );

        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1 className="App-title">Welcome to React</h1>
                </header>
                <p className="App-intro">
                    To get started, edit <code>src/App.js</code> and save to reload.
                </p>

                <div>
                    <ul className="App-recipe-list">{recipes}</ul>
                </div>
            </div>
        );
    }
}

export default App;
