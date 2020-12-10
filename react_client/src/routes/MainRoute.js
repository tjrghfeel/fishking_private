import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import MainHomePage from "../pages/main/MainHomePage";
import MainReserveBoatPage from "../pages/main/MainReserveBoatPage";
import MainReserveRockPage from "../pages/main/MainReserveRockPage";
import MainStoryPage from "../pages/main/MainStoryPage";
import MainStoryTvPage from "../pages/main/MainStoryTvPage";
import MainStoryUserPage from "../pages/main/MainStoryUserPage";
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
          <Route
            exact
            path={`${match.url}/reserve/boat`}
            component={MainReserveBoatPage}
          />
          {/** 메인 > 갯바위 */}
          <Route
            exact
            path={`${match.url}/reserve/rock`}
            component={MainReserveRockPage}
          />
          {/** 메인 > 어복스토리 > 조황일지 */}
          <Route
            exact
            path={`${match.url}/story/main`}
            component={MainStoryPage}
          />
          {/** 메인 > 어복스토리 > 어복TV */}
          <Route
            exact
            path={`${match.url}/story/tv`}
            component={MainStoryTvPage}
          />
          {/** 메인 > 어복스토리 > 유저조행기 */}
          <Route
            exact
            path={`${match.url}/story/user`}
            component={MainStoryUserPage}
          />
          {/** 메인 > 마이메뉴 */}
          <Route exact path={`${match.url}/my`} component={MainMyPage} />
        </Switch>
        <BottomTabs />
      </>
    );
  })
);
