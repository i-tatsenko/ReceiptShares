import ListItem from 'material-ui/List/ListItem';
import Avatar from 'material-ui/Avatar';
import './avatar.css';

export default class Avatar extends React.Component {
    render() {
        let avatarLink = this.props.avatar || "/no-photo-avatar.svg";
        return (
            <ListItem leftAvatar={<Avatar src={avatarLink}/>}>{this.props.name}</ListItem>
        )
    }

}