import React from 'react';

var ReaccRouter = require('react-router');
require('style!css!../../css/components/header.css');
var Link = ReaccRouter.Link;

export default class Header extends React.Component {
    render() {
        return (
            <div className='header'>
                <div className='header-user_action'>
                    <div className='btn btn-default header-user_action__login'>
                        <Link to={this.props.login}>Login</Link>
                    </div>
                    <div className='btn btn-default header-user_action__register'>
                        <Link to={this.props.register}>Register</Link>
                    </div>
                </div>
            </div>
        )
    }
}

