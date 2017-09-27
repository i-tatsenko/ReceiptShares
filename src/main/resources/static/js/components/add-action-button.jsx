import FloatingActionButton from "material-ui/FloatingActionButton";
import ContentAdd from "material-ui/svg-icons/content/add";
import IconMenu from "material-ui/IconMenu";
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
        return <IconMenu
            style={{
                position: 'fixed',
                right: '30px',
                bottom: '20px'
            }}
            iconButtonElement={
                <FloatingActionButton zDepth={5}>
                    <ContentAdd/>
                </FloatingActionButton>
            }
            anchorOrigin={{horizontal: 'left', vertical: 'top'}}
            targetOrigin={{horizontal: 'middle', vertical: 'bottom'}}
            useLayerForClickAway={true}
            open={this.state.open}
            onItemTouchTap={() => this.setState({menuOpen:false})}
            touchTapCloseDelay={1}>
            {this.state.menuItems.map(elem => React.cloneElement(elem, {key: "menu-item-" + menuItemCounter++}))}
        </IconMenu>;
    }

}