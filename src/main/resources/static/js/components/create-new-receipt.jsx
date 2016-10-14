import FriendList from './friend-list/friend-list.jsx'


export default class CreateNewReceipt extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            friends: []
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
                        <FriendList/>
                    </div>
                    <button className="btn btn-default">Create</button>
                </div>
            </section>
        )
    }
}

