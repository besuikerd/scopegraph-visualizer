import React from 'react';
import { connect } from 'react-redux';

import { svgBase64 } from 'selectors';

const DotView = ({svgBase64}) => {
  return <img className="scopegraph-image" src={'data:image/svg+xml;base64, ' + svgBase64}>
    </img>;
};

function mapStateToProps(state){
  return {
    svgBase64: svgBase64(state)
  }
}

export default connect(mapStateToProps)(DotView)