import React from "react";
import CurrentReceipt from "./receipt-card.jsx";
import CreateNewReceipt from "./create-new-receipt.jsx";

export default class Receipt extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            rec: ''
        }
    }

    render() {
        var child;
        if (this.state.rec) {
            child = <CurrentReceipt receipt={this.state.rec}/>
        } else {
            child = <CreateNewReceipt {...this.props}/>
        }
        return (
            <section>
                {child}
            </section>
        )
    }

    componentDidMount() {
        // $.get('/v1/rec/current', resp => this.setState({rec: resp}))
    }
}