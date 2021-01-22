import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import MemberLoginPage from "../pages/member/MemberLoginPage";
import MemberLoginTokenPage from "../pages/member/MemberLoginTokenPage";
import MemberFindpwPage from "../pages/member/MemberFindpwPage";
import MemberSignupPage from "../pages/member/MemberSignupPage";
import MemberSignoutPage from "../pages/member/MemberSignoutPage";
import MemberProfilePage from "../pages/member/MemberProfilePage";

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
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
      </BrowserRouter>
    );
  })
);
