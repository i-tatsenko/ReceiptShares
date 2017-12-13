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
        return (
            <section className="invite" style={{["--place-url"]: `url(${invite.receipt.place.imageUrl})`}}>
                <Avatar src={inviteAuthor.avatarUrl} className="invite-owner__avatar"/>
                <div className="invite-owner__name">{inviteAuthor.name}</div>
                <div className="invite-text"> invites you to join him in </div>
                <div className="invite-place__name">{invite.receipt.place.name}</div>
                <Button className="invite-accept" raised color="primary">Accept</Button>
            </section>
        )
    }

    componentWillMount() {
        inviteService.findById(this.props.match.params.id)
            .subscribe(invite => this.setState({invite}), error => {
                this.setState({error});
                console.log(error)
                //TODO add error handling
            })
    }
}

export default withRouter(Invite)