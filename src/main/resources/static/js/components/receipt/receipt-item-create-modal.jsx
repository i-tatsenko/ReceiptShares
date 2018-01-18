import Button from 'material-ui/Button';
import Dialog, {DialogActions, DialogContent, DialogTitle,} from 'material-ui/Dialog';
import TextField from "material-ui/TextField";

export default class NewItemModal extends React.Component {

    constructor(args) {
        super(args);
        this.state = {};
        this.close = () => {
            this.setState({name: '', price: ''});
            this.props.closed();
        }
    }

    render() {
        let t = this;
        return (
            <Dialog  open={this.props.opened} onClose={this.close}>
                <DialogTitle>
                    Add new item
                </DialogTitle>
                <DialogContent>
                    <TextField
                        label="Item name"
                        placeholder="Name"
                        name="name"
                        onChange={e => t.setState({name: e.target.value})}/>
                    <br/>
                    <TextField
                        label="Item price"
                        placeholder="Price"
                        name="price"
                        onChange={e => t.setState({price: e.target.value})}/>
                </DialogContent>
                <DialogActions>
                    <Button onClick={this.close} color="primary">
                        Cancel
                    </Button>
                    <Button onClick={() => this.createNewItem()} color="primary" disabled={!this.validateNewItem()}>
                        Create
                    </Button>
                </DialogActions>

            </Dialog>
        );
    }

    validateNewItem() {
        return this.state.name && this.state.price && !isNaN(this.state.price);
    }

    createNewItem() {
        let newItem = {name: this.state.name, price: this.state.price};
        let t = this;
        $.ajax({
            type: "POST",
            url: `/v1/receipt/${this.props.receiptId}/new-item`,
            data: JSON.stringify(newItem),
            success: t.props.itemCreatedCallback,
            contentType: "application/json",
            error: () => alert("Can't add new item")
        });
    }

}