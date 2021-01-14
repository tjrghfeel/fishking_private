import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import MemberLoginPage from "../pages/member/MemberLoginPage";
import MemberLoginTokenPage from "../pages/member/MemberLoginTokenPage";
import MemberFindpwPage from "../pages/member/MemberFindpwPage";
import MemberSignupPage from "../pages/member/MemberSignupPage";
import MemberSignoutPage from "../pages/member/MemberSignoutPage";

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
        </Switch>
      </BrowserRouter>
    );
  })
);
