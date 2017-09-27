import React from "react";
import storage from "../../storage/storage.js"
import WaitingData from "../waiting-data.jsx";
import Divider from "material-ui/Divider";
import {OwnReceiptItem, ReceiptItem} from "./receipt-item.jsx";
import NewItemModal from "./receipt-item-create-modal.jsx";
import List from "material-ui/List";
import FlatButton from 'material-ui/FlatButton';
import CustomMenuItem from "../menu/custom-menu-item.jsx";
import "./receipt.css";
import {withRouter} from "react-router-dom";
import Snackbar from 'material-ui/Snackbar';
import Subheader from 'material-ui/Subheader';

const NotFoundReceipt = withRouter(props => {
    return (
        <div>
            {"This receipt was not found on the server ;("}
            <FlatButton secondary={true} label="Return" onTouchTap={() => props.history.push("/")}/>
        </div>
    )
});

export default class Receipt extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            rec: null,
            addNewItemPopupOpened: false,
            notFoundError: false,
            showItemDeletedMessage: false,
            itemDeletedMessage: "",
            deletedItemId: "",
            itemsIdWithPendingChange: []
        };

        this.additionalAction =
            <CustomMenuItem label="Add new item" action={() => this.setState({addNewItemPopupOpened: true})}/>
    }

    closeAddNewItemPopup() {
        this.setState({addNewItemPopupOpened: false});
    }

    render() {
        if (this.state.notFoundError) {
            return (<NotFoundReceipt/>);
        }
        let receipt = this.state.rec;
        if (!receipt) {
            return (<WaitingData/>);
        }
        let mySpending = receipt.totalsPerMember[storage.getState().user.id] || 0;
        let {myItems, otherItems} = this.renderItems();
        return (
            <section className="receipt">
                <section className="receipt-header__spending">
                    <div className="receipt-header__my-spending">Your spending: <span
                        className="receipt-header__my-spending-money">{mySpending.toFixed(2)}</span></div>
                    <Divider/>
                    <div className="receipt-header__total">Total: <span
                        className="receipt-header__total-money">{receipt.total.toFixed(2)}</span>
                    </div>
                    <Divider/>
                    <List>
                        <Subheader>Items</Subheader>
                        {myItems}
                        <Divider inset={true}/>
                        {otherItems}
                    </List>
                </section>
                <NewItemModal itemCreatedCallback={() => {
                    this.closeAddNewItemPopup();
                    this.getReceiptFromServer()
                }}
                              opened={this.state.addNewItemPopupOpened}
                              closed={this.closeAddNewItemPopup.bind(this)}
                              receiptId={this.state.rec.id}/>
                <Snackbar
                    open={this.state.showItemDeletedMessage}
                    message={this.state.itemDeletedMessage}
                    action="undo"
                    onActionTouchTap={() => this.undoDelete(this.state.rec.id, this.state.deletedItemId)}
                    onRequestClose={() => this.setState({showItemDeletedMessage: false})}
                    autoHideDuration={5000}
                />
            </section>);
    }


    renderItems() {
        let items = this.state.rec.orderedItems || [];
        let myItems = items.filter(Receipt.currentUsersOrderedItem)
                           .map(item => <OwnReceiptItem item={item}
                                                        receipt={this.state.rec}
                                                        changePending={this.changeIsPendingForItem(item)}
                                                        incrementItem={this.incrementItemCount.bind(this)}
                                                        deleteItem={this.deleteItem.bind(this)}
                                                        key={item.id}/>);

        let otherItems = items.filter(item => !Receipt.currentUsersOrderedItem(item))
                              .map(item => <ReceiptItem item={item} receipt={this.state.rec}
                                                        cloneItem={this.cloneItem.bind(this)}
                                                        changePending={this.changeIsPendingForItem(item)}
                                                        key={item.id}
                              />);
        return {myItems, otherItems}
    }

    static currentUsersOrderedItem(item) {
        return item.owner.id === storage.getState().user.id
    }

    changeIsPendingForItem(item) {
        return this.state.itemsIdWithPendingChange.indexOf(item.id) !== -1;
    }

    getReceiptFromServer(callback) {
        $.get('/v1/receipt/' + this.props.match.params.id, resp => {
            this.setState({rec: resp});
            storage.screenTitle(resp.name);
            callback && callback();
        }).fail(() => this.setState({notFoundError: true}));
    }

    componentDidMount() {
        this.getReceiptFromServer();
        storage.addAddActionButtonMenuItem(storage.addAddActionButtonMenuItem(this.additionalAction));
    }

    componentWillUnmount() {
        storage.removeAddActionButtonMenuItem(this.additionalAction);
    }

    incrementItemCount(receiptId, itemId) {
        this.markItemAsPendingForChange(itemId);
        $.post(`/v1/receipt/${receiptId}/item/${itemId}/increment`).done(() =>
            this.getReceiptFromServer(() => this.unMarkItemAsPendingForChange(itemId))
        );
    }

    deleteItem(receiptId, orderedItem) {
        let itemId = orderedItem.id;
        this.markItemAsPendingForChange(itemId);
        $.post(`/v1/receipt/${receiptId}/item/${itemId}/increment?amount=-1`).done(data => {
                this.getReceiptFromServer(() => {
                    if (data.value) {
                        this.setState({
                            deletedItemId: itemId,
                            itemDeletedMessage: `${orderedItem.item.name} was removed.`,
                            showItemDeletedMessage: true
                        });
                    }
                    this.unMarkItemAsPendingForChange(itemId);
                });
            }
        );
    }

    cloneItem(receiptId, itemId) {
        this.markItemAsPendingForChange(itemId);
        $.post(`/v1/receipt/${receiptId}/item/${itemId}/clone`).done(() => {
            this.getReceiptFromServer(() => this.unMarkItemAsPendingForChange(itemId))
        })
    }

    markItemAsPendingForChange(itemId) {
        this.setState(prevState => ({itemsIdWithPendingChange: [...prevState.itemsIdWithPendingChange, itemId]}))
    }

    unMarkItemAsPendingForChange(itemId) {
        this.setState(prevState => {
            let index = prevState.itemsIdWithPendingChange.indexOf(itemId);
            if (index !== -1) {
                return {
                    markItemAsPendingForChange: prevState.itemsIdWithPendingChange.splice(index, 1)
                }
            }
        })
    }

    undoDelete(receiptId, orderedItemId) {
        this.setState({
            showItemDeletedMessage: false,
            itemDeletedMessage: "",
            deletedItemId: ""
        });
        $.post(`/v1/receipt/${receiptId}/item/${orderedItemId}/restore`).done(() => this.getReceiptFromServer());
    }
}