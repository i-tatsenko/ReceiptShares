import LeftMenu from "./left-menu.jsx";
import FloatingActionButton from "material-ui/FloatingActionButton";
import ContentAdd from "material-ui/svg-icons/content/add";
import IconMenu from "material-ui/IconMenu";
import MenuItem from "material-ui/MenuItem";
import AppBar from "material-ui/AppBar";
import Logout from "./login/logout.jsx";
import {withRouter} from "react-router-dom";


class App extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            menuOpen: false,
            menuItems: {
                Receipts: '/',
                Help: '/help',
                logout: <Logout/>
            },
            additionalMenuItems: [],
            actionItems: {
                "New receipt": () => this.props.history.push('/new')
            }
        }
    }

    render() {
        let actions = this.state.additionalMenuItems;
        actions.push({name: "New receipt", action: () => this.props.history.push('/new')});
        let menuItems = actions.map(action => <MenuItem primaryText={action.name} onTouchTap={action.action} key={action.name}/>);

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
            {menuItems}
        </IconMenu>;

        return (
            <section>
                <div className="clearfix" style={{position: "relative"}}>
                    <AppBar title={this.props.barTitle}
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
        return React.Children.map(this.props.children, child => {
            return React.cloneElement(child, {
                    addMenuItems: function (items) {
                        let menuItems = t.state.additionalMenuItems;
                        menuItems.unshift(...items);
                        t.setState({additionalMenuItems: menuItems})
                    },
                    removeMenuItems: function (itemNames) {
                        let menuItems = t.state.additionalMenuItems;
                        menuItems = menuItems.filter(item => !itemNames.includes(item.name));
                        t.setState({additionalMenuItems: menuItems});
                    }
                }
            )
        })
    }
}

export default withRouter(App)