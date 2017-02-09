import LeftMenu from "./left-menu.jsx";
import FloatingActionButton from "material-ui/FloatingActionButton";
import ContentAdd from "material-ui/svg-icons/content/add";
import IconMenu from "material-ui/IconMenu";
import MenuItem from "material-ui/MenuItem";
import AppBar from "material-ui/AppBar";
import Logout from "./login/logout.jsx";
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
            },
            additionalMenuItems: [],
            actionItems: {
                "New receipt": () => browserHistory.push('/new')
            }
        }
    }

    componentWillMount() {
        let t = this;
        $.get('/v1/rec/all').done((resp) => t.setState({receiptsList: resp}));
    }


    render() {
        let actions = [];
        for (let menuItem of this.state.additionalMenuItems) {
            let menuItemNameName = menuItem.name;
            actions.push(<MenuItem primaryText={menuItemNameName}
                                   onTouchTap={() => menuItem.action()}
                                   key={menuItemNameName}/>);
        }
        actions.push(<MenuItem primaryText={"New receipt"}
                               onTouchTap={() => browserHistory.push('/new')}/>);
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
            anchorOrigin={{horizontal: 'left', vertical: 'top'}}
            targetOrigin={{horizontal: 'middle', vertical: 'bottom'}}>
            {actions}
        </IconMenu>;

        return (
            <section>
                <div className="clearfix" style={{position: "relative"}}>
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
                    },
                    addMenuItems: function (items) {
                        let menuItems = t.state.additionalMenuItems;
                        menuItems.unshift(...items);
                        t.setState({additionalMenuItems: menuItems})
                    },
                    removeMenuItems: function (itemNames) {
                        let menuItems = t.state.additionalMenuItems;
                        menuItems = menuItems.filter(item => !itemNames.contains(item.name));
                        t.setState({additionalMenuItems: menuItems});
                    }
                }
            )
        })
    }
}
