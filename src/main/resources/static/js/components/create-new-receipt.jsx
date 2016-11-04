import FriendList from "./friend-list/friend-list.jsx";
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
import Divider from 'material-ui/Divider';
import Avatar from 'material-ui/Avatar';
import Chip from 'material-ui/Chip';

export default class CreateNewReceipt extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            friends: [],
            friendsToInvite: [],
            name: '',
            place: '',
            nameError: ''
        }
    }

    render() {
        let alreadyInvitedElement = <div/>;
        let remove = this.removeSelectedFriend.bind(this);
        if (this.state.friendsToInvite.length) {
            alreadyInvitedElement = <div>
                <h4>Invite will be sent to</h4>
                {this.state.friendsToInvite.map(function (user) {
                    return (
                        <Chip
                            onRequestDelete={() => remove(user.id)}>
                            <Avatar src={user.avatarUrl}/>
                            {user.name}
                        </Chip>
                    );
                })}
            </div>
        }
        return (
            <section>
                <TextField hintText="Receipt Name" floatingLabelText="Receipt Name" name="name"
                           onChange={this.updateStateFunction('name')}
                           errorText={this.state.nameError}/><br/>
                <TextField hintText="Place" floatingLabelText="Where are you?" name="place"
                           onChange={this.updateStateFunction('place')}/>
                <div>
                    {alreadyInvitedElement}
                    <FriendList title="Invite friends" friendSelected={this.friendSelected.bind(this)} friends={this.state.friends}/>
                </div>
                <Divider/>
                <RaisedButton label="Create" primary={true} onClick={() => this.createReceipt()}/>
            </section>
        )
    }

    updateStateFunction(key) {
        return (event) => this.setState({[key]: event.target.value.trim()})
    }

    createReceipt() {
        let state = this.state;
        if (!state.name.trim()) {
            this.setState({nameError: 'Please provide name'});
            return
        }
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

    friendSelected(id) {
        let from = this.state.friends.slice();
        let to = this.state.friendsToInvite.slice();
        var found = from.find(u => u.id === id);
        if (!found) {
            return;
        }
        to.push(found);
        from = from.filter(u => u.id !== id);
        this.setState({
            friends: from,
            friendsToInvite: to
        });
    }

    removeSelectedFriend(id) {
        let from = this.state.friendsToInvite.slice();
        let to = this.state.friends.slice();
        var found = from.find(u => u.id === id);
        if (!found) {
            return;
        }
        to.push(found);
        from = from.filter(u => u.id !== id);
        this.setState({
            friends: to,
            friendsToInvite: from
        });
    }

    moveFriend(from, to, id) {
        var found = from.find(u => u.id === id);
        if (!found) {
            return;
        }
        to.push(found);
        from = from.filter(u => u.id !== id);
        this.setState({
            friends: from,
            friendsToInvite: to
        });
    }

    componentWillMount() {
        let t = this;
        $.get('/v1/friends').done(function (r) {
            r.forEach(i => i.id = i.email);
            t.setState({friends: r})
        })
    }

    componentDidMount() {
        this.props.setTitle("Create new receipt");
    }
}

