import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import MemberLoginPage from "../pages/member/MemberLoginPage";
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
          <Route exact path={`${match.url}/findpw`} component={BlankPage} />
          {/** 멤버 > 회원가입 */}
          <Route exact path={`${match.url}/signup`} component={BlankPage} />
        </Switch>
      </>
    );
  })
);
