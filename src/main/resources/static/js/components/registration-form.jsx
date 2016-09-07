import React from 'react'

export default class RegistrationForm extends React.Component {
    render() {
        return (
            <form id='register-form'>
                <div>
                    <label>Name <input id="newUserName" className='form-control' type='text' placeholder='Your name'
                                       name='name'/></label>
                </div>
                <div>
                    <label>Email <input id="newUserEmail" className='form-control' type='email' placeholder='Email'
                                        name='email'/></label>
                </div>
                <div>
                    <label>Password <input id="newUserPassword" className='form-control' type='password'
                                           name='password'/> </label>
                </div>
                <div>
                    <label>Password one more time <input id="newUserPasswordCheck" className='form-control'
                                                         type='password'
                                                         name='passwordCheck'/></label>
                </div>
                <div className="g-recaptcha" data-sitekey="6LcZlikTAAAAABq_omyFFcB9Z3qwZsYIRO2iVwxO"
                     data-callback={captchaOk.bind(this)}></div>
                <div>
                    <input className='form-control' type='button' value='Register' onClick={this.registerUser}/>
                </div>
            </form>
        );
    }

    registerUser() {
        var form = $('#register-form').serialize();
        $.post({
            url: '/reg',
            data: form,
            dataType: 'html',
            success: function () {
                alert('You have been registered')
            }
        }).fail(function (error) {
            alert('Can\'t register' + JSON.stringify(error));
        })
    }

}

function captchaOk(resp) {
    alert('Captch Ok ' + resp)
}