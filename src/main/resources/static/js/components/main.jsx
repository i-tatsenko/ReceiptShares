import React from 'react'
import ReactDOM from 'react-dom'
import { hashHistory } from 'react-router'
import Header from './header.jsx'
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
        return(
            <h1>Application is under construction</h1>
        )
    }
}

class MainPage extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            currentPage: "welcomePage",
            currentUser: null
        }
    }

    showRegister() {
        this.setState({currentPage: "login"})
    }

    render() {
        return (
            <div style={{height: '100%'}}>
                <Header register="/register" login="/login"/>
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
                    {this.props.children}
                </div>
            </div>
        )

    }

}

ReactDOM.render(
    <Router history={hashHistory}>
        <Route path="/" component={MainPage}>
            <IndexRoute component={WelcomePage}/>
            <Route path="/register" component={RegistrationForm}/>
            <Route path="/current" component={Receipt}/>
            <Route path="/help" component={Help}/>
            <Route path="/login" component={LoginForm}/>
        </Route>
    </Router>,
    document.getElementById('container')
);