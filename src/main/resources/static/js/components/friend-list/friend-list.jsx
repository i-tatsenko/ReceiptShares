import {List, ListItem} from 'material-ui/List';
import Avatar from 'material-ui/Avatar';
import Subheader from 'material-ui/Subheader';

import './friend-list.css';

export default class FriendList extends React.Component {

    render() {
        let friendSelected = this.props.friendSelected;
        return (
            <List>
                <Subheader>{this.props.title}</Subheader>
                {this.props.friends.map(friend => {
                    return <ListItem primaryText={friend.name}
                                     insetChildren={true}
                                     onClick={() => friendSelected(friend.id)}
                                     leftAvatar={<Avatar src={friend.avatarUrl}/>}
                                     key={friend.id}
                    />
                })}
            </List>
        )
    }

}