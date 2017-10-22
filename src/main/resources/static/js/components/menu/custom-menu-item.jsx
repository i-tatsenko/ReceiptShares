import {MenuItem}  from 'material-ui/Menu';

export default class CustomMenuItem extends React.Component {
    constructor(args) {
        super(args)
    }

    render() {
        return <MenuItem onClick={() => this.props.action()} >{this.props.label}</MenuItem>
    }
}