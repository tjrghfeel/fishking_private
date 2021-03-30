import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";

import LoginPage from "../../pages/smartsail/LoginPage";
import DashboardPage from "../../pages/smartsail/DashboardPage";
import SailPage from "../../pages/smartsail/SailPage";
import SailDetailPage from "../../pages/smartsail/SailDetailPage";
import SailAddPage from "../../pages/smartsail/SailAddPage";
import CameraPage from "../../pages/smartsail/CameraPage";
import CameraAddPage from "../../pages/smartsail/CameraAddPage";

export default inject("PageStore")(
  observer(({ PageStore, history, match }) => {
    // # >>>>> 기본 설정
    PageStore.setHistory(history);
    PageStore.loadAccessToken("smartsail");

    document.querySelector("#css-style").href =
      "/assets/smartsail/css/style.css";
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
        {/** 승선관리 */}
        <Route exact path={`${match.url}/sail`} component={SailPage} />
        {/** 승선관리 > 상세 */}
        <Route
          exact
          path={`${match.url}/sail/detail`}
          component={SailDetailPage}
        />
        {/** 승선관리 > 승선자추가 */}
        <Route exact path={`${match.url}/sail/add`} component={SailAddPage} />
        {/** 카메라관리 */}
        <Route exact path={`${match.url}/camera`} component={CameraPage} />
        {/** 카메라관리 > 카메라설정 */}
        <Route
          exact
          path={`${match.url}/camera/add`}
          component={CameraAddPage}
        />

        {/** 기본 리디렉션 */}
        <Redirect from={`*`} to={`${match.url}/login`} />
      </Switch>
    );
  })
);
