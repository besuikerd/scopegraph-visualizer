var path = require('path');
var config = require('./webpack.config');
var webpack = require('webpack');
var merge = require('webpack-merge');

console.log(path.join(__dirname, 'target/scala-2.12/scopegraph-visualizer-opt.js'))

module.exports = merge(config, {
  resolve: {
    alias: {
      'scopegraph-visualizer': path.join(__dirname, 'js/target/scala-2.12/scopegraph-visualizer-opt.js')
    }
  },
  plugins: [
    new webpack.LoaderOptionsPlugin({
      minimize: true,
      debug: false
    }),
    new webpack.optimize.UglifyJsPlugin({
      exclude: [/target\/scala-2.12/]
    }),
    new webpack.DefinePlugin({
      'process.env': {
        NODE_ENV: JSON.stringify('production')
      }
    })
  ]
});