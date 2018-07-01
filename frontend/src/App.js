import React, {Component} from 'react';
import './App.css';
import RecipeDetails from "./components/recipe/RecipeDetails";
import RecipeList from "./components/recipeList/RecipeList";
import {BrowserRouter as Router, Link, Route} from "react-router-dom";
import RecipeUpdate from "./components/recipeUpdate/RecipeUpdate";
import NewRecipe from "./components/newRecipe/NewRecipe";
import LoginComponent from './components/login/LoginComponent';

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            username: null
        }

    }

    setUsername = (username) => {
        this.setState({username});
    }

    render() {
        return (

            <Router>
                <div className="App">
                    <header className="App-header">
                        {/*<img src={logo} className="App-logo" alt="logo"/>*/}
                        <h1 className="App-title">Welcome to All Recipes</h1>

                        <Link to={'/'}>Home</Link>
                        {this.state.username ? <div className="container">
                                Logged in as: <span id="user">{this.state.username}</span>
                            </div>:
                        <div className="container text-right ">
                            <Link to={'/login'}><button className={'btn'}>Log in / Sign in</button></Link>
                        </div>

                        }
                    </header>

                    <Route exact path='/' component={RecipeList}/>
                    <Route path='/recipe/:id/show' component={RecipeDetails}/>
                    <Route path='/recipe/new' component={NewRecipe}/>
                    <Route path='/recipe/update/:id' component={RecipeUpdate}/>
                    <Route path='/login'
                           render={(props) => <LoginComponent {...props} setParentUsername={this.setUsername}/>} />
                </div>

            </Router>
        );
    }
}

export default App;
