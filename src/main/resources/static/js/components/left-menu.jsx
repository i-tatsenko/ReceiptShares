import React from 'react'
import NavLink from './nav-link.jsx';

// require('style!css!../../css/components/left-menu.css');


export default class LeftMenu extends React.Component {
    render() {
        return (
            <div id="left-menu">
                <ul className="nav nav-stacked nav-pills">
                    <NavLink activeClassName='active' to="/">Main</NavLink>
                    <NavLink activeClassName='active' to={this.props.currentLink}>Current Receipt</NavLink>
                    <NavLink activeClassName='active' to={this.props.helpLink}>Help</NavLink>
                </ul>
            </div>
        )
    }
}
