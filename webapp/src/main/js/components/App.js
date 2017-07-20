import React from 'react';

import ScopegraphEditor from './ScopegraphEditor'
import TextualContainer from './TextualContainer'
// import SvgContainer from './SvgContainer'
import ErrorDisplay from './ErrorDisplay';
import ScopegraphView from './view/ScopegraphView';

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
        <div className="col-sm-6 textual-container">
          <TextualContainer/>
        </div>
      </div>

      <div className="row scopegraph-view-container">
        <ScopegraphView/>
      </div>
  </div>
</div>