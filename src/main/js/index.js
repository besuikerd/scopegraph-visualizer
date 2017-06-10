import '../css/style.scss';
import 'mini.css/dist/mini-default.css'
import 'font-awesome/css/font-awesome.css';

import React from 'react';
import ReactDOM from 'react-dom';
import {
  parseScopegraph,
  scopegraphToDot,
  ppDot
} from 'scopegraph-visualizer-fastopt';
import Viz from 'viz.js'

import App from './components/App'
import { Provider } from 'react-redux';

import store from './store';

const root = <Provider store={store}>
  <App/>
</Provider>;

window.onload = function() {
  var container = document.createElement('div')
  container.setAttribute("class", "root-container")
  document.body.appendChild(container);
  ReactDOM.render(root, container);
};