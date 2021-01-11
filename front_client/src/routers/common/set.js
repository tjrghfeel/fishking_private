import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";
import Views from "../../views";

export default inject()(
  observer(({ match }) => {
    /********** ********** ********** ********** **********/
    /** render */
    /********** ********** ********** ********** **********/
    return (
      <BrowserRouter>
        <Switch>
          {/** 공통 > 설정 > 메인 */}
          <Route exact path={`${match.url}/main`} component={Views.BlankPage} />
        </Switch>
      </BrowserRouter>
    );
  })
);
