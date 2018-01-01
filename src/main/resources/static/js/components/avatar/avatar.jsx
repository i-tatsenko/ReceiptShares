import ListItem from 'material-ui/List/ListItem';
import Avatar from 'material-ui/Avatar';
import './avatar.css';
import {withStyles} from "material-ui/styles";
import {imageService} from "../../service/image-service.js"

export default class extends React.Component {
    render() {
        let avatarLink = this.props.avatar || "/images/no-photo-avatar.svg";
        return (
            <ListItem leftAvatar={<Avatar src={avatarLink}/>}>{this.props.name}</ListItem>
        )
    }
}

const styles = {
    bigAvatar: {
        width: 150,
        height: 150,
        borderColor: '#EEEEEE',
        borderStyle: 'solid',
        borderWidth: 7
    }
};

class BigAvatarInner extends React.Component {
    constructor(args) {
        super(args);
        this.classes = args.classes;
        this.state = {
            avatarLink: ''
        }
    }

    render() {
        return (
            <Avatar className={this.classes.bigAvatar + " " + this.props.className} src={this.state.avatarLink}/>
        )
    }

    componentWillMount() {
        imageService.findLargeImageLink(this.props.avatar || "/images/no-photo-avatar.svg")
                    .subscribe(link => this.setState({avatarLink: link}))
    }
}

export const BigAvatar = withStyles(styles)(BigAvatarInner);