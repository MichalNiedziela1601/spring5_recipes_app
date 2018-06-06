import React, {Component} from 'react';
import axios from 'axios';
import {Redirect} from 'react-router-dom';
import {FormErrors} from './FormErrors';
import './RecipeForm.css';

export default class RecipeForm extends Component {

    constructor(props) {
        super(props);
        this.state = {
            difficultyArray: ['EASY', 'MODERATE', 'HARD'],
            recipe: {
                description: '',
                cookTime: 0,
                prepTime: 0,
                servings: 0,
                source: '',
                url: '',
                difficulty: '',
                ingredients: [],
                directions: '',
                categories: [],
                notes: {
                    recipeNotes: ''
                }

            },
            submitted: false,
            fireRedirect: false,
            formErrors: {},
            descriptionValid: !!props.update,
            formValid: !!props.update,
            cookTimeValid: !!props.update,
            prepTimeValid: !!props.update,
            directionsValid: !!props.update,
            uoms: [],
            categories: [],
            recipeCategories: []
        }
    }

    componentDidMount() {
        if (this.props.update) {
            return axios.get("http://localhost:8080/api/recipe/update/" + this.props.id)
                .then(res => this.setState({recipe: res.data}))
                .then(() => axios.get("http://localhost:8080/api/uoms").then(res =>
                    this.setState({uoms: res.data})
                ).then(() => {
                    return axios.get('http://localhost:8080/api/category');
                }).then(res => this.setState({categories: res.data})))
        } else {
            return axios.get("http://localhost:8080/api/uoms").then(res =>
                this.setState({uoms: res.data})
            ).then(() => {
                return axios.get('http://localhost:8080/api/category');
            }).then(res => this.setState({categories: res.data}))
        }

    }

    validateField(fieldName, value) {
        let fieldValidationErrors = this.state.formErrors;
        let descValid = this.state.descriptionValid;
        let cookTimeValid = this.state.cookTimeValid;
        let prepTimeValid = this.state.prepTimeValid;
        let directionValid = this.state.directionsValid;

        switch (fieldName) {
            case 'description':
                descValid = value.length >= 3;
                fieldValidationErrors.description = descValid ? '' : 'is invalid';
                break;
            case 'cookTime' :
                cookTimeValid = value > 0 && value < 999;
                fieldValidationErrors.cookTime = cookTimeValid ? '' : 'is invalid';
                break;

            case 'prepTime' :
                prepTimeValid = value > 0 && value < 999;
                fieldValidationErrors.prepTime = prepTimeValid ? '' : 'is invalid';
                break;

            case 'directions':
                directionValid = value.length > 5;
                fieldValidationErrors.directions = directionValid ? '' : 'is invalid';
                break;

            default:
                break;
        }
        this.setState({
            formErrors: fieldValidationErrors,
            descriptionValid: descValid,
            cookTimeValid: cookTimeValid,
            prepTimeValid: prepTimeValid
        }, this.validateForm);
    }

    validateForm() {
        this.setState({formValid: this.state.descriptionValid && this.state.cookTimeValid && this.state.prepTimeValid});
    }


    handleSubmit = (e) => {
        e.preventDefault();
        this.setState({submitted: true});


        axios.post("http://localhost:8080/api/recipe/new", this.state.recipe).then(res => {
            this.setState({fireRedirect: true, id: res.data.id});

        }, err => {
            console.log('err', err.errors);
        })
            .catch(err => console.log(err));
    };

    updateState = (event) => {
        const name = event.target.name;
        const value = event.target.value;
        const recipe = {...this.state.recipe};
        recipe[name] = value;
        this.setState({recipe}, () => {
            this.validateField(name, value)
        });
    };

    handleAddIngredient = () => {
        const recipe = {...this.state.recipe};
        recipe.ingredients = this.state.recipe.ingredients.concat([{
            amount: 0,
            uom: {description: this.state.uoms[0].description, id: this.state.uoms[0].id},
            description: ''
        }]);
        this.setState({recipe});
    };

    handleChangeIngredient = (idx) => (env) => {
        const name = env.target.name;
        const value = env.target.value;
        const newIngredients = this.state.recipe.ingredients.map((ingredient, sid) => {
            if (idx !== sid) return ingredient;
            const recipeId = this.props.update ? this.props.id : null;
            return {...ingredient, [name]: value, recipeId : recipeId};
        });
        const recipe = {...this.state.recipe};
        recipe.ingredients = newIngredients;

        this.setState({recipe});
    };
    handleChangeIngredientUom = (idx) => (env) => {
        const value = env.target.value;
        const newUom = this.state.recipe.ingredients.map((ingredient, sid) => {
            if (idx !== sid) return ingredient;
            const recipeId = this.props.update ? this.props.id : null;
            return {...ingredient, uom: this.state.uoms.find(obj => obj.id == value), recipeId};
        });
        const recipe = {...this.state.recipe};
        recipe.ingredients = newUom;

        this.setState({recipe});
    };

    updateStateNotes = (env) => {
        const value = env.target.value;
        const recipe = {...this.state.recipe};
        recipe.notes.recipeNotes = value;
        this.setState({recipe});
    };

