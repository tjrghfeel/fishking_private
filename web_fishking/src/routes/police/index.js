import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";

import LoginPage from "../../pages/police/LoginPage";
import DashboardPage from "../../pages/police/DashboardPage";

export default inject("PageStore")(
  observer(({ PageStore, history, match }) => {
    // # >>>>> 기본 설정
    PageStore.setHistory(history);
    PageStore.loadAccessToken("police");
    return (
      <BrowserRouter>
        <Switch>
          {!PageStore.loggedIn && (
            <React.Fragment>
              {/** 로그인 */}
              <Route path={`${match.url}/login`} component={LoginPage} />
              {/** 기본 리디렉션 */}
              <Redirect from={`*`} to={`${match.url}/login`} />
            </React.Fragment>
          )}
          {PageStore.loggedIn && (
            <React.Fragment>
              {/** 대시보드 */}
              <Route
                path={`${match.url}/dashboard`}
                component={DashboardPage}
              />
              {/** 기본 리디렉션 */}
              <Redirect from={`*`} to={`${match.url}/dashboard`} />
            </React.Fragment>
          )}
        </Switch>
      </BrowserRouter>
    );
  })
);
