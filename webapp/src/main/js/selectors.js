import {createSelector} from 'reselect';

import {
  parseScopegraph,
  scopegraphToDot,
  ppDot,
  scopegraphToJSON,
  scopegraphToVis,
  ppJSON
} from 'scopegraph-visualizer'
import Viz from 'viz.js';

import {
  DOT_FORMAT,
  JSON_FORMAT
} from './actions'

export const getState = state => state;
export const getScopegraphText = state => state.scopegraphText;
export const getOutputFormat = state => state.format;

export const formatName = createSelector([getOutputFormat], format => {
  switch(format){
    case DOT_FORMAT: return 'DOT';
    case JSON_FORMAT: return 'JSON';
  }
});

const parsedScopegraphOrError = createSelector([getScopegraphText], (scopegraphText) => {
  return parseScopegraph(scopegraphText)
});

const scopegraph = createSelector([parsedScopegraphOrError], (parsedScopegraphOrError) => {
  return typeof parsedScopegraphOrError == 'object' ? parsedScopegraphOrError : null;
});

export const error = createSelector([parsedScopegraphOrError], (parsedScopegraphOrError) => {
  return typeof parsedScopegraphOrError == 'string' ? parsedScopegraphOrError : 'No errors';
});

export const textFormat = createSelector([getOutputFormat, getState], (format, state) => {
  switch(format){
    case DOT_FORMAT:
      return dotFormat(state);
      break;
    case JSON_FORMAT:
      return jsonFormat(state);
  }
});

export const dotFormat = createSelector([scopegraph], (scopegraph) => {
  return scopegraph == null ? null : ppDot(scopegraphToDot(scopegraph));
});

export const jsonFormat = createSelector([scopegraph], (scopegraph) => {
  return scopegraph == null ? null : ppJSON(scopegraphToJSON(scopegraph))
});

export const visFormat = createSelector([scopegraph], (scopegraph) => {
  return scopegraph == null ? null : ppJSON(scopegraphToVis(scopegraph))
});

export const svg = createSelector([dotFormat], (dotformat) => {
  return dotformat == null ? null : Viz(dotformat);
});

export const svgBase64 = createSelector([svg], (svg) => {
  return svg !== null ? btoa(svg) : null;
});