import ListItem from 'material-ui/List/ListItem';
import Avatar from 'material-ui/Avatar';


require('style!css!./avatar.css');

export default React.createClass({
    render() {
        let avatarLink = this.props.avatar || "/no-photo-avatar.svg";
        return(
            <ListItem leftAvatar={<Avatar src={avatarLink}/>}>{this.props.name}</ListItem>
        )
    },

})