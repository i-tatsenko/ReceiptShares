import React from 'react'
import FormAlert from './form-alert.jsx';

export default class LoginForm extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            alertMessage: ''
        }
    }

    render() {
        return (
            <div>
                <form id="login-form">
                    <FormAlert message={this.state.alertMessage}/>
                    <div className="form-group"><label>
                        <input className="form-control" type="text" name="username" placeholder="Email"/>
                    </label></div>
                    <div className="form-group"><label>
                        <input className="form-control" type="text" name="password" placeholder="Password"/>
                    </label></div>
                    <button className="btn btn-default" onClick={(event) => this.login(event, this)}>Login</button>
                </form>
            </div>
        )
    }

    login(event, component) {
        event.preventDefault();
        var loginData = $('#login-form').serialize();
        $.post({
            url: '/v1/open/login',
            data: loginData,
            success() {
                window.reactHistory.push("/");
            }
        }).fail(() => component.setState({alertMessage: "Wrong credentials. Please try again later"}))
    }
}