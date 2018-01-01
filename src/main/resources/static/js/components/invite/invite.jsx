import {withRouter} from "react-router-dom";
import {withStyles} from "material-ui/styles"
import {inviteService} from "../../storage/storage.js";
import WaitingData from "../waiting-data.jsx";
import {BigAvatar} from "../avatar/avatar.jsx";
import Button from 'material-ui/Button';
import "./invite.css";

const classes = {
    avatar: {
        marginTop: 50,
        marginBottom: 50
    }
};

class Invite extends React.Component {

    constructor(args) {
        super(args);
        this.classes = args.classes;
        this.state = {}
    }

    render() {
        let invite = this.state.invite;
        if (!invite) {
            return (<WaitingData/>)
        }
        let inviteAuthor = invite.author;
        let inviteAction = this.constructInviteAction(invite);
        console.log(this.classes.avatar);
        return (
            <section className="invite" style={{["--place-url"]: `url(${invite.receipt.place.imageUrl})`}}>
                <BigAvatar avatar={inviteAuthor.avatarUrl} className={this.classes.avatar}/>
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

export default withRouter(withStyles(classes)(Invite))