import Avatar from '../avatar/avatar.jsx'

require('style!css!./friend-list.css');

class FriendListItem extends React.Component {
    render() {
        let id = 'item' + this.props.id;
        return (
            <div id={id} className="friendListItem">
                <div className="friend-list__friend-item clearfix">
                    <Avatar avatar={this.props.imageUrl}/>
                    <h4>{this.props.name}</h4>
                </div>
            </div>
        )
    }
}

export default React.createClass({

    getInitialState() {
        return {
            friends: []
        }
    },

    render() {
        return (
            <div>
                <button onClick={this.toggleFriends}>Add friends</button>
                <div style={{display: 'none'}} id="friendList">
                    {this.state.friends.map(function (friend) {
                        return <FriendListItem name={friend.name} key={friend.id} imageUrl={friend.image}
                                               id={friend.id}/>
                    })}
                </div>
            </div>
        )
    },

    toggleFriends() {
        $('#friendList').slideToggle();
    },

    componentWillMount() {
        let t = this;
        $.get('/v1/friends').done(function (r) {
            r.forEach(i => i.id = i.email);
            t.setState({friends: r})
        })
    }
})