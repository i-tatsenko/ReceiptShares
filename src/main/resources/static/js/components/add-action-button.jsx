import React from "react"
import AddIcon from 'material-ui-icons/Add';
import Button from 'material-ui/Button';
import Menu from 'material-ui/Menu'
import storage from "../storage/storage.js"

export default class ActionButton extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            menuItems: storage.getState().actionButtonMenuItems,
            menuOpen: false
        };
        storage.listenFor("add-action-menu", () => this.setState({menuItems: storage.getState().actionButtonMenuItems}));
    }

    render() {
        let menuItemCounter = 0;
        return (
            <div>
                <Button fab color="primary" style={{
                    position: 'fixed',
                    right: '30px',
                    bottom: '50px'
                }}
                        onClick={event => this.setState({menuOpen: !this.state.menuOpen, floatMenuButton: event.currentTarget})}
                >
                    <AddIcon/>
                </Button>

                <Menu
                    anchorEl={this.state.floatMenuButton}
                    open={this.state.menuOpen}
                    onRequestClose={() => this.setState({menuOpen: false})}
                    //TODO: check if this working
                    // onItemTouchTap={() => this.setState({menuOpen: false})}
                >
                    {this.state.menuItems.map(elem => React.cloneElement(elem, {key: "menu-item-" + menuItemCounter++}))}
                </Menu>
            </div>
        )
    }

}