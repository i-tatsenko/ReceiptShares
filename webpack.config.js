const path = require('path');
var webpack = require('webpack');

module.exports = {
    entry: './src/main/resources/static/js/components/main.jsx',
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, "src/main/resources/static/js/")
    },
    module: {
        rules: [
            {
                test: /.jsx?$/,
                loader: 'babel-loader',
                exclude: /node_modules/,
                query: {
                    presets: ['es2015', 'react']
                }
            },
            {
                test:/\.css$/,
                use: [
                    'style-loader',
                    'css-loader'
                ]
            }
        ]
    },


    plugins: [
        new webpack.ProvidePlugin({
            '$': 'jquery',
            'React': 'react',
            'ReactDOM': 'react-dom'
        })
    ]

};
