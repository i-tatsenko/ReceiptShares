import React from "react";
import WaitingData from "../waiting-data.jsx";
import Divider from "material-ui/Divider";
import ReceiptItem from "./receipt-item.jsx";
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
                    <div className="receipt-header__my-spending">Your spendings: <span
                        className="receipt-header__my-spending-money">{mySpending}</span></div>
                    <Divider/>
                    <div className="receipt-header__total">Total: <span
                        className="receipt-header__total-money">{total}</span>
                    </div>
                    <Divider/>
                    Items:
                    <List>
                        {receipt.orderedItems.map(item => <ReceiptItem item={item} key={"receiptItem" + item.id}/>)}
                    </List>
                </section>
                <NewItemModal itemCreatedCallback={() => {
                    this.closeAddNewItemPopup();
                    this.getReceiptFromServer()
                }} opened={this.state.addNewItemPopupOpened}
                              closed={this.closeAddNewItemPopup.bind(this)}/>
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

    setReceipt(receipt) {
        this.setState({rec: receipt});
        this.props.setTitle(receipt.name);
    }

    getReceiptFromServer() {
        $.get('/v1/rec/' + this.props.params.id, resp => this.setReceipt(resp));
    }

    componentDidMount() {
        this.getReceiptFromServer();
        this.props.addMenuItems(this.additionalActions);
    }

    componentWillUnmount() {
        this.props.removeMenuItems(this.additionalActions.map(action => action.name));
    }
}