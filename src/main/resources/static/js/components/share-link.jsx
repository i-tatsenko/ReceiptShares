import IconButton from 'material-ui/IconButton';
import Share from 'material-ui-icons/Share'

class ShareLink extends React.Component {

    render() {
        return (<IconButton onClick={() => this.setState({showShareLink: true})}><Share/></IconButton>)
    }
}