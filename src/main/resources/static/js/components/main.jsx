import React from "react";
import ReactDOM from "react-dom";
import RegistrationForm from "./registration/registration-form.jsx";
import LoginForm from "./login/login-form.jsx";
import Receipt from "./receipt/receipt.jsx";
import Paper from "material-ui/Paper";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import Cookies from "js-cookie"
import {BrowserRouter, Redirect, Route, Switch} from "react-router-dom"

import injectTapEventPlugin from "react-tap-event-plugin";
import CreateNewReceipt from './receipt/create-new-receipt.jsx'
import ReceiptList from './receipt/receipt-list.jsx'
import App from './app.jsx'

injectTapEventPlugin();

let Help = () => <h1>Application is under construction</h1>;
let LoginComponent = () => <LoginForm loginCallback={() => window.location = '/'}/>;

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

        getInitialState() {
            return {
                barTitle: "Receipt Shares"
            }
        },

        render: function () {
            return (
                <Mui>
                    <App user={user} barTitle={this.state.barTitle}>
                        {this.props.children}
                    </App>
                </Mui>)
        }
    });

    function setTitle(title) {
        AppWrapper.setState({barTitle: title});
    }

    return <BrowserRouter>
        <AppWrapper>
            <Switch>
                <Route exact path="/" component={props => <ReceiptList {...props} user={user} setTitle={setTitle}/>} />
                <Route path="/new" component={props => <CreateNewReceipt {...props} user={user} setTitle={setTitle}/>}/>
                <Route path="/receipt/:id" component={props => <Receipt {...props} user={user} setTitle={setTitle}/>}/>
                <Route path="/help" component={props => <Help {...props} user={user} setTitle={setTitle}/>}/>
                <Redirect to="/" push/>
            </Switch>
        </AppWrapper>
    </BrowserRouter>;
}


var loginLayout =
    <BrowserRouter>
        <Mui>
            <Switch>
                <Route exact path="/" component={LoginComponent}/>
                <Route path="/login" component={LoginComponent}/>
                <Route path="/register" component={RegistrationForm}/>
                <Route component={LoginComponent}/>
            </Switch>
        </Mui>
    </BrowserRouter>;


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
