import React from 'react'
import Menu from 'material-ui/Menu';
import MenuItem from 'material-ui/MenuItem';
import Drawer from 'material-ui/Drawer';
import {Link, IndexLink} from 'react-router'
import AppBar from 'material-ui/AppBar';
import Logout from './login/logout.jsx'

export default class LeftMenu extends React.Component {
    render() {
        return (
            <Drawer open={this.props.open}>
                <AppBar onLeftIconButtonTouchTap={this.props.closeMenu}/>
                <Menu>
                    <IndexLink to="/" onClick={this.props.closeMenu}>
                        <MenuItem>Main</MenuItem>
                    </IndexLink>
                    <Link to={this.props.currentLink} onClick={this.props.closeMenu}>
                        <MenuItem>Current Receipt</MenuItem>
                    </Link>
                    <Link to={this.props.helpLink} onClick={this.props.closeMenu}>
                        <MenuItem>Help</MenuItem>
                    </Link>
                    <MenuItem><Logout/></MenuItem>
                </Menu>
            </Drawer>
        )
    }
}
