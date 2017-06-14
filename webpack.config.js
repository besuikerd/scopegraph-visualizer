var path = require('path');
var fs = require('fs');
var webpack = require('webpack');

module.exports = {
  entry: 'index',

  output: {
    path: path.join(__dirname, 'target'),
    publicPath: '/assets/build/',
    filename: '[name].js',
    chunkFilename: '[chunkhash].js'
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: [/node_modules/, /target\/scala-2.12/],

        loader: 'babel-loader',
        query: {
          presets: ['es2015', 'react']
        }
      },

      {
        test: /\.(css|scss)$/,
        use: [{
          loader: "style-loader"
        }, {
          loader: "css-loader"
        }, {
          loader: "sass-loader",
          options: {
            includePaths: ["absolute/path/a", "absolute/path/b"]
          }
        }]
      },
      {
        test: /\.woff(\?v=\d+\.\d+\.\d+)?$/,
        loader: "url-loader?limit=10000&mimetype=application/font-woff"
      },
      {
        test: /\.woff2(\?v=\d+\.\d+\.\d+)?$/,
        loader: "url-loader?limit=10000&mimetype=application/font-woff"
      },
      {
        test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/,
        loader: "url-loader?limit=10000&mimetype=application/octet-stream"
      },
      {
        test: /\.eot(\?v=\d+\.\d+\.\d+)?$/,
        loader: "file-loader"
      },
      {
        test: /\.svg(\?v=\d+\.\d+\.\d+)?$/,
        loader: "url-loader?limit=10000&mimetype=image/svg+xml"
      },
      {
        test: /\.(txt|scopegraph)$/,
        use: 'raw-loader'
      }
    ]
  },
  resolve: {
    modules: [
      'node_modules',
      path.join(__dirname, 'src/main/js')
    ],

    alias: {
      'scopegraph-visualizer': path.join(__dirname, 'js/target/scala-2.12/scopegraph-visualizer-fastopt.js')
    }
  }
};
