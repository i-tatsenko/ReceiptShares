import ListItem from 'material-ui/List';


export default class ReceiptItem extends React.Component {

    render() {
        let orderedItem = this.props.item;
        let text = orderedItem.item.name + ' ' + orderedItem.item.price
        return (
            <ListItem primaryText={text}
                      leftAvatar={orderedItem.user.avatarUrl}/>
        )
    }

}