import React, { Component } from 'react';
import { connect } from 'react-redux';
import { svgBase64 } from '../selectors';

class SvgContainer extends Component {

  constructor(props){
    super(props);
    // this.DOMParser = new DOMParser();
  }

  componentDidMount(){
    // this.updateSvg();
  }

  componentDidUpdate(){
    // this.updateSvg();
  }

  updateSvg(){
    // const svgContainer = this.refs.svgContainer;
    // for(let child of svgContainer.children){
    //   svgContainer.removeChild(child)
    // }
    // const svg = this.props.svg;
    // if(svg !== null){
    //   const svgElement = this.DOMParser.parseFromString(svg, 'image/svg+xml');
    //   for(let child of svgElement.children){
    //     svgContainer.appendChild(child)
    //     // child.setAttribute('class', 'svg-holder')
    //     child.removeAttribute('width');
    //     child.removeAttribute('height');
    //   }
    //
    // }
  }

  render() {
    const { svgBase64 } = this.props;

    return <div className="svg-holder" ref="svgContainer">

      { !svgBase64 || <img className="scopegraph-image" src={'data:image/svg+xml;base64, ' + svgBase64}></img> }
    </div>;
  }
}
function mapStateToProps(state){
  return {
    svgBase64: svgBase64(state)
  }
}

export default connect(mapStateToProps)(SvgContainer);

