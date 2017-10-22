import Dialog from "material-ui/Dialog";
import TextField from "material-ui/TextField";
import Button from 'material-ui/Button';

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
            <Dialog title="Add item" modal={false} open={this.props.opened}
                    actions={this.createAddNewItemActions()} onRequestClose={this.close}>
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
            </Dialog>
        );
    }

    createAddNewItemActions() {
        return [
            <Button
                label="Cancel"
                primary={true}
                onClick={this.close}
            />,
            <Button
                label="Add"
                primary={true}
                keyboardFocused={true}
                onClick={this.createNewItem.bind(this)}
                disabled={!this.validateNewItem()}
            />
        ]
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