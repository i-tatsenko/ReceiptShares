import React from "react"
import storage from "../../storage/storage.js"
import FriendList from "../friend-list/friend-list.jsx";
import Button from 'material-ui/Button';
import Divider from "material-ui/Divider";
import Avatar from "material-ui/Avatar";
import Chip from "material-ui/Chip";
import {chipStyle, chipWrapperStyle} from "../default-styles.jsx";
import Snackbar from "material-ui/Snackbar";
import {withRouter} from "react-router-dom";
import PlaceSelect from "../place/suggest.jsx"
import {FormControl, FormHelperText} from 'material-ui/Form';
import Input, {InputLabel} from 'material-ui/Input';

class CreateNewReceipt extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            friends: [],
            friendsToInvite: [],
            name: '',
            place: null,
            nameError: '',
            error: false,
            autoSuggest: []
        }
    }

    render() {
        let alreadyInvitedElement = <div/>;
        if (this.state.friendsToInvite.length) {
            alreadyInvitedElement = <div>
                <h4>Inviting</h4>
                <div style={chipWrapperStyle}>
                    {this.state.friendsToInvite.map(function (user) {
                        return (
                            <Chip avatar={<Avatar src={user.avatarUrl}/>}
                                  label={user.name}
                                  onRequestDelete={() => this.removeSelectedFriend(user.id)} style={chipStyle}/>
                        );
                    })}
                </div>
            </div>
        }
        return (
            <section>
                <FormControl error={!!this.state.nameError}>
                    <InputLabel html-for="new-receipt-name">Receipt Name</InputLabel>
                    <Input id="new-receipt-name" value={this.state.name} onChange={this.updateStateFunction('name')}/>
                    <FormHelperText>{this.state.nameError}</FormHelperText>
                </FormControl><br/>

                <PlaceSelect selected={suggest=> this.setState({place: suggest})}/>
                <div>
                    {alreadyInvitedElement}
                    <FriendList title="Invite friends" friendSelected={this.friendSelected.bind(this)}
                                friends={this.state.friends}/>
                </div>
                <Divider style={{marginBottom: '20px'}}/>
                <Button raised color="primary" onClick={() => this.createReceipt()}>Create</Button>
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
            place: state.place,
            name: state.name,
            members: state.friendsToInvite.map(friend => friend.id)
        };
        $.ajax({
            url: '/v1/receipt',
            data: JSON.stringify(data),
            contentType: 'application/json',
            dataType: 'json',
            method: 'post',
            success: (resp) => {
                this.props.history.push('/receipt/' + resp.id)
            },
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

