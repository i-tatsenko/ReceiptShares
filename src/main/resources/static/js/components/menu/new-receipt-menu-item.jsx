import {MenuItem}  from 'material-ui/Menu';
import {withRouter} from "react-router-dom";

class NewReceiptMenuItem extends React.Component {
    render() {
        return <MenuItem onClick={() => this.props.history.push('/new')} >New receipt</MenuItem>
    }
}

export default withRouter(NewReceiptMenuItem)