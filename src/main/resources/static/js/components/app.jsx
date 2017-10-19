import storage from "../storage/storage.js"
import LeftMenu from "./left-menu.jsx";

import AppBar from "material-ui/AppBar";
import Logout from "./login/logout.jsx";
import {withRouter} from "react-router-dom";
import ActionButton from "./add-action-button.jsx"
import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import IconButton from 'material-ui/IconButton';
import MenuIcon from 'material-ui-icons/Menu';

class App extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            menuOpen: false,
            barTitle: storage.getState().screenTitle,
            menuItems: {
                Receipts: '/',
                Help: '/help',
                logout: <Logout/>
            }
        };
        storage.listenFor("screenTitle", () => this.setState({barTitle: storage.getState().screenTitle}))
    }

    render() {
        return (
            <section>
                <div className="clearflix" style={{position: "relative"}}>
                    <AppBar>
                        <Toolbar>
                            <IconButton color="contrast" onClick={() => this.setState({menuOpen: !this.state.menuOpen})}>
                                <MenuIcon/>
                            </IconButton>
                            <Typography type="title" color="inherit">
                                {this.state.barTitle}
                            </Typography>
                        </Toolbar>
                    </AppBar>
                    <LeftMenu open={this.state.menuOpen} links={this.state.menuItems}
                              closeMenu={() => this.setState({menuOpen: false})}/>
                    <div style={{
                        marginLeft: '20px',
                        marginTop: '20px',
                        marginRight: '20px',
                        marginBottom: '10px'
                    }} className="container-div">
                        {this.props.children}
                    </div>
                </div>
                <ActionButton/>
            </section>
        )
    }
}

export default withRouter(App)