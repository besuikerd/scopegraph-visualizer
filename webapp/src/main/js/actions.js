export const SET_SCOPEGRAPH = 'SET_SCOPEGRAPH';
export const SET_OUTPUT_FORMAT = 'SET_OUTPUT_FORMAT';

export const DOT_FORMAT = 'dot';
export const JSON_FORMAT = 'json';

export function setScopegraph(scopegraph){
  return {
    type: SET_SCOPEGRAPH,
    scopegraph: scopegraph
  };
}

export function setDotFormat(){
  return {
    type: SET_OUTPUT_FORMAT,
    format: DOT_FORMAT
  }
}

export function setJSONFormat(){
  return {
    type: SET_OUTPUT_FORMAT,
    format: JSON_FORMAT
  }
}