require('style!css!./avatar.css');

export default React.createClass({
    render() {
        let avatarLink = this.props.avatar || "/no-photo-avatar.svg";
        return(
            <div className="avatar" style={{backgroundImage: 'url(' + avatarLink + ')'}}></div>
        )
    },

})