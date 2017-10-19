import React from "react";
import Menu from "material-ui/Menu";
import MenuItem  from 'material-ui/Menu';
import Drawer from "material-ui/Drawer";
import AppBar from "material-ui/AppBar";
import {withRouter} from "react-router-dom";

class LeftMenu extends React.Component {
    render() {
        let items = [];
        var links = this.props.links;
        for (let linkHeader in links) {
            if (links.hasOwnProperty(linkHeader)) {
                items.push(this.createMenuItem(linkHeader, links[linkHeader]))
            }
        }
        return (
            <Drawer {...this.props} >
                <AppBar onLeftIconButtonTouchTap={this.props.closeMenu}/>
                <Menu>
                    {items}
                </Menu>
            </Drawer>
        )
    }

    createMenuItem(linkHeader, link) {
        if ((typeof link) === 'string') {
            return this.createForLink(linkHeader, link);
        }
        return (
            this.createForObject(link)
        );
    }

    createForLink(linkHeader, link) {
        return (<MenuItem onClick={this.props.closeMenu} onTouchTap={() => this.props.history.push(link)} key={link}>{linkHeader}</MenuItem>)
    }

    createForObject(object) {
        return (<MenuItem onClick={this.props.closeMenu} key={object}>{object}</MenuItem>)
    }
}

export default withRouter(LeftMenu)
