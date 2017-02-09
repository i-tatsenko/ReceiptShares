import ReceiptCard from "./receipt-card.jsx";
import WaitingData from "../waiting-data.jsx";
require("style!css!./receipt.css");

let NoReceipts = () => <section className="receipt-list__no-receipts">{"Here will be present list of your receipts"}</section>;


export default React.createClass({

    getInitialState() {
        return {};
    },

    render() {
        if (!this.state.receiptsList) {
            return (<WaitingData/>)
        }
        if (this.state.receiptsList.length == 0) {
            return (<NoReceipts/>)
        }
        let user = this.props.user;
        return (
            <section>
                {this.state.receiptsList.map(receipt => <ReceiptCard receipt={receipt} user={user.id}
                                                                     key={'rec' + receipt.id}/>)}
            </section>
        )
    },

    componentWillMount() {
        this.props.setTitle("Receipts");
        let t = this;
        $.get('/v1/rec/all').done(resp => t.setState({receiptsList: resp}));
    }
})

