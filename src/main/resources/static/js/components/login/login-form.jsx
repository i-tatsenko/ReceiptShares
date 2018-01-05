import React from 'react'
import FormAlert from '../alert/form-alert.jsx';
import FbLoginButton from './facebook-login-button';
import AppBar from 'material-ui/AppBar';
import TextField from 'material-ui/TextField';
import Button from 'material-ui/Button';
import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import {Link} from 'react-router-dom'

import './login.css';


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
                <AppBar>
                    <Toolbar>
                        <Typography type="title" color="inherit">
                            Login
                        </Typography>
                    </Toolbar>
                </AppBar>
                <div id="login">
                    <FormAlert message={this.state.alertMessage}/>
                    <form id="login-form">
                        <TextField
                            inputRef={loginInput => this.loginInput = loginInput}
                            label="Email"
                            placeholder="Your login"
                            name="username"
                            onKeyPress={e => e.key === 'Enter' ? this.login(e) : ''}
                        /><br/>
                        <TextField
                            inputRef={passwordInput => this.passwordInput = passwordInput}
                            label="Password"
                            placeholder="Your password"
                            name="password" type="password"
                            onKeyPress={e => e.key === 'Enter' ? this.login(e) : ''}
                        /><br/>
                        <Button raised color="primary" className="login__login-button"
                                onClick={(event) => this.login(event)}>Login</Button>
                    </form>
                    <hr/>
                    <div>
                        <h3>Login with your social network account</h3>
                        <FbLoginButton/>
                    </div>
                    <hr/>
                    <span className="login__invite-to-register"><Link to="/register">Register</Link> if you have no account!</span>
                </div>
            </div>
        );
    }

    login(event) {
        let that = this;
        event.preventDefault();
        let loginData = {
            username: this.loginInput.value.toLowerCase(),
            password: this.passwordInput.value
        };
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