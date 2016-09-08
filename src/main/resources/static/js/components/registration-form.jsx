import React from 'react'
require('style!css!../../css/components/reg-form.css');

export default class RegistrationForm extends React.Component {
    render() {
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
                    <div className="g-recaptcha" data-sitekey="6LcZlikTAAAAABq_omyFFcB9Z3qwZsYIRO2iVwxO"
                         data-callback={captchaOk.bind(this)}>

                    </div>
                    <div>
                        <input className='form-control' type='button' value='Register' onClick={this.registerUser}/>
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

}

function captchaOk(resp) {
    alert('Captch Ok ' + resp)
}