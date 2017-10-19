import React from 'react';
import {Link, withRouter} from 'react-router-dom';

import FormAlert from '../form-alert.jsx';
import AppBar from 'material-ui/AppBar';
import TextField from 'material-ui/TextField';
import Button from 'material-ui/Button';

import './reg-form.css';
import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';

class RegistrationForm extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            formOk: false,
            alertMessage: ''
        }
    }

    render() {
        var disabled = !this.state.formOk;
        return (
            <div>
                <AppBar>
                    <Toolbar>
                        <Typography type="title" color="inherit">
                            Register
                        </Typography>
                    </Toolbar>
                </AppBar>
                <div id="reg-form-container">

                    <FormAlert message={this.state.alertMessage}/>
                    <form id='register-form'>

                        <TextField label="Name" placeholder="Your name" name="name"/><br/>
                        <TextField label="Email" placeholder="Your email" name="email" type="email"/><br/>
                        <TextField label="Password" placeholder="Your password" name="password"
                                   type="password"/><br/>
                        <TextField label="Password one more time" placeholder="Your password one more time"
                                   name="passwordCheck" type="password"/><br/>
                        <div id="captcha"/>

                        <Button raised color="primary" fullWidth={true}
                                className="reg-form__register-button"
                                onClick={() => this.registerUser()} disabled={disabled}>
                            Register
                        </Button>

                    </form>
                    <hr/>
                    <span className="reg-form__invite-to-login">Have an account? <Link
                        to="/login">Login</Link> here!</span>
                </div>
            </div>

        );
    }

    registerUser() {
        let that = this;
        var form = $('#register-form').serialize();
        $.post({
            url: '/v1/open/reg',
            data: form,
            dataType: 'html',
            success: () => this.props.history.push("/login")
        }).fail(function (error) {
            if (error.status == 409) {
                var divContainingEmail = $('#newUserEmail').parents('.form-group');
                divContainingEmail.addClass('has-error');
                divContainingEmail.find('.help-block').text("Please choose another email.")
            } else {
                that.setState({alertMessage: 'Something went wrong. Please reload page and try again'})
            }
        })
    }

    componentDidMount() {
        if (typeof grecaptcha === 'undefined') {
            setTimeout(() => this.renderCaptcha(), 100)
        } else {
            this.renderCaptcha();
        }
    }

    renderCaptcha() {
        grecaptcha.render('captcha', {
            sitekey: '6LcZlikTAAAAABq_omyFFcB9Z3qwZsYIRO2iVwxO',
            callback: () => this.setState({formOk: true})
        });
    }

}

export default withRouter(RegistrationForm)