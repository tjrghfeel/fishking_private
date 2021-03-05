import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import TidePage from "../../pages/cust/tide/TidePage";

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 물때 > 물때 */}
          <Route exact path={`${match.url}/:dayType`} component={TidePage} />
        </Switch>
      </BrowserRouter>
    );
  })
);
