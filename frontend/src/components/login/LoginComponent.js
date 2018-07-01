import React, {Component} from 'react';
import axios from 'axios';
import {config} from "../config";
import interceptor from "../../interceptor";
import './LoginComponent.css';


export default class LoginComponent extends Component {

    constructor(props) {
        super(props);
    }

    login = (event) => {
        event.preventDefault();
        const form = new FormData(event.target);
        form.set("grant_type", 'password');
        const client = config.CLIENT_ID;
        const secret = config.CLIENT_SECRET;
        return axios.post(config.URL + '/oauth/token', form,
            {
                method: 'POST',
                headers: {
                    'Content-type': 'application/x-www-form-urlencoded;charset=UTF-8',
                    'Authorization': 'Basic ' + btoa(client + ':' + secret)
                }
            }
        ).then(res => {
            localStorage.setItem('access_token', res.data['access_token']);
            localStorage.setItem('refresh_token', res.data['refresh_token']);
            interceptor.requestIntercept();
            interceptor.responseIntercept();
            axios.get(config.URL + '/users/principal')
                .then(user => {
                    const username = user.data;
                    this.props.setParentUsername(username);
                });
        })
            .catch(error => {
                console.log(error)
            })
    };


    render() {
        return (
            <div className={'container login-form'}>
                <form onSubmit={this.login} >
                    <div className={'form-group'}>
                        <label>Username</label>
                        <input name="username" className={'form-control'}/>
                    </div>
                    <div className={'form-group'}>
                        <lebel>Password</lebel>
                        <input type="password" name="password" className={'form-control'}/></div>

                    <button type="submit" className={'btn btn-success'}>Send</button>
                </form>
            </div>
        )
    }
}