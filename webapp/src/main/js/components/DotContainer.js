import React from 'react';
import { connect } from 'react-redux';

import {dotformat} from '../selectors';

const DotContainer = ({dot}) => {
  return dot !== null ? <pre className="dot-source-container">{dot}</pre> : <div></div>;
};

function mapStateToProps(state){
  return {
    dot: dotformat(state)
  }
}

export default connect(mapStateToProps)(DotContainer)
