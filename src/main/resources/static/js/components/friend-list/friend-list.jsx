import React from "react"
import List, {ListItem, ListItemIcon, ListItemText} from 'material-ui/List';
import Avatar from 'material-ui/Avatar';
import ListSubheader from 'material-ui/List/ListSubheader';

import './friend-list.css';

export default class FriendList extends React.Component {

    render() {
        let friendSelected = this.props.friendSelected;
        return (
            <List>
                <ListSubheader>{this.props.title}</ListSubheader>
                {this.props.friends.map(friend => {
                    return <ListItem button dense
                                     onClick={() => friendSelected(friend.id)}
                                     key={friend.id}
                    >
                        <ListItemIcon>
                            <Avatar src={friend.avatarUrl}/>
                        </ListItemIcon>
                        <ListItemText primary={friend.name} />
                    </ListItem>
                })}
            </List>
        )
    }

}