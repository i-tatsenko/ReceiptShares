import React from "react";
import WaitingData from  '../waiting-data.jsx';
import Divider from 'material-ui/Divider';
import ReceiptItem from './receipt-item.jsx';

import 'style!css!./receipt.css';
export default class Receipt extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            rec: null
        }
    }

    render() {
        let receipt = this.state.rec;
        if (!receipt) {
            return (<WaitingData/>);
        }
        let total = 0;
        let mySpendings = 0;
        for (let item of receipt.orderedItems) {
            if (item.status == 'ACTIVE') {
                if (item.user.id == this.props.user.id) {
                    mySpendings += item.item.price;
                }
                total += item.item.price;
            }
        }
        return (
            <section className="receipt-header__spendings">
                <div className="receipt-header__my-spendings">Your spendings: <span
                    className="receipt-header__my-spendings-money">{mySpendings}</span></div>
                <Divider/>
                <div className="receipt-header__total">Total: <span
                    className="receipt-header__total-money">{total}</span>
                </div>
                <Divider/>
                Items:
                {receipt.orderedItems.map(item => <ReceiptItem item={item} key={item.id}/>)}
            </section>
        )
    }

    setReceipt(receipt) {
        this.setState({rec: receipt});
        this.props.setTitle(receipt.name);
    }

    componentDidMount() {
        $.get('/v1/rec/' + this.props.params.id, resp => this.setReceipt(resp));
        this.props.addMenuItems([{name: "Action 1", action: () => alert('action 1')},
            {name: "Action 2", action: () => alert('action 2')}]);
    }

    componentWillUnmount() {
        debugger;
        this.props.removeMenuItems(["Action 1", "Action 2"]);
    }
}