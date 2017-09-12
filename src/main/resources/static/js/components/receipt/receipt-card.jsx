import {Card, CardActions, CardHeader, CardMedia, CardTitle, CardText} from "material-ui/Card";
import Chip from "material-ui/Chip";
import Avatar from "material-ui/Avatar";
import {chipStyle, chipWrapperStyle} from "../default-styles.jsx";
import {withRouter} from "react-router-dom";
import storage from "../../storage/storage.js"

class ReceiptCard extends React.Component {

    render() {
        let receipt = this.props.receipt;
        let total = receipt.total;
        let mySpending = receipt.totalsPerMember[storage.getState().user.id];
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
            <Card style={{marginBottom: '15px'}} onTouchTap={this.goToReceipt.bind(this)}>
                <CardHeader title={receipt.name}
                            subtitle={'by ' + receipt.owner.name}
                            avatar={receipt.owner.avatarUrl}
                />
                <CardText expandable={false}>
                    <p>Your Part: {mySpending}</p>
                    <p>Total: {total}</p>
                    {members}
                </CardText>
            </Card>
        )
    }

    goToReceipt() {
        this.props.history.push("/receipt/" + this.props.receipt.id);
    }
}

export default withRouter(ReceiptCard)