import React from "react";
import ReactDOM from "react-dom";
import RegistrationForm from "./registration/registration-form.jsx";
import LoginForm from "./login/login-form.jsx";
import Receipt from "./receipt/receipt.jsx";
import Paper from "material-ui/Paper";
import Cookies from "js-cookie"
import {BrowserRouter, Redirect, Route, Switch} from "react-router-dom"
import storage from "../storage/storage.js"
import CreateNewReceipt from './receipt/create-new-receipt.jsx'
import ReceiptList from './receipt/receipt-list.jsx'
import App from './app.jsx'
import NewReceiptMenuItem from "./menu/new-receipt-menu-item.jsx"
import Invite from "./invite/invite.jsx";

let Help = () => <h1>Application is under construction</h1>;

let LoginComponent = () => <LoginForm loginCallback={() => redirectToPreviousLocation()}/>;

storage.addAddActionButtonMenuItem(<NewReceiptMenuItem/>);

class Mui extends React.Component {
    render() {
        return (
            <div className="container-div">
                <Paper elevation={3} style={{minHeight: '90vh'}}>
                    {this.props.children}
                </Paper>
            </div>
        )
    }
}

function getMainLayout(user) {
    storage.setUser(user);
    let AppWrapper = class extends React.Component {

        constructor(args) {
            super(args);
            this.state = {
                barTitle: "Receipt Shares"
            }
        }

        render() {
            return (
                <Mui>
                    <App user={user} barTitle={this.state.barTitle}>
                        {this.props.children}
                    </App>
                </Mui>)
        }
    };

    return <BrowserRouter>
        <AppWrapper>
            <Switch>
                <Route exact path="/" component={ReceiptList}/>
                <Route path="/new" component={CreateNewReceipt}/>
                <Route path="/receipt/invite/:id" component={Invite}/>
                <Route path="/receipt/:id" component={Receipt}/>
                <Route path="/help" component={Help}/>
                <Redirect to={storage.getAndRemoveReturnUrl()} push/>
            </Switch>
        </AppWrapper>
    </BrowserRouter>;
}

let loginLayout =
    <BrowserRouter>
        <Mui>
            <Switch>
                <Route exact path="/" component={LoginComponent}/>
                <Route path="/login" component={LoginComponent}/>
                <Route path="/register" component={RegistrationForm}/>
                <Route path="/receipt/invite/:id" component={Invite}/>
                <Route component={LoginComponent}/>
            </Switch>
        </Mui>
    </BrowserRouter>;


$(document).ajaxSend(function (event, jqXHR) {
    jqXHR.setRequestHeader("X-XSRF-TOKEN", Cookies.get("XSRF-TOKEN"))
});


$(document).ajaxError(function (event, jqxhr, settings, thrownError) {
    console.log(thrownError);
    if (jqxhr.status === 401 && window.location.pathname !== '/login') {
        window.location = "/login"
    }
});

$.get({
    url: '/v1/me',
    success: function (resp) {
        renderApp(getMainLayout(resp));
    }
}).fail(() => {
    if (window.location.pathname !== '/login') {
        storage.saveReturnUrl(window.location.pathname);
    }
    renderApp(loginLayout);
});

function renderApp(app) {
    ReactDOM.render(app, document.getElementById('container'));
}

function redirectToPreviousLocation() {
    return window.location = storage.getAndRemoveReturnUrl();
}
