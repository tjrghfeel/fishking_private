import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route, withRouter } from "react-router-dom";

import MemberLoginPage from "../../pages/cust/member/MemberLoginPage";
import MemberLoginTokenPage from "../../pages/cust/member/MemberLoginTokenPage";
import MemberFindpwPage from "../../pages/cust/member/MemberFindpwPage";
import MemberSignupPage from "../../pages/cust/member/MemberSignupPage";
import MemberSignoutPage from "../../pages/cust/member/MemberSignoutPage";
import MemberProfilePage from "../../pages/cust/member/MemberProfilePage";

export default inject()(
  observer(
    withRouter(({ match }) => {
      return (
        <Switch>
          {/** 멤버 > 로그인 */}
          <Route
            exact
            path={`${match.url}/login`}
            component={MemberLoginPage}
          />
          {/** 멤버 > 로그인 > 토큰 */}
          <Route
            exact
            path={`${match.url}/login/token`}
            component={MemberLoginTokenPage}
          />
          {/** 멤버 > 비밀번호찾기 */}
          <Route
            exact
            path={`${match.url}/findpw`}
            component={MemberFindpwPage}
          />
          {/** 멤버 > 가입하기 */}
          <Route
            exact
            path={`${match.url}/signup`}
            component={MemberSignupPage}
          />
          {/** 멤버 > 탈퇴하기 */}
          <Route
            exact
            path={`${match.url}/signout`}
            component={MemberSignoutPage}
          />
          {/** 멤버 > 프로필보기 */}
          <Route
            exact
            path={`${match.url}/profile/:id`}
            component={MemberProfilePage}
          />
        </Switch>
      );
    })
  )
);
