import React, {Component} from 'react';
import axios from 'axios';
import {Redirect} from 'react-router-dom';
import {FormErrors} from './FormErrors';

export default class RecipeForm extends Component {

    constructor(props) {
        super(props);
        this.state = {
            description: '',
            submitted: false,
            fireRedirect: false,
            formErrors: {description: ''},
            descriptionValid: false,
            formValid: false
        }
    }

    validateField(fieldName, value) {
        let fieldValidationErrors = this.state.formErrors;
        let descValid = this.state.descriptionValid;

        switch(fieldName) {
            case 'description':
                descValid =  value.length >= 6;
                fieldValidationErrors.description = descValid ? '' : 'is invalid';
                break;
            default:
                break;
        }
        this.setState({formErrors: fieldValidationErrors,
            descriptionValid: descValid,
        }, this.validateForm);
    }

    validateForm() {
        this.setState({formValid: this.state.descriptionValid });
    }


    handleSubmit = (e) => {
        e.preventDefault();
        this.setState({ submitted: true});


        axios.post("http://localhost:8080/api/recipe/new", {
            description: this.state.description
        }).then(res => {
            console.log(res);
            this.setState({ fireRedirect: true, id: res.data.id});

        })
            .catch(err => console.error(err));
    };

    updateState = (event) => {
        const name = event.target.name;
        const value = event.target.value;
        this.setState({[name]: value}, () => { this.validateField(name, value) });
    };

    render() {
        const { id, fireRedirect} = this.state;
        return (
            <div>
                <div className="panel panel-default">
                    <FormErrors formErrors={this.state.formErrors}/>
                </div>
                <form onSubmit={this.handleSubmit}>
                <input type='text' name='description' onChange={this.updateState}/>
                <button type='submit'>Submit</button>

            </form>
                {fireRedirect && (
                    <Redirect to={'/recipe/' + id + '/show'}/>
                )}
            </div>
        )
    }
}