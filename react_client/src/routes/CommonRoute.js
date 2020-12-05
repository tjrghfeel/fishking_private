import React from "react";
import { Switch, Route } from "react-router-dom";
import { inject, observer } from "mobx-react";

import LoginPage from "../pages/common/LoginPage";

const CommonRoute = inject("RouteStore")(
  observer(({ match }) => {
    return (
      <>
        <Switch>
          {/** 로그인 */}
          <Route exact path={`${match.url}/login`} component={LoginPage} />
        </Switch>
      </>
    );
  })
);

export default CommonRoute;
