import React from 'react'
import Menu from 'material-ui/Menu';
import MenuItem from 'material-ui/MenuItem';
import Drawer from 'material-ui/Drawer';
import {Link, IndexLink} from 'react-router'
import AppBar from 'material-ui/AppBar';


export default class LeftMenu extends React.Component {
    render() {
        return (
            <Drawer open={this.props.open}>
                <AppBar onLeftIconButtonTouchTap={this.props.closeMenu} />
                <Menu>
                    <MenuItem><IndexLink activeClassName='active' to="/">Main</IndexLink></MenuItem>
                    <MenuItem><Link activeClassName='active' to={this.props.currentLink}>Current
                        Receipt</Link></MenuItem>
                    <MenuItem><Link activeClassName='active' to={this.props.helpLink}>Help</Link></MenuItem>
                </Menu>
            </Drawer>
        )
    }
}
