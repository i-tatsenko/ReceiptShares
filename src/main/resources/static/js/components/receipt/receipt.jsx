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

        this.additionalAction = <CustomMenuItem label="Add new item" action={() => this.setState({addNewItemPopupOpened: true})}/>
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
        let {total, mySpending} = this.calculateSpending();

        return (
            <section className="receipt">
                <section className="receipt-header__spending">
                    <div className="receipt-header__my-spending">Your spending: <span
                        className="receipt-header__my-spending-money">{mySpending.toFixed(2)}</span></div>
                    <Divider/>
                    <div className="receipt-header__total">Total: <span
                        className="receipt-header__total-money">{total.toFixed(2)}</span>
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
                <Snackbar
                    open={this.state.showItemDeletedMessage}
                    message={this.state.itemDeletedMessage}
                    action="undo"
                    onActionTouchTap={() => this.undoDelete(this.state.rec.id, this.state.deletedItemId)}
                    onRequestClose={() => this.setState({showItemDeletedMessage: false})}
                />
            </section>);
    }

    calculateSpending() {
        let total = 0;
        let mySpending = 0;
        for (let item of (this.state.rec.orderedItems || [])) {
            if (item.status === 'ACTIVE') {
                if (item.owner.id === storage.getState().user.id) {
                    mySpending += item.item.price;
                }
                total += item.item.price;
            }
        }
        return {total, mySpending};
    }

    renderItems() {
        let items = [];
        for (let item of (this.state.rec.orderedItems || [])) {
            let foundItem = items.find(present => present.owner.id === item.owner.id && present.item.id === item.item.id);
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
                        if (this.currentUsersOrderedItem(item)) {
                            console.log(item);
                            return <OwnReceiptItem item={item}
                                                   receipt={this.state.rec}
                                                   changePending={this.state.itemsIdWithPendingChange.indexOf(item.id) !== -1}
                                                   cloneItem={this.cloneItem.bind(this)}
                                                   deleteItem={this.deleteItem.bind(this)}
                            />;
                        }
                        else
                            return <ReceiptItem item={item} receipt={this.state.rec} />;
                    })
    }

    currentUsersOrderedItem(item) {
        return item.owner.id === storage.getState().user.id
    }

    getReceiptFromServer() {
        $.get('/v1/receipt/' + this.props.match.params.id, resp => {
            this.setState({rec: resp});
            storage.screenTitle(resp.name)
        }).fail(() => this.setState({notFoundError: true}));
    }

    componentDidMount() {
        this.getReceiptFromServer();
        storage.addAddActionButtonMenuItem(storage.addAddActionButtonMenuItem(this.additionalAction));
    }

    componentWillUnmount() {
        storage.removeAddActionButtonMenuItem(this.additionalAction);
    }

    cloneItem(receiptId, itemId) {
        this.markItemAsPendingForChange(itemId);
        $.post(`/v1/receipt/${receiptId}/item/${itemId}/duplicate`).done(() => {
            this.unMarkItemAsPendingForChange(itemId);
            this.getReceiptFromServer();
        });
    }

    deleteItem(receiptId, orderedItem) {
        let itemId = orderedItem.id;
        this.markItemAsPendingForChange(itemId);
        $.ajax({
            url: `/v1/receipt/${receiptId}/item/${itemId}`,
            method: "DELETE",
            success: () => {
                this.setState({
                    deletedItemId: itemId,
                    itemDeletedMessage: `1 ${orderedItem.item.name} was removed.`,
                    showItemDeletedMessage: true
                });
                this.unMarkItemAsPendingForChange(itemId);
                this.getReceiptFromServer();
            }
        })
    }

    markItemAsPendingForChange(itemId) {
        this.setState(prevState => ({itemsIdWithPendingChange: [...prevState.itemsIdWithPendingChange, itemId]}))
    }

    unMarkItemAsPendingForChange(itemId) {
        //TODO mark item as pending change was done after full update only
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