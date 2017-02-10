import React from "react";
import WaitingData from "../waiting-data.jsx";
import Divider from "material-ui/Divider";
import {OwnReceiptItem, ReceiptItem} from "./receipt-item.jsx";
import NewItemModal from "./receipt-item-create-modal.jsx";
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
            action: () => this.setState({addNewItemPopupOpened: true})
        }];
    }

    closeAddNewItemPopup() {
        this.setState({addNewItemPopupOpened: false});
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
                    <div className="receipt-header__my-spending">Your spending: <span
                        className="receipt-header__my-spending-money">{mySpending}</span></div>
                    <Divider/>
                    <div className="receipt-header__total">Total: <span
                        className="receipt-header__total-money">{total}</span>
                    </div>
                    <Divider/>
                    Items:
                    <List>
                        {this.renderItems()}
                    </List>
                </section>
                <NewItemModal itemCreatedCallback={() => {
                    this.closeAddNewItemPopup();
                    this.getReceiptFromServer()
                }}
                              opened={this.state.addNewItemPopupOpened}
                              closed={this.closeAddNewItemPopup.bind(this)}
                              receiptId={this.state.rec.id}/>
            </section>);
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

    renderItems() {
        let items = [];
        for (let item of this.state.rec.orderedItems) {
            let foundItem = items.find(present => present.user.id == item.user.id && present.item.id == item.item.id);
            if (foundItem) {
                foundItem.sum += foundItem.item.price;
                foundItem.count++;
                foundItem.orderedItemIds.push(item.id);
            } else {
                items.push(item);
                item.sum = item.item.price;
                item.count = 1;
                item.orderedItemIds = [item.id]
            }
        }
        return items.map(item => {
                        let key= "receiptItem" + item.id;
                        if (this.currentUsersOrderedItem(item))
                            return <OwnReceiptItem item={item} key={key} />;
                        else
                            return <ReceiptItem item={item} key={key} />;
                    })
    }

    currentUsersOrderedItem(item) {
        return item.user.id === this.props.user.id
    }

    getReceiptFromServer() {
        $.get('/v1/rec/' + this.props.params.id, resp => {
            this.setState({rec: resp});
            this.props.setTitle(resp.name);
        });
    }

    componentDidMount() {
        this.getReceiptFromServer();
        this.props.addMenuItems(this.additionalActions);
    }

    componentWillUnmount() {
        this.props.removeMenuItems(this.additionalActions.map(action => action.name));
    }
}