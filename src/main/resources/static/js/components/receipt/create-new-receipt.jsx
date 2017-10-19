import storage from "../../storage/storage.js"
import FriendList from "../friend-list/friend-list.jsx";
import TextField from "material-ui/TextField";
import Button from 'material-ui/Button';
import Divider from "material-ui/Divider";
import Avatar from "material-ui/Avatar";
import Chip from "material-ui/Chip";
import {chipStyle, chipWrapperStyle} from "../default-styles.jsx";
import Snackbar from "material-ui/Snackbar";
import {withRouter} from "react-router-dom";
import location from "../../service/location.js"
import PlaceSuggest from "../place/suggest.jsx"

class CreateNewReceipt extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            friends: [],
            friendsToInvite: [],
            name: '',
            place: '',
            nameError: '',
            error: false,
            autoSuggest: []
        }
    }

    render() {
        let alreadyInvitedElement = <div/>;
        let remove = this.removeSelectedFriend.bind(this);
        if (this.state.friendsToInvite.length) {
            alreadyInvitedElement = <div>
                <h4>Invite will be sent to</h4>
                <div style={chipWrapperStyle}>
                    {this.state.friendsToInvite.map(function (user) {
                        return (
                            <Chip onRequestDelete={() => remove(user.id)} style={chipStyle}>
                                <Avatar src={user.avatarUrl}/>
                                {user.name}
                            </Chip>
                        );
                    })}
                </div>
            </div>
        }
        return (
            <section>
                <TextField label="Receipt Name" placeholder="Receipt Name" name="name"
                           onChange={this.updateStateFunction('name')}
                           errorText={this.state.nameError}/><br/>
                <TextField label="Place" placeholder="Where are you?" name="place"
                           onChange={this.updateStateFunction('place')}/>
                <PlaceSuggest searchString={this.state.place}/>
                <div>
                    {alreadyInvitedElement}
                    <FriendList title="Invite friends" friendSelected={this.friendSelected.bind(this)}
                                friends={this.state.friends}/>
                </div>
                <Divider/>
                <Button raised label="Create" primary={true} onClick={() => this.createReceipt()}/>
                <Snackbar
                    open={this.state.error}
                    message="Can't create receipt. Please try one more time"
                    autoHideDuration={4000}
                    onRequestClose={() => this.setState({error: false})}
                />
            </section>
        )
    }

    updateStateFunction(key) {
        return (event) => {
            let newValue = event.target.value.trim();
            if (key === "place") {
                location.getPlacesNearWithName(newValue, (res) => this.setState({autoSuggest: res}));
            }
            this.setState({[key]: newValue})
        }
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
            members: state.friendsToInvite.map(friend => friend.id)
        };
        $.ajax({
            url: '/v1/receipt',
            data: JSON.stringify(data),
            contentType: 'application/json',
            dataType: 'json',
            method: 'post',
            success: (resp) => {this.props.history.push('/receipt/' + resp.id)},
            error: () => this.setState({error: true})
        })

    }

    friendSelected(id) {
        let from = this.state.friends.slice();
        let to = this.state.friendsToInvite.slice();
        let found = from.find(u => u.id === id);
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
        let found = from.find(u => u.id === id);
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

    componentWillMount() {
        let t = this;
        $.get('/v1/friends').done(function (r) {
            t.setState({friends: r})
        })
    }

    componentDidMount() {
        storage.screenTitle("Create new receipt");
    }
}

export default withRouter(CreateNewReceipt)

