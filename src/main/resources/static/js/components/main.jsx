import React from 'react'
import ReactDOM from 'react-dom'
// import ReactRouter from 'react-router'

import Header from './header.jsx'
import RegistrationForm from './registration-form.jsx'

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
            <div>
                <Header register="/register" login="/login"/>
                <div>
                    {this.props.children}
                </div>
            </div>
        )

    }

}

ReactDOM.render(
    <Router>
        <Route path="/" component={MainPage}>
            <IndexRoute component={WelcomePage}/>
            <Route path="/register" component={RegistrationForm}/>
        </Route>
    </Router>,
    document.getElementById('container')
);