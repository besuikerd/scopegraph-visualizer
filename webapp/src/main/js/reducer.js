import {
  SET_SCOPEGRAPH,
  SET_OUTPUT_FORMAT,
  DOT_FORMAT,
  JSON_FORMAT
} from './actions'

import initialScopegraph from '../resources/initial.scopegraph';

const initialState = {
  scopegraphText: initialScopegraph,
  format: DOT_FORMAT
};

const reducerActions = {};
reducerActions[SET_SCOPEGRAPH] = setScopegraphText;
reducerActions[SET_OUTPUT_FORMAT] = setOutputFormat;
reducerActions['@@INIT'] = function(state) { return state; }

export default function reducer(state = initialState, action){
  let reducerAction = reducerActions[action.type];
  if(reducerAction !== undefined){
    state = reducerAction(state, action)
  } else{
    console.warn('unknown action ', action.type)
  }
  return state;
}

function setScopegraphText(state, action){
  return Object.assign({}, state, {scopegraphText: action.scopegraph});
}

function setOutputFormat(state, action){
  return Object.assign({}, state, {
    format: action.format
  })
}