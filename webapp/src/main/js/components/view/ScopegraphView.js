import React, { Component } from 'react';
import { connect } from 'react-redux';
import {
  getOutputFormat
} from 'selectors';

import {
  DOT_FORMAT,
  JSON_FORMAT
} from 'actions';

import DotView from './DotView';
import JSONView from './JSONView';

const ScopegraphView = ({format}) => {
  switch(format){
    case DOT_FORMAT: return <DotView/>;
    case JSON_FORMAT: return <JSONView/>;
  }
};

function mapStateToProps(state){
  return {
    format: getOutputFormat(state)
  };
}

export default connect(mapStateToProps)(ScopegraphView);