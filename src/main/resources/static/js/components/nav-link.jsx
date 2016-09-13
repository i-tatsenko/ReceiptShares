import {Link, IndexLink} from 'react-router'
import React from 'react'

export default React.createClass({
    contextTypes: {
        router: React.PropTypes.object
    },

    render: function () {
        let isActive = this.context.router.isActive(this.props.to, true);
        let link = this.props.isIndexLink ?
            <Link {...this.props}>{this.props.children}</Link>
            : <IndexLink {...this.props}>{this.props.children}</IndexLink>;
        let className = isActive ? this.props.activeClassName : '';

        return (
            <li className={className} role="presentation">
                {link}
            </li>
        )
    }
})