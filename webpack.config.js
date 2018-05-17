const path = require('path');
const webpack = require('webpack');

const outputDir = path.resolve(__dirname, "src/main/resources/static/js/dist/");
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');

module.exports = {
    entry: './src/main/resources/static/js/components/main.jsx',
    mode: "development",
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
                    presets: ['es2015', 'react', 'stage-2']
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
    devtool: "inline-cheap-source-map",

    plugins: [
        new webpack.ProvidePlugin({
            '$': 'jquery',
            'React': 'react',
            'ReactDOM': 'react-dom'
        }),
        // new UglifyJsPlugin({
        //     sourceMap: true,
        //     parallel: true
        // }),
        // new BundleAnalyzerPlugin()
    ],

    devServer: {
        proxy : {
            "/v1" : {
                target: "https://localhost:8043",
                secure: false
            },
            "/signin" : {
                target: "https://localhost:8043",
                secure: false
            },
            "/social" : {
                target: "https://localhost:8043",
                secure: false
            }
        },
        contentBase: [path.join(__dirname, "/src/main/resources/static/html")],
        port: 9000,
        allowedHosts:["www.splitit.cf"],
        publicPath: "/js/dist/"
    }

};
