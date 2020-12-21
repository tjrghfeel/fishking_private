import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import BlankPage from "../views/BlankPage";
import CommonLoginView from "../views/common/CommonLoginView";
import CommonFindpwView from "../views/common/CommonFindpwView";
import CommonSignupView from "../views/common/CommonSignupView";
import CommonSignoutView from "../views/common/CommonSignoutView";
import CommonCouponAvailable from "../views/common/CommonCouponAvailableView";
import CommonProfileView from "../views/common/CommonProfileView";

export default inject()(
  observer(({ match }) => {
    return (
      <>
        <Switch>
          {/** 공통 > 버전정보 */}
          <Route
            exact
            path={`${match.url}/version`}
            component={CommonLoginView}
          />
          {/** 공통 > 로그인 */}
          <Route
            exact
            path={`${match.url}/login`}
            component={CommonLoginView}
          />
          {/** 공통 > 비밀번호재설정 */}
          <Route
            exact
            path={`${match.url}/findpw`}
            component={CommonFindpwView}
          />
          {/** 공통 > 회원가입 */}
          <Route
            exact
            path={`${match.url}/signup`}
            component={CommonSignupView}
          />
          {/** 공통 > 회원탈퇴 */}
          <Route
            exact
            path={`${match.url}/signout`}
            component={CommonSignoutView}
          />
          {/** 공통 > 혜택쿠폰 */}
          <Route
            exact
            path={`${match.url}/coupon/available`}
            component={CommonCouponAvailable}
          />
          {/** 공통 > 프로필보기 */}
          <Route
            exact
            path={`${match.url}/profile/:memberId`}
            component={CommonProfileView}
          />
          {/** 공통 > 물때 */}
          <Route exact path={`${match.url}/tide`} component={BlankPage} />
          {/** 공통 > 공지사항 */}
          <Route exact path={`${match.url}/notice`} component={BlankPage} />
          {/** 공통 > 이벤트 */}
          <Route exact path={`${match.url}/event`} component={BlankPage} />
          {/** 공통 > 고객센터 > FAQ */}
          <Route exact path={`${match.url}/cs/faq`} component={BlankPage} />
        </Switch>
      </>
    );
  })
);
