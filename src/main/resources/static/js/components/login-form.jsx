import React from 'react'
import FormAlert from './form-alert.jsx';
import FbLoginButton from './facebook-login-button.jsx';
var ReactRouter = require('react-router');

export default class LoginForm extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            alertMessage: ''
        }
    }

    render() {
        return (
            <div className="panel panel-primary">
                <div className="panel-heading">Login</div>
                <div className="panel-body">
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
                    <div>
                        <h2>Login with your social network account</h2>
                        <FbLoginButton/>
                    </div>
                </div>
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
                ReactRouter.hashHistory.push("/current");
            }
        }).fail(() => component.setState({alertMessage: "Wrong credentials. Please try again later"}))
    }
}