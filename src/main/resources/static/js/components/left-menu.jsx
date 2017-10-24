import React from "react";
import List, {ListItem, ListItemText} from 'material-ui/List';
import Drawer from "material-ui/Drawer";
import {withRouter} from "react-router-dom";

class LeftMenu extends React.Component {
    render() {
        let items = [];
        let links = this.props.links;
        for (let linkHeader in links) {
            if (links.hasOwnProperty(linkHeader)) {
                items.push(this.createMenuItem(linkHeader, links[linkHeader]))
            }
        }
        return (
            <Drawer open={this.props.open} onRequestClose={this.props.closeMenu}>
                <div
                    tabIndex={0}
                    role="button"
                    onClick={this.props.closeMenu}
                    onKeyDown={this.props.closeMenu}
                >
                    <div style={{
                        width: 250
                    }}>

                        <List>
                            {items}
                        </List>
                    </div>
                </div>
            </Drawer>
        )
    }

    createMenuItem(linkHeader, link) {
        if ((typeof link) === 'string') {
            return this.createForLink(linkHeader, link);
        } else if ((typeof link === 'function')) {
            return LeftMenu.createForFunction(linkHeader, link)
        }
        throw new Error(`Can't create menu item for ${link}`)
    }

    createForLink(linkHeader, link) {
        return (<ListItem button onClick={() => this.props.history.push(link)} key={link}>
            <ListItemText primary={linkHeader}/>
        </ListItem>)
    }

    static createForFunction(linkHeader, func) {
        return (<ListItem button onClick={func} key={linkHeader}>
            <ListItemText primary={linkHeader}/>
        </ListItem>)
    }
}

export default withRouter(LeftMenu)
