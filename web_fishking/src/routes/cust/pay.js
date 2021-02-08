import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import PayKspayPage from "../../pages/cust/pay/PayKspayPage";

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 결제 > kspay */}
          <Route exact path={`${match.url}/kspay`} component={PayKspayPage} />
        </Switch>
      </BrowserRouter>
    );
  })
);
