import React from "react";
import { Switch, Route, Redirect } from "react-router-dom";
import { inject, observer } from "mobx-react";

import IndexPage from "../pages/main/IndexPage";
import BoatPage from "../pages/main/BoatPage";
import MyPage from "../pages/main/MyPage";

import MainTabs from "../components/tabs/MainTabs.js";

const MainRoute = inject("RouteStore")(
  observer(({ match }) => {
    return (
      <>
        <Switch>
          {/** 홈 */}
          <Route exact path={`${match.url}/index`} component={IndexPage} />
          {/** 선상 */}
          <Route exact path={`${match.url}/boat`} component={BoatPage} />
          {/** 마이메뉴 */}
          <Route exact path={`${match.url}/my`} component={MyPage} />
          {/** 기본 리디렉션 */}
          <Redirect from={`*`} to={`${match.url}/index`} />
        </Switch>
        <MainTabs />
      </>
    );
  })
);

export default MainRoute;
