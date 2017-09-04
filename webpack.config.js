const path = require('path');
const webpack = require('webpack');
const WebpackShellPlugin = require('webpack-shell-plugin');

const outputDir = path.resolve(__dirname, "src/main/resources/static/js/dist/");
const ideaOutFile = path.resolve(__dirname, "out/production/resources/static/js/dist/");

module.exports = {
    entry: './src/main/resources/static/js/components/main.jsx',
    output: {
        filename: 'bundle.js',
        path: outputDir
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
        }),
        new WebpackShellPlugin({
            onBuildExit: [`echo cp ${outputDir}/bundle.js ${ideaOutFile}/`,`cp ${outputDir}/bundle.js ${ideaOutFile}/bundle.js`]
        })
    ]

};
