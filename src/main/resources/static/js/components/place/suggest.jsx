import location from '../../service/location.js'
import Autosuggest from 'react-autosuggest';
import TextField from 'material-ui/TextField';
import Paper from 'material-ui/Paper';
import { ListItem, ListItemText } from 'material-ui/List';
import { withStyles } from 'material-ui/styles';

function renderSuggestionsContainer(options) {
    const { containerProps, children } = options;

    return (
        <Paper {...containerProps} square>
            {children}
        </Paper>
    );
}

function getSuggestValue(item) {
    return item.name
}

function renderSuggest(item) {
    return (
        <ListItem button>
            <ListItemText primary={item.name} secondary={item.location.address} key={item.id}/>
        </ListItem>
    )
}


class PlacesList extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            suggestList: [],
            value: ''
        }
    }

    renderInput(inputProps) {
        const {classes, value, ref, ...other} = inputProps;

        return (
            <TextField
                className={classes.textField}
                value={value}
                inputRef={ref}
                InputProps={{
                    classes: {
                        input: classes.input,
                    },
                    ...other,
                }}
            />
        );
    }

    getSuggestions(query) {
        location.getPlacesNearWithName(query.value, list => this.setState({suggestList: list}));
    }

    clearSuggestions() {
        this.setState({suggestList: []})
    }

    handleChange(event, val) {
        console.log(event);
        console.log(val);
        this.setState({value: val.newValue});
        if (event.method === 'type') {
            this.props.suggestSelected({name: val});
        }
    }

    suggestSelected(_, { suggestion}) {
        const {id, name, provider} = suggestion;
        this.props.selected({
            id, name, provider
        })
    }

    render() {
        const {classes} = this.props;

        return (
            <Autosuggest
                theme={{
                    container: classes.container,
                    suggestionsContainerOpen: classes.suggestionsContainerOpen,
                    suggestionsList: classes.suggestionsList,
                    suggestion: classes.suggestion,
                }}
                renderInputComponent={this.renderInput.bind(this)}
                suggestions={this.state.suggestList}
                onSuggestionsFetchRequested={this.getSuggestions.bind(this)}
                onSuggestionsClearRequested={this.clearSuggestions.bind(this)}
                renderSuggestionsContainer={renderSuggestionsContainer}
                onSuggestionSelected={this.suggestSelected.bind(this)}
                getSuggestionValue={getSuggestValue}
                renderSuggestion={renderSuggest}
                inputProps={{
                    autoFocus: true,
                    classes,
                    placeholder: 'Where are you?',
                    value: this.state.value,
                    onChange: this.handleChange.bind(this),
                }}
            />
        );
    }
}



const styles = theme => ({
    container: {
        flexGrow: 1,
        position: 'relative',
    },
    suggestionsContainerOpen: {
        position: 'absolute',
        marginTop: theme.spacing.unit,
        marginBottom: theme.spacing.unit * 3,
        left: 0,
        right: 0,
        zIndex: 10,
    },
    suggestion: {
        display: 'block',
    },
    suggestionsList: {
        margin: 0,
        padding: 0,
        listStyleType: 'none',
    },
    textField: {
        width: '100%',
    },
});

export default withStyles(styles)(PlacesList)