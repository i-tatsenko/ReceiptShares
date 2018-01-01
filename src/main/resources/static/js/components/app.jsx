import storage from "../storage/storage.js"
import {withStyles} from 'material-ui/styles';
import AppBar from "material-ui/AppBar";
import {withRouter} from "react-router-dom";
import ActionButton from "./add-action-button.jsx"
import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import Description from 'material-ui-icons/Description';
import Settings from 'material-ui-icons/Settings';
import BottomNavigation, { BottomNavigationButton } from 'material-ui/BottomNavigation';

const styles = theme => ({
    container: {
        marginLeft: 10,
        marginTop: theme.spacing.unit * 8,
        marginRight: 10,
        marginBottom: theme.spacing.unit * 7 + 5,
        paddingTop: theme.spacing.unit
    },

    navigationBar: {
        position: 'fixed',
        bottom: 0,
        width: '100%',
        backgroundColor: '#E3F2FD'
    }
});

const navigationNodes = {
    "receipts": "/",
    "settings": "/settings"
};

class App extends React.Component {

    constructor(args) {
        super(args);
        this.classes = args.classes;
        this.state = {
            barTitle: storage.getState().screenTitle,
            navigationLocation: "receipts"
        };
        storage.listenFor("screenTitle", () => this.setState({barTitle: storage.getState().screenTitle}))
    }

    render() {
        return (
            <section>
                <div className="clearflix" style={{position: "relative"}}>
                    <AppBar>
                        <Toolbar>
                            <Typography type="title" color="inherit">
                                {this.state.barTitle}
                            </Typography>
                        </Toolbar>
                    </AppBar>
                    <div className={this.classes.container}>
                        {this.props.children}
                    </div>
                </div>
                <BottomNavigation onChange={(e, v) => this.navigationChange(v)}
                                  showLabels
                                  className={this.classes.navigationBar}>
                    <BottomNavigationButton label="Receipts" value="receipts" icon={<Description/>}/>
                    <BottomNavigationButton label="Settings" value="settings" icon={<Settings/>}/>
                </BottomNavigation>
                <ActionButton/>
            </section>
        )
    }

    navigationChange(location) {
        if (location !== this.state.navigationLocation) {
            this.setState({navigationLocation: location});
            this.props.history.push(navigationNodes[location]);
        }
    }
}

export default withRouter(withStyles(styles)(App))