import React from 'react'

export default class CreateNewReceipt extends React.Component {

    render() {
        return (
            <section className="panel panel-primary">
                <div className="panel-heading"> Create new receipt</div>
                <div className="panel-body">
                    <div className="form-group"><label>Receipt name
                        <input type="text" className="form-control" name="name"/></label>
                    </div>
                    <div className="form-group"><label>Place
                        <input type="text" className="form-control" name="place"/></label>
                    </div>
                    <div className="form-group">
                        <h2>Invite friends</h2>
                        <select className="form-control">
                            <option>Friend 1</option>
                            <option>Friend 2</option>
                            <option>Friend 3</option>
                        </select>
                    </div>
                    <button className="btn btn-default">Create</button>
                </div>
            </section>
        )
    }
}