import MenuItem  from 'material-ui/Menu';
import {withRouter} from "react-router-dom";

class NewReceiptMenuItem extends React.Component {
    render() {
        return <MenuItem primaryText={"New receipt"} onTouchTap={() => this.props.history.push('/new')} />
    }
}

export default withRouter(NewReceiptMenuItem)