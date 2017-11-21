import ContentCopy from 'material-ui-icons/ContentCopy'
import Input, {InputAdornment} from 'material-ui/Input';
import {FormControl} from 'material-ui/Form';
import IconButton from 'material-ui/IconButton';


export default class CopyAbleInput extends React.Component {

    render() {
        return (
            <FormControl>
                <Input value={this.props.value} endAdornment={
                    <InputAdornment position="end">
                        <IconButton>
                            <ContentCopy/>
                        </IconButton>
                    </InputAdornment>
                }/>
            </FormControl>
        )
    }
}