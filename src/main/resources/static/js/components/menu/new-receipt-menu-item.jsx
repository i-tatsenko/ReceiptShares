import MenuItem from "material-ui/MenuItem";
import {withRouter} from "react-router-dom";

class NewReceiptMenuItem extends React.Component {
    render() {
        return <MenuItem primaryText={"New receipt"} onTouchTap={() => this.props.history.push('/new')} key="new-receipt"/>
    }
}

export default withRouter(NewReceiptMenuItem)