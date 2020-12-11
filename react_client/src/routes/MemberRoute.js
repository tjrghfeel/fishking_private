import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import BlankPage from "../pages/BlankPage";

export default inject()(
  observer(({ match }) => {
    return (
      <>
        <Switch>
          {/** 멤버 > 로그인 */}
          <Route exact path={`${match.url}/login`} component={BlankPage} />
        </Switch>
      </>
    );
  })
);
