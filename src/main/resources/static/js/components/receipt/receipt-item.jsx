import {ListItem} from "material-ui/List";
import Avatar from "material-ui/Avatar";


export default class ReceiptItem extends React.Component {

    render() {
        let orderedItem = this.props.item;
        let text = orderedItem.item.name + ' ' + orderedItem.sum;
        return (
            <ListItem primaryText={text}
                      leftAvatar={<Avatar src={orderedItem.user.avatarUrl}/>}
                      rightToggle={<div>{orderedItem.count}</div>}
            />
        )
    }

}