import {List, ListItem} from 'material-ui/List';
import Divider from 'material-ui/Divider';
import Avatar from 'material-ui/Avatar';
import Subheader from 'material-ui/Subheader';

require('style!css!./friend-list.css');

class FriendListItem extends React.Component {
    render() {
        let id = 'item' + this.props.id;
        return (
            <div id={id} className="friendListItem" onClick={() => this.props.click(this.props.id)}>
                <div className="friend-list__friend-item clearfix">
                    <Avatar avatar={this.props.imageUrl}/>
                    <h4>{this.props.name}</h4>
                </div>
            </div>
        )
    }
}

export default React.createClass({
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
    },

    toggleFriends() {
        $('#friendList').slideToggle();
    },
})