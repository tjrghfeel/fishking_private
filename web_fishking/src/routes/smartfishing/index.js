import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";

import LoginPage from "../../pages/smartfishing/LoginPage";
import FindpwPage from "../../pages/smartfishing/FindpwPage";
import ApplyPage from "../../pages/smartfishing/ApplyPage";
import DashboardPage from "../../pages/smartfishing/DashboardPage";
import ReservationPage from "../../pages/smartfishing/ReservationPage";
import ReservationDetailPage from "../../pages/smartfishing/ReservationDetailPage";
import FishPage from "../../pages/smartfishing/FishPage";
import GoodsPage from "../../pages/smartfishing/GoodsPage";
import GoodsAddPage from "../../pages/smartfishing/GoodsAddPage";
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
        {!PageStore.loggedIn && (
          <React.Fragment>
            <Switch>
              {/** 로그인 */}
              <Route path={`${match.url}/login`} component={LoginPage} />
              {/** 비밀번호재설정 */}
              <Route path={`${match.url}/findpw`} component={FindpwPage} />
              {/** 업체등록 */}
              <Route path={`${match.url}/apply`} component={ApplyPage} />
              {/** 기본 리디렉션 */}
              <Redirect from={`*`} to={`${match.url}/login`} />
            </Switch>
          </React.Fragment>
        )}
        {PageStore.loggedIn && (
          <React.Fragment>
            <Switch>
              {/** 대시보드 */}
              <Route
                exact
                path={`${match.url}/dashboard`}
                component={DashboardPage}
              />
              {/** 예약 */}
              <Route
                exact
                path={`${match.url}/reservation`}
                component={ReservationPage}
              />
              {/** 예약 > 상세 */}
              <Route
                exact
                path={`${match.url}/reservation/detail`}
                component={ReservationDetailPage}
              />
              {/** 조황 */}
              <Route exact path={`${match.url}/fish`} component={FishPage} />
              {/** 상품 */}
              <Route exact path={`${match.url}/goods`} component={GoodsPage} />
              {/** 상품 > 등록 */}
              <Route
                exact
                path={`${match.url}/goods/add`}
                component={GoodsAddPage}
              />
              {/** 선상 */}
              <Route exact path={`${match.url}/boat`} component={BoatPage} />
              {/** 정산 */}
              <Route exact path={`${match.url}/paid`} component={PaidPage} />
              <Redirect from={`*`} to={`${match.url}/dashboard`} />
            </Switch>
          </React.Fragment>
        )}
      </BrowserRouter>
    );
  })
);
