import React from 'react';
import IconButton from 'material-ui/IconButton';
import Cookies from 'js-cookie'

export default class FbLoginButton extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            scriptReady: false

        }
    }

    render() {
        let medium = {
            width: 200,
            height: 200,
            padding: 24,
            paddingTop: 0
        };
        return (
            <form id="signInFacebook" action="/signin/facebook" method="POST" onSubmit={this.setCsrfToken}>
                <IconButton style={medium} type="submit">
                    <svg width="1792" height="1792" viewBox="0 0 1792 1792" xmlns="http://www.w3.org/2000/svg"
                    style={{height: "200px"}}>
                        <path stroke="rgb(66,103,178)" fill="rgb(66,103,178)"
                            d="M1579 128q35 0 60 25t25 60v1366q0 35-25 60t-60 25h-391v-595h199l30-232h-229v-148q0-56 23.5-84t91.5-28l122-1v-207q-63-9-178-9-136 0-217.5 80t-81.5 226v171h-200v232h200v595h-735q-35 0-60-25t-25-60v-1366q0-35 25-60t60-25h1366z"/>
                    </svg>
                </IconButton>
                <input type="hidden" name="_csrf"/>
            </form>
        );
    }

    setCsrfToken() {
        document.forms['signInFacebook']['_csrf'].value = Cookies.get("X-CSRF-TOKEN")
    }


}
