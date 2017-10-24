import {ListItem, ListItemIcon, ListItemText} from 'material-ui/List';
import Avatar from "material-ui/Avatar";
import {LinearProgress} from 'material-ui/Progress';
import IconButton from 'material-ui/IconButton';
import Remove from 'material-ui-icons/Remove'
import Add from 'material-ui-icons/Add'
import PlusOne from 'material-ui-icons/PlusOne'

import "./receipt.css";

export class OwnReceiptItem extends React.Component {

    render() {
        return (
            <div>
                <CommonComponent {...this.props}
                                 actionButtons={this.actionButtons(this.props.receipt, this.props.item)}/>
            </div>
        )
    }

    actionButtons(receipt, orderedItem) {
        return [
            <IconButton key={"MinusItem" + orderedItem.id}
                        onClick={() => this.props.deleteItem(receipt.id, orderedItem)}>
                <Remove/>
            </IconButton>,
            <IconButton key={"PlusItem" + orderedItem.id}
                        onClick={() => this.props.incrementItem(receipt.id, orderedItem.id)}>
                <Add/>
            </IconButton>
        ]
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.item.id !== this.props.item.id) {
            this.setState({item: nextProps.item});
        }
    }
}

export class ReceiptItem extends React.Component {
    render() {
        return (<CommonComponent {...this.props} actionButtons={this.actionButtons(this.props.item)}/>)
    }

    actionButtons(item) {
        return [<IconButton key={"MeeToo" + item.id}
                            onClick={() => this.props.cloneItem(this.props.receipt.id, this.props.item.id)}>
            <PlusOne/>
        </IconButton>]
    }
}

class CommonComponent extends React.Component {

    render() {
        let orderedItem = this.props.item;
        let total = orderedItem.total;
        let primaryText = <div className='receipt-item__main-text'>{orderedItem.item.name}: ${total.toFixed(2)}</div>;
        let secondaryText = <div className='receipt-item__secondary-text'>{orderedItem.count} x
            ${orderedItem.item.price}</div>;

        return (
            <ListItem className="receipt-item" key={"ListItem" + orderedItem.id}>
                <Avatar className='receipt-item__avatar' src={orderedItem.owner.avatarUrl}/>
                <ListItemText primary={primaryText} secondary={secondaryText}/>
                {this.children()}
            </ListItem>
        )
    }

    children() {
        if (this.props.changePending) {
            return <LinearProgress style={{width: '100%'}}/>
        }
        else {
            return (
                <div className="receipt-item__actions" key={"action-buttons"}>
                    {this.props.actionButtons}
                </div>
            )

        }
    }
}