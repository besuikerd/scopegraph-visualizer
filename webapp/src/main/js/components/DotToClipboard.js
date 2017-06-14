import React, { Component } from 'react';
import { connect } from 'react-redux';
import { dotformat } from '../selectors';
import { copyToClipboard } from '../lib/clipboard'
import { displayToast } from '../lib/toast';

class DotToClipboard extends Component{

  constructor(props){
    super(props);
    this.dotToClipboard = this.dotToClipboard.bind(this);
  }

  dotToClipboard(){
    const { dot } = this.props;
    if(dot !== null){
      copyToClipboard(dot)
      // displayToast("copied dot to clipboard")
    }
  }

  render(){
    const { dot } = this.props;
    return <button onClick={this.dotToClipboard} disabled={dot == null}><i className="fa fa-clipboard" aria-hidden="true"></i></button>
  }
}

function mapStateToProps(state){
  return {
    dot: dotformat(state)
  };
}

export default connect(mapStateToProps)(DotToClipboard)