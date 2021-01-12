import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";
import Pages from "../../pages";
const {
  Common: {
    Set: {
      MainPage,
      ProfilePage,
      ProfileNicknamePage,
      ProfileStatusPage,
      ProfileEmailPage,
      ProfilePasswordPage,
      AlarmPage,
      VodPage,
    },
  },
} = Pages;

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 공통 > 설정 > 메인 */}
          <Route exact path={`${match.url}/main`} component={MainPage} />

          {/** 공통 > 설정 > 메인 > 프로필관리 */}
          <Route exact path={`${match.url}/profile`} component={ProfilePage} />
          {/** 공통 > 설정 > 메인 > 프로필관리 > 닉네임변경 */}
          <Route
            exact
            path={`${match.url}/profile/nickname`}
            component={ProfileNicknamePage}
          />
          {/** 공통 > 설정 > 메인 > 프로필관리 > 상태메시지변경 */}
          <Route
            exact
            path={`${match.url}/profile/status`}
            component={ProfileStatusPage}
          />
          {/** 공통 > 설정 > 메인 > 프로필관리 > 이메일변경 */}
          <Route
            exact
            path={`${match.url}/profile/email`}
            component={ProfileEmailPage}
          />
          {/** 공통 > 설정 > 메인 > 프로필관리 > 비밀번호변경 */}
          <Route
            exact
            path={`${match.url}/profile/password`}
            component={ProfilePasswordPage}
          />

          {/** 공통 > 설정 > 메인 > 알림설정 */}
          <Route exact path={`${match.url}/alarm`} component={AlarmPage} />
          {/** 공통 > 설정 > 메인 > 동영상설정 */}
          <Route exact path={`${match.url}/vod`} component={VodPage} />
        </Switch>
      </BrowserRouter>
    );
  })
);
