import React from 'react'


class FriendListItem extends React.Component {
    render() {
        return (
            <div>
                <i className='fa fa-user' aria-hidden='true'/><span>{this.props.name}</span>
            </div>
        )
    }
}

export default class CreateNewReceipt extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            friends: [{name: "Vaisa Ivasiuta", id: 1}, {name: "Petia Petrov", id: 2}]
        }
    }

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
                        <div className="dropdown">
                            <button id="addFriends" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                Add Friends
                            </button>
                            <ul className="dropdown-menu" aria-labelledby="addFriends">
                                {this.state.friends.map(function (friend) {
                                    return <FriendListItem name={friend.name} key={friend.id}/>
                                })}
                            </ul>
                        </div>
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

