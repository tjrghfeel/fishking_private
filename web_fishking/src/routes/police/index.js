import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route, Redirect } from "react-router-dom";

import LoginPage from "../../pages/police/LoginPage";
import DashboardPage from "../../pages/police/DashboardPage";
import MapPage from "../../pages/police/MapPage";
import BoatPage from "../../pages/police/BoatPage";
import AboardPage from "../../pages/police/AboardPage";
import MyPage from "../../pages/police/MyPage";

export default inject("PageStore")(
  observer(({ PageStore, history, match }) => {
    // # >>>>> 기본 설정
    PageStore.setHistory(history);
    PageStore.loadAccessToken("police");

    document.querySelector("#css-style").href = "/assets/police/css/style.css";
    return (
      <Switch>
        {/** 로그인 */}
        <Route exact path={`${match.url}/login`} component={LoginPage} />
        {/** 대시보드 */}
        <Route
          exact
          path={`${match.url}/dashboard`}
          component={DashboardPage}
        />
        {/** 출항정보 */}
        <Route exact path={`${match.url}/map`} component={MapPage} />
        {/** 선상정보 */}
        <Route exact path={`${match.url}/boat`} component={BoatPage} />
        {/** 선상정보 > 승선명부 */}
        <Route exact path={`${match.url}/aboard`} component={AboardPage} />
        {/** 더보기 */}
        <Route exact path={`${match.url}/my`} component={MyPage} />

        {/** 기본 리디렉션 */}
        <Redirect from={`*`} to={`${match.url}/login`} />

        {/*{!PageStore.loggedIn && (*/}
        {/*  <React.Fragment>*/}
        {/*    /!** 로그인 *!/*/}
        {/*    <Route path={`${match.url}/login`} component={LoginPage} />*/}
        {/*    /!** 기본 리디렉션 *!/*/}
        {/*    <Redirect from={`*`} to={`${match.url}/login`} />*/}
        {/*  </React.Fragment>*/}
        {/*)}*/}
        {/*{PageStore.loggedIn && (*/}
        {/*  <React.Fragment>*/}
        {/*    /!** 대시보드 *!/*/}
        {/*    <Route*/}
        {/*      path={`${match.url}/dashboard`}*/}
        {/*      component={DashboardPage}*/}
        {/*    />*/}
        {/*    /!** 기본 리디렉션 *!/*/}
        {/*    <Redirect from={`*`} to={`${match.url}/dashboard`} />*/}
        {/*  </React.Fragment>*/}
        {/*)}*/}
      </Switch>
    );
  })
);
