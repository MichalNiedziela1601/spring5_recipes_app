import React, {Component} from 'react';
import logo from './logo.svg';
import './App.css';
import RecipeDetails from "./components/recipe/RecipeDetails";
import RecipeList from "./components/recipeList/RecipeList";
import RecipeForm from './components/recipeForm/RecipeForm';
import {BrowserRouter as Router, Route} from "react-router-dom";
import RecipeUpdate from "./components/RecipeUpdate";

class App extends Component {


    render() {
        return (

            <Router>
                <div className="App">
                    <header className="App-header">
                        <img src={logo} className="App-logo" alt="logo"/>
                        <h1 className="App-title">Welcome to React</h1>
                    </header>

                    <Route exact path='/' component={RecipeList}/>
                    <Route path='/recipe/:id/show' component={RecipeDetails}/>
                    <Route path='/recipe/new' component={RecipeForm}/>
                    <Route path='/recipe/update/:id' component={RecipeUpdate} />
                </div>

            </Router>
        );
    }
}

export default App;
