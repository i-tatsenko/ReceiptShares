import React from 'react'

export default class CurrentReceipt extends React.Component {

    render() {
        return (
            <div className="panel panel-primary">
                <div className="panel-heading">Current Receipt</div>
                <div className="panel-body">
                    <h3>Members</h3>
                    <div className="receipt__members">
                        <h5>{this.props.rec}</h5>
                        <ul id="receipt-member__ul">

                        </ul>
                    </div>
                    <h3>Your orders</h3>
                </div>
            </div>
        )
    }
}