import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";

import LoginPage from "../../pages/smartfishing/LoginPage";
import DashboardPage from "../../pages/smartfishing/DashboardPage";
import ReservationPage from "../../pages/smartfishing/ReservationPage";
import FishPage from "../../pages/smartfishing/FishPage";
import GoodsPage from "../../pages/smartfishing/GoodsPage";
import BoatPage from "../../pages/smartfishing/BoatPage";
import PaidPage from "../../pages/smartfishing/PaidPage";

export default inject("PageStore")(
  observer(({ PageStore, history, match }) => {
    // # >>>>> 기본 설정
    PageStore.setHistory(history);
    PageStore.loadAccessToken("smartfishing");

    document.querySelector("#css-style").href =
      "/assets/smartfishing/css/style.css";
    return (
      <BrowserRouter>
        <Switch>
          {/** 로그인 */}
          <Route path={`${match.url}/login`} component={LoginPage} />
          {/** 대시보드 */}
          <Route path={`${match.url}/dashboard`} component={DashboardPage} />
          {/** 예약 */}
          <Route
            path={`${match.url}/reservation`}
            component={ReservationPage}
          />
          {/** 조황 */}
          <Route path={`${match.url}/fish`} component={FishPage} />
          {/** 상품 */}
          <Route path={`${match.url}/goods`} component={GoodsPage} />
          {/** 선상 */}
          <Route path={`${match.url}/boat`} component={BoatPage} />
          {/** 정산 */}
          <Route path={`${match.url}/paid`} component={PaidPage} />

          {/** 기본 리디렉션 */}
          <Redirect from={`*`} to={`${match.url}/login`} />
        </Switch>
      </BrowserRouter>
    );
  })
);
