import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";

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

export default inject("PageStore")(
  observer(({ PageStore, history, match }) => {
    // # SNS 로그인 콜백 체크
    const { loggedIn = false, accesstoken = null } = PageStore.getQueryParams();
    if (loggedIn && accesstoken !== null) {
      PageStore.setAccessToken(accesstoken, "cust", "Y");
    }

    // # >>>>> 기본 설정
    PageStore.setHistory(history);
    PageStore.loadAccessToken("cust");

    // # >>>>> 화면 접근 권한 체크 :: 로그인화면으로 리디렉트
    if (
      !PageStore.loggedIn &&
      history.location.pathname.indexOf(`/reservation/goods/`) !== -1
    ) {
      window.location.href = "/cust/member/login";
    }
    return (
      <BrowserRouter>
        <Switch>
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

          {/** 기본 리디렉션 */}
          <Redirect from={`*`} to={`${match.url}/main/home`} />
        </Switch>
      </BrowserRouter>
    );
  })
);
