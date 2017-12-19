import storage from "../storage/storage.js"
import LeftMenu from "./left-menu.jsx";
import {withStyles} from 'material-ui/styles';
import AppBar from "material-ui/AppBar";
import {withRouter} from "react-router-dom";
import ActionButton from "./add-action-button.jsx"
import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import IconButton from 'material-ui/IconButton';
import MenuIcon from 'material-ui-icons/Menu';

const styles = theme => ({
    container: {
        marginLeft: '20px',
        marginTop: theme.spacing.unit * 9,
        marginRight: '20px',
        marginBottom: '10px',
        paddingTop: theme.spacing.unit
    }
});

class App extends React.Component {

    constructor(args) {
        super(args);
        this.classes = args.classes;
        this.state = {
            menuOpen: false,
            barTitle: storage.getState().screenTitle,
            menuItems: {
                Receipts: '/',
                Help: '/help',
                Logout: () => $.post('/v1/open/logout').done(() => window.location = '/login')
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
                            <IconButton color="contrast"
                                        onClick={() => this.setState({menuOpen: !this.state.menuOpen})}>
                                <MenuIcon/>
                            </IconButton>
                            <Typography type="title" color="inherit">
                                {this.state.barTitle}
                            </Typography>
                        </Toolbar>
                    </AppBar>
                    <LeftMenu open={this.state.menuOpen} links={this.state.menuItems}
                              closeMenu={() => this.setState({menuOpen: false})}/>
                    <div className={this.classes.container}>
                        {this.props.children}
                    </div>
                </div>
                <ActionButton/>
            </section>
        )
    }
}

export default withRouter(withStyles(styles)(App))