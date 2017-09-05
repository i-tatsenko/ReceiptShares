import {ListItem} from "material-ui/List";
import Avatar from "material-ui/Avatar";
import FontIcon from "material-ui/FontIcon";
import "./receipt.css";

export class OwnReceiptItem extends React.Component {

    render() {
        return (<CommonComponent {...this.props}
                                 actionButtons={this.actionButtons(this.props.item)}/>)
    }

    actionButtons(orderedItem) {
        return [
            <FontIcon className="fa fa-minus-circle receipt-item-actions__action" key={"MinusItem"+orderedItem.id}/>,
            <FontIcon className="fa fa-plus-circle receipt-item-actions__action" key={"PlusItem"+orderedItem.id}/>
        ]
    }
}

export class ReceiptItem extends React.Component {
    render() {
        return (<CommonComponent {...this.props} actionButtons={this.actionButtons(this.props.item)}/>)
    }

    actionButtons(item) {
        return [<span key={"MeeToo"+item.id}>Me too!</span>]
    }
}

class CommonComponent extends React.Component {

    render() {
        let orderedItem = this.props.item;
        let total = orderedItem.sum;
        let text = orderedItem.item.name + '. Price: ' + orderedItem.item.price + ". Total: " + total;
        console.log("Key", orderedItem.id);

        return (
            <ListItem primaryText={text}
                      leftAvatar={<Avatar src={orderedItem.owner.avatarUrl}/>}
                      children={this.children()}
                      className="receipt-item"
                      key={"ListItem"+orderedItem.id}
            />
        )
    }

    children() {
        return (
            <span className="receipt-item__actions">
                {this.props.actionButtons}
            </span>
        );

    }
}