import React from 'react'
import FormAlert from './form-alert.jsx';
var ReactRouter = require('react-router');
require('style!css!../../css/components/reg-form.css');

export default class RegistrationForm extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            formOk: false,
            alertMessage : ''
        }
    }

    render() {
        var disabled = !this.state.formOk;
        return (
            <div id="reg-form-container">
                <h2>Register</h2>
                <FormAlert message={this.state.alertMessage}/>
                <form id='register-form'>
                    <div className="form-group">
                        <label>Name <input id="newUserName" className='form-control' type='text' placeholder='Your name'
                                           name='name'/></label>
                        <span className="help-block"/>
                    </div>
                    <div className="form-group">
                        <label>Email <input id="newUserEmail" className='form-control' type='email' placeholder='Email'
                                            name='email'/></label>
                        <span className="help-block"/>
                    </div>
                    <div className="form-group">
                        <label>Password <input id="newUserPassword" className='form-control' type='password'
                                               name='password' placeholder="Password"/> </label>
                        <span className="help-block"/>
                    </div>
                    <div className="form-group">
                        <label>Password one more time <input id="newUserPasswordCheck" className='form-control'
                                                             type='password'
                                                             name='passwordCheck' placeholder="Password check"/></label>
                        <span className="help-block"/>
                    </div>
                    <div id="captcha"/>

                    <div>
                        <input id="registration-form__register-button" className='form-control' type='button'
                               value='Register' onClick={() => this.registerUser(this)}  disabled={disabled}/>
                    </div>
                </form>
            </div>
        );
    }

    registerUser(component) {
        var form = $('#register-form').serialize();
        $.post({
            url: '/v1/open/reg',
            data: form,
            dataType: 'html',
            success: () => ReactRouter.hashHistory.push("/current")
        }).fail(function (error) {
            if (error.status == 409) {
                var divContainingEmail = $('#newUserEmail').parents('.form-group');
                divContainingEmail.addClass('has-error');
                divContainingEmail.find('.help-block').text("Please choose another email.")
            } else {
                component.setState({alertMessage: 'Something went wrong. Please reload page and try again'})
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