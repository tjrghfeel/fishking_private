import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";

import LoginPage from "../../pages/smartsail/LoginPage";
import DashboardPage from "../../pages/smartsail/DashboardPage";
import SailPage from "../../pages/smartsail/SailPage";
import CameraPage from "../../pages/smartsail/CameraPage";

export default inject("PageStore")(
  observer(({ PageStore, history, match }) => {
    // # >>>>> 기본 설정
    PageStore.setHistory(history);
    PageStore.loadAccessToken("smartsail");

    document.querySelector("#css-style").href =
      "/assets/smartsail/css/style.css";
    return (
      <BrowserRouter>
        <Switch>
          {/** 로그인 */}
          <Route path={`${match.url}/login`} component={LoginPage} />
          {/** 대시보드 */}
          <Route path={`${match.url}/dashboard`} component={DashboardPage} />
          {/** 승선관리 */}
          <Route path={`${match.url}/sail`} component={SailPage} />
          {/** 카메라관리 */}
          <Route path={`${match.url}/camera`} component={CameraPage} />

          {/** 기본 리디렉션 */}
          <Redirect from={`*`} to={`${match.url}/login`} />
        </Switch>
      </BrowserRouter>
    );
  })
);
