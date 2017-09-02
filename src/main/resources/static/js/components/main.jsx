import React from "react";
import ReactDOM from "react-dom";
import {browserHistory} from "react-router";
import RegistrationForm from "./registration/registration-form.jsx";
import LoginForm from "./login/login-form.jsx";
import Receipt from "./receipt/receipt.jsx";
import Paper from "material-ui/Paper";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import Cookies from "js-cookie"

import injectTapEventPlugin from "react-tap-event-plugin";
import {Router, Route, IndexRoute} from 'react-router'
import CreateNewReceipt from './receipt/create-new-receipt.jsx'
import ReceiptList from './receipt/receipt-list.jsx'
import App from './app.jsx'

injectTapEventPlugin();

let Help = () => <h1>Application is under construction</h1>;
let LoginComponent = () => <LoginForm loginCallback={() => window.location = '/'}/>;


class RedirectComponent extends React.Component {
    constructor(args) {
        super(args);
        ReactRouter.browserHistory.push("/");
    }
}

class Mui extends React.Component {
    render() {
        return (
            <MuiThemeProvider>
                <div className="container-div">
                    <div id="main-component" className="col-lg-6 col-lg-offset-3 container-div">
                        <Paper zDepth={3} className="container-div">
                            {this.props.children}
                        </Paper>
                    </div>
                </div>
            </MuiThemeProvider>
        )
    }
}

function getMainLayout(user) {
    var AppWrapper = React.createClass({
        render: function () {
            return (
                <Mui>
                    <App user={user}>
                        {this.props.children}
                    </App>
                </Mui>)
        }
    });
    return <Router history={browserHistory}>
        <Route path="/" component={AppWrapper}>
            <IndexRoute component={ReceiptList}/>
            <Route path="/new" component={CreateNewReceipt}/>
            <Route path="/receipt/:id" component={Receipt}/>
            <Route path="/help" component={Help}/>
            <Route path="*" component={RedirectComponent}/>
        </Route>
    </Router>;
}


var loginLayout =
    <Router history={browserHistory}>
        <Route path="/" component={Mui}>
            <IndexRoute component={LoginComponent}/>
            <Route path="/login" component={LoginComponent}/>
            <Route path="/register" component={RegistrationForm}/>
            <Route path="*" component={LoginComponent}/>
        </Route>
    </Router>;

$(document).ajaxSend(function (event, jqXHR) {
    jqXHR.setRequestHeader("X-XSRF-TOKEN", Cookies.get("XSRF-TOKEN"))
});

$.get({
    url: '/v1/me',
    success: function (resp) {
        renderApp(getMainLayout(resp));
    }
}).fail(() => {
    renderApp(loginLayout);
});

function renderApp(app) {
    ReactDOM.render(app, document.getElementById('container'));
}
