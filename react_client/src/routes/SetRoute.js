import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import SetMainPage from "../pages/set/SetMainPage";
import SetProfilePage from "../pages/set/SetProfilePage";
import SetProfileNickname from "../pages/set/SetProfileNicknamePage";
import SetProfileStatus from "../pages/set/SetProfileStatusPage";
import SetProfileEmail from "../pages/set/SetProfileEmailPage";
import SetProfilePassword from "../pages/set/SetProfilePasswordPage";
import SetAlarm from "../pages/set/SetAlarmPage";
import SetVod from "../pages/set/SetVodPage";
import SetPolicy from "../pages/set/SetPolicyPage";
import BlankPage from "../pages/BlankPage";

export default inject()(
  observer(({ match }) => {
    return (
      <>
        <Switch>
          {/** 설정 > 메인 */}
          <Route exact path={`${match.url}/main`} component={SetMainPage} />
          {/** 설정 > 프로필 관리 */}
          <Route
            exact
            path={`${match.url}/profile`}
            component={SetProfilePage}
          />
          {/** 설정 > 프로필 관리 > 닉네임 */}
          <Route
            exact
            path={`${match.url}/profile/nickname`}
            component={SetProfileNickname}
          />
          {/** 설정 > 프로필 관리 > 상태메시지 */}
          <Route
            exact
            path={`${match.url}/profile/status`}
            component={SetProfileStatus}
          />
          {/** 설정 > 프로필 관리 > 이메일 */}
          <Route
            exact
            path={`${match.url}/profile/email`}
            component={SetProfileEmail}
          />
          {/** 설정 > 프로필 관리 > 비밀번호 */}
          <Route
            exact
            path={`${match.url}/profile/password`}
            component={SetProfilePassword}
          />
          {/** 설정 > 알림설정 */}
          <Route exact path={`${match.url}/alarm`} component={SetAlarm} />
          {/** 설정 > 동영상설정 */}
          <Route exact path={`${match.url}/vod`} component={SetVod} />
          {/** 설정 > 약관및정책 */}
          <Route exact path={`${match.url}/policy`} component={SetPolicy} />
        </Switch>
      </>
    );
  })
);
