import React from 'react';

import ScopegraphEditor from './ScopegraphEditor'
import DotContainer from './DotContainer'
import SvgContainer from './SvgContainer'
import ErrorDisplay from './ErrorDisplay';
import DotToClipboard from './DotToClipboard'

export default () => <div className="app-container">
    <div className="container">
      <div className="row">
        <div className="error-container">
          <ErrorDisplay/>
        </div>
      </div>

      <div className="row text-container">
        <div className="col-sm-6 scopegraph-container">
          <div className="card fluid">
            <h3 className="scopegraph-title">Scope graph</h3>
            <ScopegraphEditor/>
          </div>
        </div>
        <div className="col-sm-6 dot-container">
          <div className="card fluid">
            <h3 className="dot-title">Dot format</h3>
            <div className="dot-actions">
              <DotToClipboard/>
            </div>
            <DotContainer/>
          </div>
        </div>
      </div>

      <div className="row svg-container">
        <SvgContainer/>
      </div>
  </div>
</div>