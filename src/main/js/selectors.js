import {createSelector} from 'reselect';

import {parseScopegraph, scopegraphToDot, ppDot} from 'scopegraph-visualizer'
import Viz from 'viz.js';

export const getScopegraphText = state => state.scopegraphText;

const parsedScopegraphOrError = createSelector([getScopegraphText], (scopegraphText) => {
  return parseScopegraph(scopegraphText)
});

const scopegraph = createSelector([parsedScopegraphOrError], (parsedScopegraphOrError) => {
  return typeof parsedScopegraphOrError == 'object' ? parsedScopegraphOrError : null;
});

export const error = createSelector([parsedScopegraphOrError], (parsedScopegraphOrError) => {
  return typeof parsedScopegraphOrError == 'string' ? parsedScopegraphOrError : 'No errors';
});

export const dotformat = createSelector([scopegraph], (scopegraph) => {
  return scopegraph == null ? null : ppDot(scopegraphToDot(scopegraph));
});

export const svg = createSelector([dotformat], (dotformat) => {
  return dotformat == null ? null : Viz(dotformat);
});

export const svgBase64 = createSelector([svg], (svg) => {
  return svg !== null ? btoa(svg) : null;
})