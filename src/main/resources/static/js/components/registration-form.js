var RegistrationForm = React.createClass({
    render: function () {
        return (
            <div id='register-form'>
                <div>
                    <label>Name <input id="newUserName" className='form-control' type='text' placeholder='Your name' name='name'/> </label>
                </div>
                <div>
                    <label>Email <input id="newUserEmail" className='form-control' type='email' placeholder='Email' name='email'/></label>
                </div>
                <div>
                    <label>Password <input id="newUserPassword" className='form-control' type='password' name='password'/> </label>
                </div>
                <div>
                    <label>Password one more time <input id="newUserPasswordCheck" className='form-control' type='password'
                                                         name='passwordCheck'/></label>
                </div>
                <div>
                    <input className='form-control' type='button' value='Register' onClick={this.registerUser}/>
                </div>
            </div>
        );
    },

    registerUser: function () {
        console.log("Name: " + $('#newUserName').val() + " Email: " + $('#newUserEmail').val());
    }
});