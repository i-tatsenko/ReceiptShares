import ContentCopy from 'material-ui-icons/ContentCopy'
import Input, {InputAdornment} from 'material-ui/Input';
import {FormControl} from 'material-ui/Form';
import IconButton from 'material-ui/IconButton';
import Snackbar from 'material-ui/Snackbar';

export default class CopyAbleInput extends React.Component {

    constructor(args) {
        super(args);
        this.state = {
            notificationOpen: false
        }
    }

    render() {
        return (
            [
                <FormControl key="control">
                    <Input inputRef={input => {this.input = input; console.log(input)}}
                           value={this.props.value}
                           onClick={this.copyInputValueToClipboard.bind(this)}
                           endAdornment={
                               <InputAdornment position="end">
                                   <IconButton>
                                       <ContentCopy/>
                                   </IconButton>
                               </InputAdornment>
                           }/>
                </FormControl>,
                <Snackbar
                    key="notification"
                    open={this.state.notificationOpen}
                    autoHideDuration={3000}
                    onRequestClose={() => this.setState({notificationOpen: false})}
                    message={<span>Invite link was copied to clipboard!</span>}
                />
            ]
        )
    }

    copyInputValueToClipboard() {
        this.input.select();
        document.execCommand("Copy");
        this.showNotification();
    }

    showNotification() {
        this.setState({notificationOpen: true});
    }
}