import React from 'react';
import { connect } from 'react-redux';
import TextualActions from './TextualActions';

import {
  textFormat,
  formatName
} from '../selectors';

const TextualContainer = ({text, formatName}) => {
  return <div className="card fluid">
    <TextualActions/>
    {
      text !== null ? <pre className="textual-source-container">{text}</pre> : <div></div>
    }
  </div>;
};

function mapStateToProps(state){
  return {
    text: textFormat(state)
  }
}

export default connect(mapStateToProps)(TextualContainer)
