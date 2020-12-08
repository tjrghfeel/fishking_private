import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import MainHomePage from "../pages/main/MainHomePage";
import BottomTabs from "../components/layouts/BottomTabs";

export default inject()(
  observer(({ match }) => {
    return (
      <>
        <Switch>
          {/** 메인 > 홈 */}
          <Route exact path={`${match.url}/home`} component={MainHomePage} />
        </Switch>
        <BottomTabs />
      </>
    );
  })
);
