import {Card, CardActions, CardHeader, CardMedia, CardTitle, CardText} from 'material-ui/Card';
import Chip from 'material-ui/Chip';
import Avatar from 'material-ui/Avatar';
import {chipStyle, chipWrapperStyle} from '../default-styles.jsx';

export default class ReceiptCard extends React.Component {

    render() {
        let receipt = this.props.receipt;
        let members = receipt.members.length == 0 ? "" :
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
            <Card style={{marginBottom: '15px'}}>
                <CardHeader title={receipt.name}
                            subtitle={'by ' + receipt.owner.name}
                            avatar={receipt.owner.avatarUrl}
                />
                <CardText expandable={false}>
                    <p>Your Spendings: {this.userSpendings()}</p>
                    <p>Total Spendings: {this.totalSpendings()}</p>
                    {members}
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