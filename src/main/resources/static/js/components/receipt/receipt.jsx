import React from "react";
import WaitingData from "../waiting-data.jsx";
import Divider from "material-ui/Divider";
import ReceiptItem from "./receipt-item.jsx";
import FlatButton from "material-ui/FlatButton";
import Dialog from "material-ui/Dialog";
import TextField from "material-ui/TextField";
import List from "material-ui/List";
import "style!css!./receipt.css";
export default class Receipt extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            rec: null,
            addNewItemPopupOpened: false,
        };

        this.additionalActions = [{
            name: "Add new item",
            action: this.openAddNewItemPopup.bind(this)
        }];
    }

    validateNewItem() {
        return this.state.newItemName && !isNaN(this.state.newItemPrice);
    }

    closeAddNewItemPopup() {
        this.setState({addNewItemPopupOpened: false});
    }

    openAddNewItemPopup() {
        this.setState({addNewItemPopupOpened: true});
    }

    render() {
        let receipt = this.state.rec;
        if (!receipt) {
            return (<WaitingData/>);
        }
        let {total, mySpending} = this.calculateSpending();

        return (
            <section className="receipt">
                <section className="receipt-header__spending">
                    <div className="receipt-header__my-spending">Your spendings: <span
                        className="receipt-header__my-spending-money">{mySpending}</span></div>
                    <Divider/>
                    <div className="receipt-header__total">Total: <span
                        className="receipt-header__total-money">{total}</span>
                    </div>
                    <Divider/>
                    Items:
                    <List>
                        {receipt.orderedItems.map(item => <ReceiptItem item={item} key={item.id}/>)}
                    </List>
                </section>
                {this.createAddNewItemDialog()}
            </section>);
    }

    createAddNewItemActions() {
        return [
            <FlatButton
                label="Cancel"
                primary={true}
                onTouchTap={this.closeAddNewItemPopup.bind(this)}
            />,
            <FlatButton
                label="Add"
                primary={true}
                keyboardFocused={true}
                onTouchTap={this.createNewItem.bind(this)}
                disabled={!this.validateNewItem()}
            />
        ]
    }

    createAddNewItemDialog() {
        let t = this;
        return (
            <Dialog title="Add item" modal={false} open={this.state.addNewItemPopupOpened}
                    actions={this.createAddNewItemActions()} onRequestClose={this.closeAddNewItemPopup.bind(this)}>
                <TextField
                    hintText="Item name"
                    floatingLabelText="Name"
                    name="name"
                    onChange={e => t.setState({newItemName: e.target.value})}/>
                <br/>
                <TextField
                    hintText="Item price"
                    floatingLabelText="Price"
                    name="price"
                    onChange={e => t.setState({newItemPrice: e.target.value})}/>
            </Dialog>
        )
    }

    createNewItem() {
        let data = JSON.stringify({
            name: this.state.newItemName,
            price: this.state.newItemPrice,
            receiptId: this.state.rec.id
        });
        $.ajax({
            type: "POST",
            url: "/v1/rec/new-item",
            data: data,
            success: this.getReceiptFromServer.bind(this),
            contentType: "application/json",
            fail: () => alert("Can't add new item")
        });
        this.closeAddNewItemPopup();
    }

    calculateSpending() {
        let total = 0;
        let mySpending = 0;
        for (let item of this.state.rec.orderedItems) {
            if (item.status == 'ACTIVE') {
                if (item.user.id == this.props.user.id) {
                    mySpending += item.item.price;
                }
                total += item.item.price;
            }
        }
        return {total, mySpending};
    }

    setReceipt(receipt) {
        this.setState({rec: receipt});
        this.props.setTitle(receipt.name);
    }

    componentDidMount() {
        this.getReceiptFromServer();
        this.props.addMenuItems(this.additionalActions);
    }

    getReceiptFromServer() {
        $.get('/v1/rec/' + this.props.params.id, resp => this.setReceipt(resp));
    }

    componentWillUnmount() {
        this.props.removeMenuItems(this.additionalActions.map(action => action.name));
    }
}