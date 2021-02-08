import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";

import MainRoute from "./main";
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

export default inject("PageStore")(
  observer(({ PageStore, history, match }) => {
    PageStore.setHistory(history);
    // # SNS 로그인 콜백 체크
    const { loggedIn = false, accesstoken = null } = PageStore.getQueryParams();
    if (loggedIn && accesstoken !== null) {
      PageStore.setLogin(accesstoken);
    }
    // # 로그인 체크
    PageStore.loadLogin();
    return (
      <BrowserRouter>
        <Switch>
          {/** 메인 */}
          <Route path={`${match.url}/main`} component={MainRoute} />

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

          {/** 기본 리디렉션 */}
          <Redirect from={`*`} to={`${match.url}/main/home`} />
        </Switch>
      </BrowserRouter>
    );
  })
);
