import React from "react";
import CreateNewReceipt from "./create-new-receipt.jsx";

export default class Receipt extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            rec: null
        }
    }

    render() {

    }

    componentDidMount() {
        $.get('/v1/rec/' + this.props.id, resp => this.setState({rec: resp}))
    }
}