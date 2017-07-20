import React from 'react';
import { connect } from 'react-redux';
import { error } from '../selectors';

const ErrorDisplay = ({error}) => {
  return <div>{error}</div>
};

function mapStateToProps(state){
  return {
    error: error(state)
  }
}

export default connect(mapStateToProps)(ErrorDisplay)