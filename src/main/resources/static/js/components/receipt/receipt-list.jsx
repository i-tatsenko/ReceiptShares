import ReceiptCard from './receipt-card.jsx';
import WaitingData from  '../waiting-data.jsx';

let NoReceipts = () => <section>{"Here will be present list of your receipts"}</section>;


export default React.createClass({

    // constructor(args) {
    //     this.props.setTitle("Receipts");
    // },

    render() {
        if (this.props.receiptsList === null) {
            return (<WaitingData/>)
        }
        if (this.props.receiptsList.length == 0) {
            return (<NoReceipts/>)
        }
        let user = this.props.user;
        return (
            <section>
                {this.props.receiptsList.map(receipt => <ReceiptCard receipt={receipt} user={user.id}
                                                                     key={'rec' + receipt.id}/>)}
            </section>
        )
    }
})
