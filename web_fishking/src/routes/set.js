import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import SetMainPage from "../pages/set/SetMainPage";
import SetProfilePage from "../pages/set/SetProfilePage";
import SetProfileNickname from "../pages/set/SetProfileNickname";
import SetProfileStatusPage from "../pages/set/SetProfileStatusPage";
import SetProfileEmailPage from "../pages/set/SetProfileEmailPage";
import SetProfilePasswordPage from "../pages/set/SetProfilePasswordPage";
import SetAlarmPage from "../pages/set/SetAlarmPage";
import SetVodPage from "../pages/set/SetVodPage";

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 설정 > 메인 */}
          <Route exact path={`${match.url}/main`} component={SetMainPage} />

          {/** 설정 > 프로필관리 */}
          <Route
            exact
            path={`${match.url}/profile`}
            component={SetProfilePage}
          />
          {/** 설정 > 프로필관리 > 닉네임 */}
          <Route
            exact
            path={`${match.url}/profile/nickname`}
            component={SetProfileNickname}
          />
          {/** 설정 > 프로필관리 > 상태메시지 */}
          <Route
            exact
            path={`${match.url}/profile/status`}
            component={SetProfileStatusPage}
          />
          {/** 설정 > 프로필관리 > 이메일변경 */}
          <Route
            exact
            path={`${match.url}/profile/email`}
            component={SetProfileEmailPage}
          />
          {/** 설정 > 프로필관리 > 비밀번호변경 */}
          <Route
            exact
            path={`${match.url}/profile/password`}
            component={SetProfilePasswordPage}
          />
          {/** 설정 > 알림설정 */}
          <Route exact path={`${match.url}/alarm`} component={SetAlarmPage} />
          {/** 설정 > 동영상설정 */}
          <Route exact path={`${match.url}/vod`} component={SetVodPage} />
        </Switch>
      </BrowserRouter>
    );
  })
);
