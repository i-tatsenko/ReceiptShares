import FriendList from './friend-list/friend-list.jsx';
import Avatar from './avatar/avatar.jsx';

export default class CreateNewReceipt extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            friends: [],
            friendsToInvite: []
        }
    }

    render() {
        let alreadyInvitedElement = <div/>;
        if (this.state.friendsToInvite.length) {
            alreadyInvitedElement = <div>
                <h4>Invite will be sent to</h4>
                {this.state.friendsToInvite.map(function (user) {
                    return (<Avatar avatar={user.avatarUrl}/>);
                })}
            </div>
        }
        return (
            <section className="panel panel-primary">
                <div className="panel-heading"> Create new receipt</div>
                <div className="panel-body">
                    <div className="form-group"><label>Receipt name
                        <input type="text" className="form-control" name="name"/></label>
                    </div>
                    <div className="form-group"><label>Place
                        <input type="text" className="form-control" name="place"/></label>
                    </div>
                    <div className="form-group">
                        <h2>Invite friends</h2>
                        {alreadyInvitedElement}
                        <FriendList friendSelected={this.friendSelected.bind(this)} friends={this.state.friends}/>
                    </div>
                    <button className="btn btn-default">Create</button>
                </div>
            </section>
        )
    }

    friendSelected(userId) {
        let friends = this.state.friends.slice();
        let toInvite = this.state.friendsToInvite.slice();
        var selectedFriend = friends.find(u => u.id === userId);
        if (!selectedFriend) {
            return;
        }
        toInvite.push(selectedFriend);
        friends = friends.filter(u => u.id !== userId);
        this.setState({
            friends,
            friendsToInvite: toInvite
        });
    }

    componentWillMount() {
        let t = this;
        $.get('/v1/friends').done(function (r) {
            r.forEach(i => i.id = i.email);
            t.setState({friends: r})
        })
    }
}

