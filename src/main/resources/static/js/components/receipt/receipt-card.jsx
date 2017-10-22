import Card, {CardHeader, CardContent} from "material-ui/Card";
import Chip from "material-ui/Chip";
import Avatar from "material-ui/Avatar";
import {chipStyle, chipWrapperStyle} from "../default-styles.jsx";
import {withRouter} from "react-router-dom";
import storage from "../../storage/storage.js"
import Typography from 'material-ui/Typography';


class ReceiptCard extends React.Component {

    render() {
        let receipt = this.props.receipt;
        let total = receipt.total;
        let mySpending = receipt.totalsPerMember[storage.getState().user.id] || 0;
        let members = receipt.members.length === 0 ? "" :
            <section>
                Members:
                <div style={chipWrapperStyle}>
                    {receipt.members.map(user =>
                        <Chip key={'avatarlink' + user.avatarUrl} style={chipStyle}>
                            <Avatar src={user.avatarUrl}/>
                            {user.name}
                        </Chip>)}
                </div>
            </section>;
        return (
            <Card style={{marginBottom: '15px'}} onClick={this.goToReceipt.bind(this)}>
                <CardHeader avatar={<Avatar src={receipt.owner.avatarUrl}/>}
                            title={receipt.name}
                            subheader={'by ' + receipt.owner.name}>
                </CardHeader>
                <CardContent>
                    <Typography type="body1">Your Part: {mySpending}</Typography>
                    <Typography type="body1">Total: {total}</Typography>
                    {members}
                </CardContent>
            </Card>

        )
    }

    goToReceipt() {
        this.props.history.push("/receipt/" + this.props.receipt.id);
    }
}

export default withRouter(ReceiptCard)