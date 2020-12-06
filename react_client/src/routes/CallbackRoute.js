import React from "react";
import { Switch, Route } from "react-router-dom";
import { inject, observer } from "mobx-react";

import SNSPage from "../pages/callback/SNSPage";

const CallbackRoute = inject("RouteStore")(
  observer(({ match }) => {
    return (
      <Switch>
        {/** SNS 콜백 */}
        <Route exact path={`${match.url}/sns/:provider`} component={SNSPage} />
      </Switch>
    );
  })
);

export default CallbackRoute;
