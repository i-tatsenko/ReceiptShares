import React from 'react'

require('style!css!../../css/components/left-menu.css');

var ReactRouter = require('react-router');

var Link = ReactRouter.Link;
var IndexLink = ReactRouter.IndexLink;

export default class LeftMenu extends React.Component {
    render() {
        return (
            <div id="left-menu">
                <ul>
                    <li><IndexLink activeClassName='left-menu__active-li' to="/">Main</IndexLink></li>
                    <li><Link  activeClassName='left-menu__active-li' to={this.props.currentLink}>Current Receipt</Link></li>
                    <li><Link activeClassName='left-menu__active-li' to={this.props.helpLink}>Help</Link></li>
                </ul>
            </div>
        )
    }
}
