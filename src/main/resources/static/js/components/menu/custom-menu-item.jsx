import MenuItem from "material-ui/MenuItem";

export default class CustomMenuItem extends React.Component {
    constructor(args) {
        super(args)
    }

    render() {
        return <MenuItem primaryText={this.props.label} onTouchTap={() => this.props.action()} key={this.props.label}/>
    }
}