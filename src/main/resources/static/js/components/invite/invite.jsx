import {withRouter} from "react-router-dom";
import {inviteService} from "../../storage/storage.js";
import Avatar from 'material-ui/Avatar';
import WaitingData from "../waiting-data.jsx";
import Button from 'material-ui/Button';
import "./invite.css";

class Invite extends React.Component {

    constructor(args) {
        super(args);
        this.state = {}
    }

    render() {
        let invite = this.state.invite;
        if (!invite) {
            return (<WaitingData/>)
        }
        let inviteAuthor = invite.author;
        let inviteAction = this.constructInviteAction(invite);
        return (
            <section className="invite" style={{["--place-url"]: `url(${invite.receipt.place.imageUrl})`}}>
                <Avatar src={inviteAuthor.avatarUrl} className="invite-owner__avatar"/>
                <div className="invite-owner__name">{inviteAuthor.name}</div>
                <div className="invite-text"> invites you to join him in</div>
                <div className="invite-place__name">{invite.receipt.place.name}</div>
                <Button className="invite-accept" raised color="primary"
                        onClick={inviteAction.action}>{inviteAction.name}</Button>
            </section>
        )
    }

    constructInviteAction(invite) {
        let name, action;
        if (invite.alreadyAccepted) {
            name = "Go to Receipt";
            action = () => this.moveToReceipt(invite.receipt.id);
        } else {
            name = "Accept";
            action = () => this.acceptInvite(invite.id);
        }
        return {
            name, action
        }
    }

    componentWillMount() {
        inviteService.findById(this.props.match.params.id)
                     .subscribe(invite => this.setState({invite}), error => {
                         this.setState({error});
                         console.log(error)
                         //TODO add error handling
                     })
    }

    acceptInvite(inviteId) {
        inviteService.accept(inviteId)
                     .subscribe(receipt => this.moveToReceipt(receipt.id),
                         error => console.log(error));
        //TODO add error handling
    }

    moveToReceipt(receiptId) {
        this.props.history.push(`/receipt/${receiptId}`);
    }
}

export default withRouter(Invite)