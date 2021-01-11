import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import BlankPage from "../../views/BlankPage";

export default inject()(
  observer(({ match }) => {
    /********** ********** ********** ********** **********/
    /** function */
    /********** ********** ********** ********** **********/

    /********** ********** ********** ********** **********/
    /** render */
    /********** ********** ********** ********** **********/
    return (
      <BrowserRouter>
        <Switch>
          {/** 설정 */}
          <Route path={`${match.url}/set`} component={BlankPage} />
        </Switch>
      </BrowserRouter>
    );
  })
);
