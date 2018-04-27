import React, {Component} from 'react';
import logo from './logo.svg';
import './App.css';
import RecipeDetails from "./components/recipe/RecipeDetails";
import RecipeList from "./components/RecipeList";
import {BrowserRouter as Router, Route} from "react-router-dom";

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
                </div>

            </Router>
        );
    }
}

export default App;
