var path = require('path');
var webpack = require('webpack');

module.exports = {
    entry: './src/main/resources/static/js/components/main.jsx',
    output: {path: __dirname, filename: 'src/main/resources/static/js/bundle.js'},
    module: {
        loaders: [
            {
                test: /.jsx?$/,
                loader: 'babel-loader',
                exclude: /node_modules/,
                query: {
                    presets: ['es2015', 'react']
                }
            }
        ],
    },

    plugins: [
        new webpack.ProvidePlugin({
            '$': 'jquery',
            'React': 'react',
            'ReactDOM': 'react-dom'
        })
    ]

};
