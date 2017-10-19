import Button from 'material-ui/Button';
import AddIcon from 'material-ui-icons/Add';
import IconButton from 'material-ui/IconButton';
import Menu from 'material-ui/Menu'

'material-ui/Menu';
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
                <IconButton style={{
                    position: 'fixed',
                    right: '30px',
                    bottom: '20px'
                }} ref={button => this.setState({floatMenuButton: button})}>
                    <AddIcon/>
                </IconButton>

                <Menu
                    anchorEl={this.state.floatMenuButton}
                    open={this.state.open}
                    onItemTouchTap={() => this.setState({menuOpen: false})}
                    touchTapCloseDelay={1}>
                    {this.state.menuItems.map(elem => React.cloneElement(elem, {key: "menu-item-" + menuItemCounter++}))}
                </Menu>
            </div>
        )
    }

}