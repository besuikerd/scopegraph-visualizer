import React, { Component } from 'react';
import { connect } from 'react-redux';
import { copyToClipboard } from '../lib/clipboard'
import {
  getOutputFormat,
  textFormat
} from '../selectors'
import {
  DOT_FORMAT,
  JSON_FORMAT
} from '../actions'


import {
  setDotFormat,
  setJSONFormat
} from '../actions'

class TextualActions extends Component {
  constructor(props){
    super(props);
    this.textToClipboard = this.textToClipboard.bind(this);
    this.formatButtonClass = this.formatButtonClass.bind(this);
  }

  textToClipboard(){
    const { text } = this.props;
    if(text !== null){
      copyToClipboard(text);
      // displayToast("copied text to clipboard")
    }
  }

  formatButtonClass(format){
    return this.props.format == format ?
      'primary' : '';
  }

  render(){
    const {
      text,
      format,
      setDotFormat,
      setJSONFormat
    } = this.props;
    return <div className="textual-actions-container">
      <div className="row">
        <div>
          <div className="button-group">
            <button className={this.formatButtonClass(DOT_FORMAT)}  onClick={setDotFormat}>DOT</button>
            <button className={this.formatButtonClass(JSON_FORMAT)} onClick={setJSONFormat}>JSON</button>
          </div>
        </div>

        <div className="textual-actions-buttons">
          <button onClick={this.textToClipboard} disabled={text == null}><i className="fa fa-clipboard" aria-hidden="true"></i></button>
        </div>

      </div>

    </div>;

  }
}


function mapStateToProps(state){
  return {
    format: getOutputFormat(state),
    text: textFormat(state)
  };
}

const mapDispatchToProps = {
  setDotFormat: setDotFormat,
  setJSONFormat: setJSONFormat
};

export default connect(mapStateToProps, mapDispatchToProps)(TextualActions)