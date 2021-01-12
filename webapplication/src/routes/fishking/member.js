import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";
import Pages from "../../pages";
const {
  BlankPage,
  Fishking: {
    Member: { LoginPage, FindPwPage, SignupPage, SignoutPage },
  },
} = Pages;

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 어복황제 > 멤버 > 로그인 */}
          <Route exact path={`${match.url}/login`} component={LoginPage} />
          {/** 어복황제 > 멤버 > 비밀번호찾기 */}
          <Route exact path={`${match.url}/findpw`} component={FindPwPage} />
          {/** 어복황제 > 멤버 > 회원가입 */}
          <Route exact path={`${match.url}/signup`} component={SignupPage} />
          {/** 어복황제 > 멤버 > 회원탈퇴 */}
          <Route exact path={`${match.url}/signout`} component={SignoutPage} />
        </Switch>
      </BrowserRouter>
    );
  })
);
