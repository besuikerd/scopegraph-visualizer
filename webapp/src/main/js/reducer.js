import {
  SET_SCOPEGRAPH
} from './actions'

import initialScopegraph from '../resources/initial.scopegraph';

const initialState = {
  scopegraphText: initialScopegraph,
  dot: '',
  error: ''
};

const reducerActions = {};
reducerActions[SET_SCOPEGRAPH] = setScopegraphText
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