import Grid from 'material-ui/Grid';
import PropTypes from 'prop-types';

export default class MainLayoutWrapper extends React.Component {

    render() {
        return (
            <Grid item xs={12} md={10} lg={6} style={{
                width: '100%',
                display: 'flex',
                flexDirection: 'column'
            }} className={this.props.className}>
                {this.props.children}
            </Grid>
        )
    }
}

MainLayoutWrapper.propTypes = {
    children: PropTypes.any.isRequired,
    className: PropTypes.string
};