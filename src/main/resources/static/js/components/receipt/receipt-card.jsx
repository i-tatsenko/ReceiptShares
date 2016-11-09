import {Card, CardActions, CardHeader, CardMedia, CardTitle, CardText} from 'material-ui/Card';
import Divider from 'material-ui/Divider';
import Chip from 'material-ui/Chip';
import Avatar from 'material-ui/Avatar';

export default class ReceiptCard extends React.Component {

    render() {
        let receipt = this.props.receipt;
        receipt.members.push(receipt.owner);
        return (
            <Card>
                <CardHeader title={receipt.name}
                           subtitle={'by ' + receipt.owner.name}
                           avatar={receipt.owner.avatarUrl}
                />
                <CardText>
                    <p>Your Spendings: {this.userSpendings()}</p>
                    <p>Total Spendings: {this.totalSpendings()}</p>
                    <Divider/>
                    Members
                    {receipt.members.map(user =>
                        <Chip>
                            <Avatar src={user.avatarUrl}/>
                            {user.name}
                        </Chip>)}
                </CardText>

            </Card>
        )
    }

    userSpendings() {
        let userId = this.props.user.id;
        return this.props.receipt
            .orderedItems
            .filter(item => item.user.id === userId)
            .reduce((l, c) => l + c, 0)
    }

    totalSpendings() {
        return this.props.receipt
            .orderedItems
            .reduce((l, c) => l + c, 0)
    }
}