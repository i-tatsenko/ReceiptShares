import React from 'react'
require('style!css!../../css/components/reg-form.css');

export default class RegistrationForm extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            formOk: false
        }
    }

    render() {
        var disabled = !this.state.formOk;
        return (
            <div id="reg-form-container">
                <h2>Register</h2>
                <div id="reg-form-alert" className="alert alert-danger" hidden="true"/>
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
                                               name='password'/> </label>
                        <span className="help-block"/>
                    </div>
                    <div className="form-group">
                        <label>Password one more time <input id="newUserPasswordCheck" className='form-control'
                                                             type='password'
                                                             name='passwordCheck'/></label>
                        <span className="help-block"/>
                    </div>
                    <div id="captcha"/>

                    <div>
                        <input id="registration-form__register-button" className='form-control' type='button'
                               value='Register' onClick={this.registerUser} disabled={disabled}/>
                    </div>
                </form>
            </div>
        );
    }

    registerUser() {
        var form = $('#register-form').serialize();
        $.post({
            url: '/v1/reg',
            data: form,
            dataType: 'html',
            success: function () {
                alert('You have been registered')
            }
        }).fail(function (error) {
            if (error.status == 409) {
                var divContainingEmail = $('#newUserEmail').parents('.form-group');
                divContainingEmail.addClass('has-error');
                divContainingEmail.find('.help-block').text("Please choose another email.")
            } else {
                $('#reg-form-alert').show().text('Something went wrong. Please reload page and try again')
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