import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import BlankPage from "../views/BlankPage";

export default inject()(
  observer(({ match }) => {
    return (
      <>
        <Switch>
          {/** 선상 > 메인 */}
          <Route exact path={`${match.url}/main`} component={BlankPage} />
        </Switch>
      </>
    );
  })
);
