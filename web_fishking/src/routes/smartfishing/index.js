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
import BoatAddPage from "../../pages/smartfishing/BoatAddPage";
import PaidPage from "../../pages/smartfishing/PaidPage";
import PaidDetailPage from "../../pages/smartfishing/PaidDetailPage";
import CsNoticePage from "../../pages/smartfishing/CsNoticePage";
import CsFaqPage from "../../pages/smartfishing/CsFaqPage";
import CsQnaAddPage from "../../pages/smartfishing/CsQnaAddPage";
import CsQnaListPage from "../../pages/smartfishing/CsQnaListPage";
import SetMainPage from "../../pages/smartfishing/SetMainPage";
import SetPaidPage from "../../pages/smartfishing/SetPaidPage";

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
              {/** 선박 */}
              <Route exact path={`${match.url}/boat`} component={BoatPage} />
              {/** 선박 > 등록 */}
              <Route
                exact
                path={`${match.url}/boat/add`}
                component={BoatAddPage}
              />
              {/** 정산 */}
              <Route exact path={`${match.url}/paid`} component={PaidPage} />
              {/** 정산 > 정산내역 */}
              <Route
                exact
                path={`${match.url}/paid/detail`}
                component={PaidDetailPage}
              />
              {/** 고객센터 > 공지사항 */}
              <Route
                exact
                path={`${match.url}/cs/notice`}
                component={CsNoticePage}
              />
              {/** 고객센터 > 자주하는질문 */}
              <Route exact path={`${match.url}/cs/faq`} component={CsFaqPage} />
              {/** 고객센터 > 1:1문의 > 문의하기 */}
              <Route
                exact
                path={`${match.url}/cs/qna/add`}
                component={CsQnaAddPage}
              />
              {/** 고객센터 > 1:1문의 > 문의내역 */}
              <Route
                exact
                path={`${match.url}/cs/qna/list`}
                component={CsQnaListPage}
              />
              {/** 고객센터 > 설정 */}
              <Route
                exact
                path={`${match.url}/set/main`}
                component={SetMainPage}
              />
              {/** 고객센터 > 정산계좌설정 */}
              <Route
                exact
                path={`${match.url}/set/paid`}
                component={SetPaidPage}
              />
              <Redirect from={`*`} to={`${match.url}/dashboard`} />
            </Switch>
          </React.Fragment>
        )}
      </BrowserRouter>
    );
  })
);
