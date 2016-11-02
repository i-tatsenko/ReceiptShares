import FriendList from "./friend-list/friend-list.jsx";
import Avatar from "./avatar/avatar.jsx";

export default class CreateNewReceipt extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            friends: [],
            friendsToInvite: [],
            name: '',
            place: ''
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
                        <input type="text" className="form-control" name="name"
                               onChange={this.updateStateFunction('name')}/></label>
                    </div>
                    <div className="form-group"><label>Place
                        <input type="text" className="form-control" name="place"
                               onChange={this.updateStateFunction('place')}/></label>
                    </div>
                    <div className="form-group">
                        <h2>Invite friends</h2>
                        {alreadyInvitedElement}
                        <FriendList friendSelected={this.friendSelected.bind(this)} friends={this.state.friends}/>
                    </div>
                    <button className="btn btn-default  " onClick={() => this.createReceipt()}>Create</button>
                </div>
            </section>
        )
    }

    updateStateFunction(key) {
        return (event) => this.setState({[key]: event.target.value})
    }

    createReceipt() {
        let state = this.state;
        let data = {
            place: {
                name: state.place
            },
            name: state.name,
            members: state.friends.map(val => Object.create({id: val}))
        };
        $.ajax({
            url: '/v1/rec/create',
            data: JSON.stringify(data),
            contentType: 'application/json',
            dataType: 'json',
            method: 'post',
            success: () => alert('created!'),
            error: () => alert('failed!')
        })

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

