import MenuItem  from 'material-ui/Menu';

export default class CustomMenuItem extends React.Component {
    constructor(args) {
        super(args)
    }

    render() {
        return <MenuItem primaryText={this.props.label} onTouchTap={() => this.props.action()} />
    }
}