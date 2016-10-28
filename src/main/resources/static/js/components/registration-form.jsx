import React from 'react';
import {ReactRouter, Link, hashHistory} from 'react-router';

import FormAlert from './form-alert.jsx';
import AppBar from 'material-ui/AppBar';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';

require('style!css!../../css/components/reg-form.css');

export default class RegistrationForm extends React.Component {

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
                <AppBar title="Register" iconStyleLeft={{display: 'none'}}/>
                <div id="reg-form-container">

                    <FormAlert message={this.state.alertMessage}/>
                    <form id='register-form'>

                        <TextField hintText="Name" floatingLabelText="Your name" name="name"/><br/>
                        <TextField hintText="Email" floatingLabelText="Your email" name="email" type="email"/><br/>
                        <TextField hintText="Password" floatingLabelText="Your password" name="password"
                                   type="password"/><br/>
                        <TextField hintText="Password one more time" floatingLabelText="Your password one more time"
                                   name="passwordCheck" type="password"/><br/>
                        <div id="captcha"/>

                        <RaisedButton label="Register" primary={true} fullWidth={true} className="reg-form__register-button"
                                      onClick={() => this.registerUser()} disabled={disabled}/>

                    </form>
                    <hr/>
                    <span className="reg-form__invite-to-login">Have an account? <Link to="/login">Login</Link> here!</span>
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
            success: () => hashHistory.push("/login")
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