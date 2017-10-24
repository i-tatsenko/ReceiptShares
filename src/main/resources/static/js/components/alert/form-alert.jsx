import React from 'react'
import { red } from 'material-ui/colors';
import { withStyles } from 'material-ui/styles';

const styles = {
    alert : {
        border: '3px solid ' + red[500],
        padding: '10px',
        color: red[900],
        marginTop: 10
    }
};

class FormAlert extends React.Component {

    constructor(args) {
        super(args);
        this.classes = args.classes;
    }

    render() {
        let css = {
            display: this.props.message ? 'block' : 'none'
        };
        return (
            <div className={this.classes.alert} hidden="true" style={css}>
                {this.props.message}
            </div>
        )
    }
}

export default withStyles(styles)(FormAlert)