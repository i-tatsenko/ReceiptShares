import React from 'react'

export default class Receipt extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            rec: ''
        }
    }
    render() {
        return (
            <div>
                <h3>Members</h3>
                <div className="receipt__members">
                    <h5>{this.props.rec}</h5>
                    <ul id="receipt-member__ul">

                    </ul>
                </div>
                <h3>Your orders</h3>
            </div>
        )
    }

    componentDidMount() {
        $.get({
            url: "/v1/rec/current",
            success: (resp) => this.setState({rec: resp})
        })
    }
}