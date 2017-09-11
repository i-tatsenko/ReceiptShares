import {ListItem} from "material-ui/List";
import Avatar from "material-ui/Avatar";
import CircularProgress from 'material-ui/CircularProgress';
import IconButton from 'material-ui/IconButton';

import "./receipt.css";

export class OwnReceiptItem extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            item: this.props.item
        }
    }

    render() {
        return (
            <div>
                <CommonComponent {...this.props}
                                 actionButtons={this.actionButtons(this.props.receipt, this.state.item)}/>

            </div>
        )
    }

    actionButtons(receipt, orderedItem) {
        if (this.props.changePending) {
            return [<CircularProgress/>]
        } else {
            return [
                <IconButton iconClassName="fa fa-minus-circle receipt-item-actions__action"
                          key={"MinusItem" + orderedItem.id}
                          onTouchTap={() => this.props.deleteItem(receipt.id, orderedItem)}/>,
                <IconButton iconClassName="fa fa-plus-circle receipt-item-actions__action" key={"PlusItem" + orderedItem.id}
                          onTouchTap={() => this.props.cloneItem(receipt.id, orderedItem.id)}/>
            ]
        }
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.item.id !== this.state.item.id) {
            this.setState({item: nextProps.item});
        }
    }
}

export class ReceiptItem extends React.Component {
    render() {
        return (<CommonComponent {...this.props} actionButtons={this.actionButtons(this.props.item)}/>)
    }

    actionButtons(item) {
        return [<div key={"MeeToo" + item.id} className="receipt-item-actions__action">Me too!</div>]
    }
}

class CommonComponent extends React.Component {

    render() {
        let orderedItem = this.props.item;
        let total = orderedItem.sum;
        let primaryText = <div className='receipt-item__main-text'>{orderedItem.item.name}: ${total.toFixed(2)}</div>;
        let secondaryText = <div className='receipt-item__secondary-text'>{orderedItem.count} x ${orderedItem.item.price}</div>;

        return (
            <ListItem primaryText={primaryText}
                      secondaryText={secondaryText}
                      leftAvatar={<Avatar className='receipt-item__avatar' src={orderedItem.owner.avatarUrl}/>}
                      children={this.children()}
                      className="receipt-item"
                      key={"ListItem" + orderedItem.id}
            />
        )
    }

    children() {
        return (
            <div className="receipt-item__actions">
                {this.props.actionButtons}
            </div>
        );

    }
}