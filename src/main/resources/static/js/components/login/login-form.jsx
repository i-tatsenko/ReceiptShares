import React from 'react'
import FormAlert from '../form-alert.jsx';
import FbLoginButton from './facebook-login-button.jsx';
import AppBar from 'material-ui/AppBar';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
import './login.css';

var ReactRouter = require('react-router');
var Link = ReactRouter.Link;

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
                <AppBar title="Login" iconStyleLeft={{display: 'none'}}/>
                    <div id="login">
                        <FormAlert message={this.state.alertMessage}/>
                        <form id="login-form">
                            <TextField
                                hintText="Email"
                                floatingLabelText="Your login"
                                name="username"
                            /><br />
                            <TextField
                                hintText="Password"
                                floatingLabelText="Your password"
                                name="password" type="password"
                            /><br />
                            <RaisedButton label="Login" primary={true} className="login__login-button"
                                          onClick={(event) => this.login(event)}/>
                            <hr/>
                            <div>
                                <h3>Login with your social network account</h3>
                                <FbLoginButton/>
                            </div>
                        </form>
                        <hr/>
                        <span className="login__invite-to-register"><Link to="/register">Register</Link> if you have no account!</span>
                    </div>
            </div>
        )
    }

    login(event) {
        let that = this;
        event.preventDefault();
        var loginData = $('#login-form').serialize();
        $.post({
            url: '/v1/open/login',
            data: loginData,
            success() {
                $.get({
                    url: '/v1/me',
                    success: resp => that.props.loginCallback(resp)
                }).fail(() => that.loginFailed(that));
            }
        }).fail(() => that.loginFailed())
    }

    loginFailed() {
        this.setState({alertMessage: "Wrong credentials. Please try again later"})
    }
}