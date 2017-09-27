export default class Logout extends React.Component {

    render() {
        return (
            <div onClick={this.logout}>
                Logout
            </div>
        )
    }

    logout() {
        $.post('/v1/open/logout').done(window.location = '/');
    }
}