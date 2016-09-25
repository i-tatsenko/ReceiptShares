import React from 'react';

var ReactRouter = require('react-router');
require('style!css!../../css/components/header.css');

export default class Header extends React.Component {
    render() {
        console.log(this.props.user);
        return (
            <div className='header clearfix'>
                <h1>Receipt Shares</h1>
                <div className="header__user-profile clearfix">
                    <div className="header__user-avatar" style={{
                        "background-image": "url(/no-photo-avatar.svg)"
                    }}>
                    </div>
                    <div className="header__user-name">
                        {this.props.user.name}
                    </div>
                </div>
            </div>
        )
    }
}

