import storage from "../storage/storage.js"
import {withStyles} from 'material-ui/styles';
import AppBar from "material-ui/AppBar";
import {withRouter} from "react-router-dom";
import ActionButton from "./add-action-button.jsx"
import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import Navigation from "./navigation.jsx";
import Grid from 'material-ui/Grid';

const styles = theme => ({
    container: {
        marginLeft: 10,
        marginTop: theme.spacing.unit * 8,
        marginRight: 10,
        marginBottom: theme.spacing.unit * 7 + 5,
        paddingTop: theme.spacing.unit,
        // width: '100%',
        // display: 'flex',
        // flexDirection: 'column'
    },

    app: {
        display: 'flex',
        flexDirection: 'column'
    }
});

class App extends React.Component {

    constructor(args) {
        super(args);
        this.classes = args.classes;
        this.state = {
            barTitle: storage.getState().screenTitle,
        };
        storage.listenFor("screenTitle", () => this.setState({barTitle: storage.getState().screenTitle}))
    }

    render() {
        return (
            <section className={this.classes.app}>
                <AppBar>
                    <Toolbar>
                        <Typography type="title" color="inherit">
                            {this.state.barTitle}
                        </Typography>
                    </Toolbar>
                </AppBar>
                <Grid className={this.classes.container}>
                    {this.props.children}
                </Grid>
                <Navigation/>
                <ActionButton/>
            </section>
        )
    }
}

export default withRouter(withStyles(styles)(App))