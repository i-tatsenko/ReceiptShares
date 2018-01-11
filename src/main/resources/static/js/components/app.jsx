import ChevronLeft from 'material-ui-icons/ChevronLeft'
import AppBar from "material-ui/AppBar";
import Grid from 'material-ui/Grid';
import IconButton from 'material-ui/IconButton';
import {withStyles} from 'material-ui/styles';
import Toolbar from 'material-ui/Toolbar';
import Typography from 'material-ui/Typography';
import {withRouter} from "react-router-dom";
import NavigationHistory from '../service/navigation-history';
import storage from "../storage/storage.js"
import ActionButton from "./add-action-button.jsx"
import Navigation from "./navigation.jsx";

const styles = theme => ({
    container: {
        marginLeft: 10,
        marginTop: theme.spacing.unit * 8,
        marginRight: 10,
        marginBottom: theme.spacing.unit * 7 + 5,
        paddingTop: theme.spacing.unit,
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
            haveReturnView: false
        };
        storage.listenFor("screenTitle", () => this.setState({barTitle: storage.getState().screenTitle}))
    }

    render() {
        let backIcon = null;
        if (this.state.haveReturnView) {
            backIcon =
                <IconButton color="contrast" onClick={() => this.props.history.push(NavigationHistory.popHistorySilently())}>
                    <ChevronLeft/>
                </IconButton>
        }
        return (
            <section className={this.classes.app}>
                <AppBar>
                    <Toolbar>
                        {backIcon}
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

    componentWillMount() {
        NavigationHistory.listen(stack => this.setState({haveReturnView: stack.length > 0}))
    }
}

export default withRouter(withStyles(styles)(App))