    handleCategoryChange = (env) => {
        const recipe = {...this.state.recipe};
        let categories = recipe.categories;
        const value = env.target.value;
        const checkedCategory = this.state.categories[value];
        this.setState({recipe});
        const index = categories.findIndex(obj => obj.id === checkedCategory.id);
        if (index > -1) {
            categories.splice(index, 1);
        } else {
            categories.push(checkedCategory);
        }
        recipe.categories = categories;
        this.setState({recipe});
    };


    render() {
        const {id, fireRedirect} = this.state;
        const selectUoms = this.state.uoms.map((uom, index) => (
                <option value={uom.id} key={uom.id}>{uom.description}</option>
            )
        );
        const categoryCheckboxes = this.state.categories.map((category, idx) => (
            <div className="form-check" key={category.id}>
                <label className="form-check-label">
                    <input type="checkbox" className="form-check-input" value={idx} checked={this.state.recipe.categories.find(obj => obj.id === category.id)}
                           onChange={this.handleCategoryChange}/>{category.description}
                </label>
            </div>
        ));
        return (
            <div className="container recipe-form">
                <div className="panel panel-default">
                    <FormErrors formErrors={this.state.formErrors}/>
                </div>
                <form onSubmit={this.handleSubmit}>
                    <div className="form-group">
                        <label>Description</label>
                        <input type='text' name='description' id="description" onChange={this.updateState} value={this.state.recipe.description}
                               className="form-control"/>
                    </div>

                    <div className="form-row">
                        <div className="form-group col-md-4">
                            <label>Preparation Time</label>
                            <input type="text"
                                   name="prepTime"
                                   onChange={this.updateState}
                                   id="prepTime"
                                   className="form-control"
                                    value={this.state.recipe.prepTime}
                            />
                        </div>
                        <div className="form-group col-md-4">
                            <label>Cooking Time</label>
                            <input type="text"
                                   name="cookTime"
                                   id="cookTime"
                                   onChange={this.updateState}
                                   className="form-control"
                                   value={this.state.recipe.cookTime}/>
                        </div>
                        <div className="form-group col-md-4">
                            <label>Servings</label>
                            <input type="text"
                                   name="servings"
                                   id="servings"
                                   onChange={this.updateState}
                                   className="form-control"
                                   value={this.state.recipe.servings}/>
                        </div>
                    </div>
                    <div className={'form-row'}>
                        <div className="form-group col-md-6">
                            <label>Source</label>
                            <input type="text"
                                   name="source"
                                   id="source"
                                   onChange={this.updateState}
                                   className="form-control"
                                   value={this.state.recipe.source}/>
                        </div>
                        <div className="form-group col-md-6">
                            <label>URL</label>
                            <input type="url"
                                   name="url"
                                   id="url"
                                   onChange={this.updateState}
                                   className="form-control"
                                   value={this.state.recipe.url}/>
                        </div>
                    </div>
                    <div className="form-group">
                        {categoryCheckboxes}
                    </div>

                    <div className="form-row">
                        {this.state.difficultyArray.map((difficulty, id) =>
                            <div className="form-check form-check-inline" key={id}>
                                <input className="form-check-input" type="radio" name="difficulty"
                                       id={'difficulty' + id}
                                       value={difficulty} onChange={this.updateState}
                                        checked={this.state.recipe.difficulty === difficulty}
                                />
                                <label className="form-check-label">{difficulty}</label>
                            </div>
                        )}

                    </div>


                    {this.state.recipe.ingredients.length > 0 ? this.state.recipe.ingredients.map((ingredient, idx) => (
                        <div className="form-row" key={idx}>
                            <div className="form-group col-md-3">
                                <input type="number" name="amount" placeholder="Amount" value={ingredient.amount}
                                       onChange={this.handleChangeIngredient(idx)}/>
                            </div>
                            <div className="form-group col-md-3">
                                {this.props.update ? <select name="uom.description" onChange={this.handleChangeIngredientUom(idx)} value={ingredient.uom ?
                                    ingredient.uom.id : 0}>
                                    {selectUoms}
                                </select> :
                                    <select name="uom.description" onChange={this.handleChangeIngredientUom(idx)}>
                                        {selectUoms}
                                    </select>
                                }


                            </div>
                            <div className="form-group col-md-3">
                                <input type="text" name="description" placeholder="Ingredient description"
                                       value={ingredient.description}
                                       onChange={this.handleChangeIngredient(idx)}/>
                            </div>
                        </div>
                    )) : []}

                    <div className="form-group col-md-3">
                        <button type="button" onClick={this.handleAddIngredient} className={'btn btn-primary'}>Add
                            ingredient
                        </button>
                    </div>

                    <div className={'form-group'}>
                        <label className='label'>Directions</label>
                        <textarea value={this.state.recipe.directions} className='form-control' name="directions"
                                  onChange={this.updateState}></textarea>
                    </div>

                    <div className={'form-group'}>
                        <label>Recipe notes</label>
                        <textarea value={this.state.recipe.notes.recipeNotes} className='form-control'
                                  name="recipeNotes" onChange={this.updateStateNotes}></textarea>
                    </div>

                    <button type='submit' className='btn btn-success' disabled={!this.state.formValid}>Submit</button>

                </form>
                {fireRedirect && (
                    <Redirect to={'/recipe/' + id + '/show'}/>
                )}
            </div>
        )
    }
}