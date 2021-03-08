import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";

import InitRoute from "./init";
import MainRoute from "./main";
import CompanyRoute from "./company";
import MemberRoute from "./member";
import SetRoute from "./set";
import PolicyRoute from "./policy";
import CsRoute from "./cs";
import StoryRoute from "./story";
import ZzimRoute from "./zzim";
import CouponRoute from "./coupon";
import SearchRoute from "./search";
import ReservationRoute from "./reservation";
import PayRoute from "./pay";
import EventRoute from "./event";
import GuideRoute from "./guide";
import TideRoute from "./tide";

import crypto from "crypto";

export default inject("PageStore")(
  observer(({ PageStore, history, match }) => {
    // # SNS 로그인 콜백 체크
    const { loggedIn = false, accesstoken = null } = PageStore.getQueryParams();
    if (loggedIn && accesstoken !== null) {
      PageStore.setAccessToken(
        decodeURI(accesstoken).replace(/[ ]/g, "+").decrypt(),
        "cust",
        "Y"
      );
    }

    // # >>>>> 기본 설정
    PageStore.setHistory(history);
    PageStore.loadAccessToken("cust");
    // # 리디렉션
    const redirectUrl = sessionStorage.getItem("@redirect-url");
    if (
      redirectUrl !== null &&
      history.location.pathname.indexOf("/member/login") === -1
    ) {
      sessionStorage.removeItem("@redirect-url");
      window.location.href = redirectUrl;
    }
    // # >>>>> 뒤로가기 시 로그인화면 건너뛰기
    const goBack = sessionStorage.getItem("@goBack") || "N";
    if (
      goBack === "Y" &&
      PageStore.loggedIn &&
      history.location.pathname.indexOf(`/member/login`) !== -1
    ) {
      sessionStorage.removeItem("@goBack");
      window.history.go(-2);
      return;
    } else {
      sessionStorage.removeItem("@goBack");
    }
    // # >>>>> 화면 접근 권한 체크 :: 로그인 화면으로 리디렉트
    if (
      !PageStore.loggedIn &&
      (history.location.pathname.indexOf(`/reservation/goods/`) !== -1 ||
        history.location.pathname.indexOf(`/story/add`) !== -1)
    ) {
      sessionStorage.setItem(
        "@redirect-url",
        history.location.pathname + (history.location.search || "")
      );
      window.location.href = `/cust/member/login`;
    }
    return (
      <BrowserRouter>
        <Switch>
          {/** 초기화면 */}
          <Route path={`${match.url}/init`} component={InitRoute} />

          {/** 메인 */}
          <Route path={`${match.url}/main`} component={MainRoute} />

          {/** 업체 */}
          <Route path={`${match.url}/company`} component={CompanyRoute} />

          {/** 멤버 */}
          <Route path={`${match.url}/member`} component={MemberRoute} />

          {/** 설정 */}
          <Route path={`${match.url}/set`} component={SetRoute} />

          {/** 약관및정책 */}
          <Route path={`${match.url}/policy`} component={PolicyRoute} />

          {/** 고객센터 */}
          <Route path={`${match.url}/cs`} component={CsRoute} />

          {/** 스토리 */}
          <Route path={`${match.url}/story`} component={StoryRoute} />

          {/** 찜 */}
          <Route path={`${match.url}/zzim`} component={ZzimRoute} />

          {/** 쿠폰 */}
          <Route path={`${match.url}/coupon`} component={CouponRoute} />

          {/** 검색 */}
          <Route path={`${match.url}/search`} component={SearchRoute} />

          {/** 예약 */}
          <Route
            path={`${match.url}/reservation`}
            component={ReservationRoute}
          />

          {/** 결제 */}
          <Route path={`${match.url}/pay`} component={PayRoute} />

          {/** 이벤트 */}
          <Route path={`${match.url}/event`} component={EventRoute} />

          {/** 가이드 */}
          <Route path={`${match.url}/guide`} component={GuideRoute} />

          {/** 물때 */}
          <Route path={`${match.url}/tide`} component={TideRoute} />

          {/** 기본 리디렉션 */}
          <Redirect from={`*`} to={`${match.url}/main/home`} />
        </Switch>
      </BrowserRouter>
    );
  })
);
