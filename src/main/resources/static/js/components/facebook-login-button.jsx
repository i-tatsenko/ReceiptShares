import React from 'react';

export default class FbLoginButton extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            scriptReady: false

        }
    }

    render() {
        console.log("State: " + JSON.stringify(this.state));
        return (
            <form id="signInFacebook" action="/signin/facebook" method="POST" onSubmit={this.setCsrfToken}>
                <button type="submit">Login FB</button>
                <input type="hidden" name="_csrf"/>
            </form>
        );
    }

    setCsrfToken() {
        document.forms['signInFacebook']['_csrf'].value = Cookies.get("XSRF-TOKEN")
    }


}
