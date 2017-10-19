import location from '../../service/location.js'
import {List, ListItem} from 'material-ui/List';

export default class extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            suggestList: []
        }
    }

    render() {
        if (!this.props.searchString) {
            return null;
        }
        return (
            <List style={{float: 'left', width: '100%'}}>
                {this.state.suggestList.map(item => <ListItem key={item.id} primaryText={item.name}
                                                              secondaryText={item.location.address}/>)}
            </List>
        )
    }

    componentWillReceiveProps(nextProps) {
        if (!this.props.searchString !== nextProps.searchString) {
            this.updateSuggestList(nextProps.searchString)
        }
    }

    componentWillMount() {
        if (this.props.searchString) {
            this.updateSuggestList(this.props.searchString);
        }
    }

    updateSuggestList(searchQuery) {
        location.getPlacesNearWithName(searchQuery, (res) => this.setState({suggestList: res}));
    }
}