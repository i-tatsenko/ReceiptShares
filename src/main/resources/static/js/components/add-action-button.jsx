import FloatingActionButton from "material-ui/FloatingActionButton";
import ContentAdd from "material-ui/svg-icons/content/add";
import IconMenu from "material-ui/IconMenu";
import storage from "../storage/storage.js"

export default class ActionButton extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            menuItems: storage.getState().actionButtonMenuItems
        };
        storage.listenFor("add-action-menu", () => this.setState({menuItems: storage.getState().actionButtonMenuItems}));
    }

    render() {
        return <IconMenu
            style={{
                position: 'absolute',
                right: '30px',
                bottom: '20px'
            }}
            iconButtonElement={
                <FloatingActionButton zDepth={3}>
                    <ContentAdd/>
                </FloatingActionButton>
            }
            anchorOrigin={{horizontal: 'left', vertical: 'top'}}
            targetOrigin={{horizontal: 'middle', vertical: 'bottom'}}>
            {this.state.menuItems}
        </IconMenu>;
    }

}