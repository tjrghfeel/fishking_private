import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import MemberLoginPage from "../pages/member/MemberLoginPage";
import MemberFindpwPage from "../pages/member/MemberFindpwPage";
import MemberSignupPage from "../pages/member/MemberSignupPage";
import MemberSignoutPage from "../pages/member/MemberSignoutPage";
import BlankPage from "../pages/BlankPage";

export default inject()(
  observer(({ match }) => {
    return (
      <>
        <Switch>
          {/** 멤버 > 로그인 */}
          <Route
            exact
            path={`${match.url}/login`}
            component={MemberLoginPage}
          />
          {/** 멤버 > 비밀번호찾기 */}
          <Route
            exact
            path={`${match.url}/findpw`}
            component={MemberFindpwPage}
          />
          {/** 멤버 > 회원가입 */}
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
      </>
    );
  })
);
