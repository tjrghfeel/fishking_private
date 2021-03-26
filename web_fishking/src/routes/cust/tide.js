import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route, withRouter } from "react-router-dom";

import TidePage from "../../pages/cust/tide/TidePage";

export default inject()(
  observer(
    withRouter(({ match }) => {
      return (
        <Switch>
          {/** 물때 > 물때 */}
          <Route exact path={`${match.url}/:dayType`} component={TidePage} />
        </Switch>
      );
    })
  )
);
