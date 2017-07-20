import React, { Component } from 'react';
import { connect } from 'react-redux';
import vis from 'vis';

import { visFormat } from 'selectors';

class JSONView extends Component{

  componentDidUpdate(){
    this.setupVis()
  }

  componentDidMount(){
    this.setupVis()
  }

  setupVis(){
    const json = JSON.parse(this.props.scopegraph)
    const dataset = {
      nodes: new vis.DataSet(json.nodes),
      edges: new vis.DataSet(json.edges)
    };
    const container = this.refs['vis-container'];
    const network = new vis.Network(container, dataset, {
      nodes: {
        scaling: {
          label: {
            enabled: true
          }
        }
      },

      groups: {
        scope: {
          shape: 'circle'
        },
        ref: {
          shape: 'box',
          shapeProperties: {
            borderRadius: 0
          }
        },
        decl: {
          shape: 'box',
          shapeProperties: {
            borderRadius: 0
          }
        }
      }
    })
  }

  render(){
    return <div className="scopegraph-image" ref="vis-container">
    </div>
  }
}

function mapStateToProps(state){
  return {
    scopegraph: visFormat(state)
  }
}


export default connect(mapStateToProps)(JSONView)