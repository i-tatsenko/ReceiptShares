import {ListItem} from "material-ui/List";
import Avatar from "material-ui/Avatar";
import CircularProgress from 'material-ui/CircularProgress';
import IconButton from 'material-ui/IconButton';

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
            <IconButton iconClassName="fa fa-minus-square-o receipt-item-actions__action"
                        key={"MinusItem" + orderedItem.id}
                        onTouchTap={() => this.props.deleteItem(receipt.id, orderedItem)}/>,
            <IconButton iconClassName="fa fa-plus-square-o receipt-item-actions__action"
                        key={"PlusItem" + orderedItem.id}
                        onTouchTap={() => this.props.incrementItem(receipt.id, orderedItem.id)}/>
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
        return (<CommonComponent {...this.props} actionButtons={this.actionButtons(this.props.item)} />)
    }

    actionButtons(item) {
        return [<IconButton key={"MeeToo" + item.id} iconClassName="fa fa-clone receipt-item-actions__action"
                            onTouchTap={() => this.props.cloneItem(this.props.receipt.id, this.props.item.id)}/>]
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
            <ListItem primaryText={primaryText}
                      secondaryText={secondaryText}
                      leftAvatar={<Avatar className='receipt-item__avatar' src={orderedItem.owner.avatarUrl || "/images/no-photo-avatar.svg"}/>}
                      children={this.children()}
                      className="receipt-item"
                      key={"ListItem" + orderedItem.id}
            />
        )
    }

    children() {
        if (this.props.changePending) {
            return <CircularProgress/>
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