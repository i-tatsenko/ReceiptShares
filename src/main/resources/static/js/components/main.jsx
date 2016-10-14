import React from 'react'
import ReactDOM from 'react-dom'
import {hashHistory} from 'react-router'
import Header from './header/header.jsx'
import RegistrationForm from './registration-form.jsx'
import LoginForm from './login-form.jsx'
import LeftMenu from './left-menu.jsx'
import Receipt from './receipt.jsx'

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
                <Header register="/register" login="/login" user={this.props.user}/>
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
        </Route>
    </Router>;
}

var loginLayout =
    <Router history={hashHistory}>
        <Route path="/" component={LoginForm}/>
        <Route path="/login" component={LoginForm}/>
        <Route path="/register" component={RegistrationForm}/>
    </Router>;

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
