import ReceiptCard from './receipt-card.jsx'
import CircularProgress from 'material-ui/CircularProgress';

let UpdatingReceipts = () => <CircularProgress size={80} thickness={5} style={{marginLeft: '50%'}}/>;
let NoReceipts = () => <section>{"Here will be present list of your receipts"}</section>;


export default React.createClass({

    // constructor(args) {
    //     this.props.setTitle("Receipts");
    // },

    render() {
        if (this.props.receiptsList === null) {
            return (<UpdatingReceipts/>)
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

