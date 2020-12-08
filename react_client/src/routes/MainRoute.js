import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import MainHomePage from "../pages/main/MainHomePage";
import MainBoatPage from "../pages/main/MainBoatPage";
import MainMyPage from "../pages/main/MainMyPage";
import BottomTabs from "../components/layout/BottomTabs";

export default inject()(
  observer(({ match }) => {
    return (
      <>
        <Switch>
          {/** 메인 > 홈 */}
          <Route exact path={`${match.url}/home`} component={MainHomePage} />
          {/** 메인 > 선상 */}
          <Route exact path={`${match.url}/boat`} component={MainBoatPage} />
          {/** 메인 > 마이메뉴 */}
          <Route exact path={`${match.url}/my`} component={MainMyPage} />
        </Switch>
        <BottomTabs />
      </>
    );
  })
);
