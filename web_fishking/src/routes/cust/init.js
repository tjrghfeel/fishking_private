import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route, withRouter } from "react-router-dom";

import PermissionsPage from "../../pages/cust/init/PermissionsPage";
import IntroPage from "../../pages/cust/init/IntroPage";

export default inject()(
  observer(
    withRouter(({ match }) => {
      return (
        <Switch>
          {/** 초기화면 > 권한요청 */}
          <Route
            exact
            path={`${match.url}/permissions`}
            component={PermissionsPage}
          />
          {/** 초기화면 > 인트로 */}
          <Route exact path={`${match.url}/intro`} component={IntroPage} />
        </Switch>
      );
    })
  )
);
