import React from 'react'
import ReactDOM from 'react-dom'
import {hashHistory} from 'react-router'
import Header from './header/header.jsx'
import RegistrationForm from './registration-form.jsx'
import LoginForm from './login/login-form.jsx'
import LeftMenu from './left-menu.jsx'
import Receipt from './receipt.jsx'
import Paper from 'material-ui/Paper';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

var ReactRouter = require('react-router');

var Router = ReactRouter.Router;
var Route = ReactRouter.Route;
var IndexRoute = ReactRouter.IndexRoute;

class WelcomePage extends React.Component {
    render() {
        return (
            <h1>Hello in app!</h1>
        )
    }
}

class Help extends React.Component {
    render() {
        return (
            <h1>Application is under construction</h1>
        )
    }
}

class MainPage extends React.Component {

    render() {
        return (
            <div style={{height: '100%'}}>
                <Header user={this.props.user}/>
                <div style={{
                    float: 'left'
                }} className="container-div">
                    <LeftMenu currentLink="/current" helpLink="/help"/>
                </div>
                <div style={{
                    float: 'left',
                    marginLeft: '20px',
                    marginTop: '20px',
                    width: 'auto'
                }} className="container-div">
                    {this.renderChildren()}
                </div>
            </div>
        )
    }

    renderChildren() {
        let user = this.props.user;
        return React.Children.map(this.props.children, child => {
            return React.cloneElement(child, {user: user})
        })
    }

}

class RedirectComponent extends React.Component {
    constructor(args) {
        super(args);
        ReactRouter.hashHistory.push("/");
    }
}

class Mui extends React.Component {
    constructor(args) {
        super(args);
    }

    render() {
        return (
            <MuiThemeProvider>
                <div className="container">
                    <div className="col-lg-6 col-lg-offset-3">
                        <Paper zDepth={3}>

                            {this.props.children}
                        </Paper>
                    </div>
                </div>
            </MuiThemeProvider>
        )
    }
}

function getMainLayout(user) {
    var mainPageWrapper = React.createClass({
        render: function () {
            return (
                <MainPage user={user}>
                    {this.props.children}
                </MainPage>)
        }
    });
    return <Router history={hashHistory}>
        <Route path="/" component={mainPageWrapper}>
            <IndexRoute component={WelcomePage}/>
            <Route path="/register" component={RegistrationForm}/>
            <Route path="/current" component={Receipt}/>
            <Route path="/help" component={Help}/>
            <Route path="/login" component={LoginForm}/>
            <Route path="*" component={RedirectComponent}/>
        </Route>
    </Router>;
}

var loginComponent = React.createClass({
    render() {
        return (<LoginForm loginCallback={() => window.location = '/'}/>);
    }
});

var loginLayout =
    <Router history={hashHistory}>
        <Route path="/" component={Mui}>
            <IndexRoute component={loginComponent}/>
            <Route path="/login" component={loginComponent}/>
            <Route path="/register" component={RegistrationForm}/>
            <Route path="*" component={loginComponent}/>
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
