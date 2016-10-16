import Avatar from '../avatar/avatar.jsx'

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
            <div>
                <button onClick={this.toggleFriends} className="btn btn-default">Add friends</button>
                <div style={{display: 'none'}} id="friendList" className="friend-list">
                    {this.props.friends.map(function (friend) {
                        return <FriendListItem name={friend.name} key={friend.id} imageUrl={friend.avatarUrl}
                                               id={friend.id} click={friendSelected}/>
                    })}
                </div>
            </div>
        )
    },

    toggleFriends() {
        $('#friendList').slideToggle();
    },
})