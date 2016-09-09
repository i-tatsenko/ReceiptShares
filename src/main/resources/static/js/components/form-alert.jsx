import React from 'react'

export default class FormAlert extends React.Component {
    render() {
        var css = {
            display: this.props.message ? 'block' : 'none'
        };
        return (
            <div id="reg-form-alert" className="alert alert-danger" hidden="true" style={css}>
                {this.props.message}
            </div>
        )
    }
}