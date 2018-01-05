import React from "react"
import IconButton from 'material-ui/IconButton';
import Share from 'material-ui-icons/Share'
import CopyAbleInput from './input-copy-able.jsx'

class ShareLink extends React.Component {

    constructor(args) {
        super(args);
        this.state = {showShareLink: false}
    }

    render() {
        let linkText = null;
        if (this.state.showShareLink) {
            linkText = <CopyAbleInput value={this.props.link}/>

        }
        return (<div className={this.props.className}>
                {linkText}
                <IconButton onClick={() => this.setState({showShareLink: !this.state.showShareLink})}><Share/>
                </IconButton>
            </div>
        )
    }
}

export default ShareLink;