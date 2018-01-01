import storage from "../storage/storage";
import {BigAvatar} from "./avatar/avatar.jsx";
import {withStyles} from "material-ui/styles";
import List, {ListItem, ListItemText,} from 'material-ui/List';
import ExitToApp from "material-ui-icons/ExitToApp"
import Grid from 'material-ui/Grid';

const classes = {

    avatarContainer: {
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        marginTop: 20,
        marginBottom: 50
    },

    logoutItem: {
        color: "#DD2C00",
        fontSize: '1.2rem'
    }
};

class Settings extends React.Component {

    constructor(args) {
        super(args);
        this.classes = args.classes;
    }

    render() {
        return (
            <div>
                <Grid container direction="column">
                    <Grid item xs={12}>
                        <div className={this.classes.avatarContainer}>
                            <BigAvatar avatar={storage.getState().user.avatarUrl}/>
                        </div>
                    </Grid>
                    <Grid container>
                        <Grid item xs={2}/>
                        <Grid item xs={8}>
                            <List>
                                <ListItem button onClick={this.logout.bind(this)}>
                                    <ListItemText classes={{text: this.classes.logoutItem}} primary="Logout"/>
                                    <ExitToApp color="error"/>
                                </ListItem>
                            </List>
                        </Grid>
                        <Grid item xs={2}/>
                    </Grid>
                </Grid>
            </div>
        );
    }

    componentWillMount() {
        storage.screenTitle("Settings");
    }

    logout() {
        $.post('/v1/open/logout').done(() => window.location = '/login')
    }
}

export default withStyles(classes)(Settings)