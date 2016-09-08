import React from 'react'

export default class Receipt extends React.Component {
    render() {
        return (
            <div>
                <h3>Members</h3>
                <div className="receipt__members">
                    <ul id="receipt-member__ul">

                    </ul>
                </div>
                <h3>Your orders</h3>
            </div>
        )
    }
}