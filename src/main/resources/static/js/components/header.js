var Header = React.createClass({
    render: function () {
        return (
            <div className='header'>
                <div className='header-user_action'>
                    <button className='btn btn-default header-user_action__login' onClick={this.props.login}>Login</button>
                    <button className='btn btn-default header-user_action__register' onClick={this.props.register}>Register</button>
                </div>
            </div>
        )
    }
});

