import React from 'react';

var ReactRouter = require('react-router');
require('style!css!../../css/components/header.css');

export default class Header extends React.Component {
    render() {
        console.log(this.props.user);
        let avatarLink = this.props.user.avatar ? "data:image;base64," + this.props.user.avatar : "/no-photo-avatar.svg";
        avatarLink = "url(" + avatarLink + ")";
        return (
            <div className='header clearfix'>
                <h1>Receipt Shares</h1>
                <div className="header__user-profile clearfix">
                    <div className="header__user-avatar" style={{
                        backgroundImage: avatarLink
                    }}>
                    </div>
                    <div className="header__user-name">
                        {this.props.user.name}
                    </div>
                </div>
            </div>
        );
    }
}

