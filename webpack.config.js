const path = require('path');
const webpack = require('webpack');
const WebpackShellPlugin = require('webpack-shell-plugin');

const outputDir = path.resolve(__dirname, "src/main/resources/static/js/dist/");
const ideaOutFile = path.resolve(__dirname, "out/production/resources/static/js/dist/");
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');

module.exports = {
    entry: './src/main/resources/static/js/components/main.jsx',
    output: {
        filename: 'bundle.js',
        path: outputDir
    },

    resolve: {
        extensions: [".ts", ".tsx", ".js", ".jsx"]
    },

    module: {
        rules: [{
            test: /\.(t|j)sx?$/,
            use: {
                loader: 'awesome-typescript-loader'
            },
            exclude: /node_modules/
        },

            {
                test: /\.css$/,
                use: [
                    'style-loader',
                    'css-loader'
                ],
                exclude: /node_modules/
            },
            {
                enforce: "pre",
                test: /\.js$/, loader: "source-map-loader",
                exclude: /node_modules/
            }
        ]
    },
    externals: {
        // "react": "React",
        // "react-dom": "ReactDOM",
    },
    devtool: "source-map",

    plugins: [
        function () {
            this.plugin("watch-run", function (watching, callback) {
                console.log("Compiled at " + new Date().toTimeString());
                callback();
            })
        },
        new webpack.ProvidePlugin({
            '$': 'jquery',
            // 'React': 'react',
            // 'ReactDOM': 'react-dom'
        }),
        new WebpackShellPlugin({
            onBuildExit: [`echo cp ${outputDir}/bundle.js ${ideaOutFile}/`, `cp ${outputDir}/bundle.js ${ideaOutFile}/bundle.js`]
        }),
        // new UglifyJsPlugin({
        //     sourceMap: true,
        //     parallel: true
        // }),
        // new BundleAnalyzerPlugin()
    ]

};
