import React, { Component } from 'react';
import { connect } from 'react-redux';
import {setScopegraph} from '../actions'
import {getScopegraphText} from '../selectors';

export class ScopegraphEditor extends Component {
  constructor(props){
    super(props);
    this.setScopegraph = this.setScopeGraph.bind(this);
  }

  componentDidMount(){
    this.refs.textArea.focus();
  }

  setScopeGraph(e){
    this.props.setScopegraph(e.target.value);
  }

  render() {
    return <textarea className="scopegraph-editor" ref="textArea" value={this.props.value} onChange={this.setScopegraph}/>
  }
}


function mapStatetoProps(state){
  return {
    value: getScopegraphText(state)
  }
}

const mapDispatchToProps = {
  setScopegraph: setScopegraph
}

export default connect(mapStatetoProps, mapDispatchToProps)(ScopegraphEditor)