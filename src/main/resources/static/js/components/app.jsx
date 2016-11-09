import LeftMenu from "./left-menu.jsx";
import FloatingActionButton from "material-ui/FloatingActionButton";
import ContentAdd from "material-ui/svg-icons/content/add";
import IconMenu from "material-ui/IconMenu";
import MenuItem from "material-ui/MenuItem";
import AppBar from 'material-ui/AppBar';
import Logout from './login/logout.jsx'
import {browserHistory} from "react-router";


export default class App extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            menuOpen: false,
            barTitle: 'Receipt Shares',
            receiptsList: null,
            menuItems: {
                Receipts: '/',
                Help: '/help',
                logout: <Logout/>
            }
        }
    }

    componentWillMount() {
        let t = this;
        $.get('/v1/rec/all').done((resp) => t.setState({receiptsList: resp}));
    }


    render() {
        let ActionButton = () => <IconMenu
            style={{
                position: 'absolute',
                right: '30px',
                bottom: '20px'
            }}
            iconButtonElement={
                <FloatingActionButton zDepth={3}>
                    <ContentAdd/>
                </FloatingActionButton>
            }
            onItemTouchTap={(event, item)=> {
                console.log(item);
                browserHistory.push('/new')
            }}
            anchorOrigin={{horizontal: 'left', vertical: 'top'}}
            targetOrigin={{horizontal: 'middle', vertical: 'bottom'}}>
            <MenuItem primaryText="New receipt"/>
        </IconMenu>;

        return (
            <section>
                <div className="clearfix">
                    <AppBar title={this.state.barTitle}
                            onLeftIconButtonTouchTap={() => this.setState({menuOpen: !this.state.menuOpen})}/>
                    <LeftMenu open={this.state.menuOpen} links={this.state.menuItems}
                              closeMenu={() => this.setState({menuOpen: false})}/>
                    <div style={{
                        marginLeft: '20px',
                        marginTop: '20px',
                    }} className="container-div">
                        {this.renderChildren()}
                    </div>
                </div>
                <ActionButton/>
            </section>
        )
    }

    renderChildren() {
        let t = this;
        let user = this.props.user;
        let receiptsList = this.state.receiptsList;
        return React.Children.map(this.props.children, child => {
            return React.cloneElement(child, {
                    user,
                    receiptsList,
                    setTitle: function (title) {
                        t.setState({barTitle: title})
                    }
                }
            )
        })
    }
}
