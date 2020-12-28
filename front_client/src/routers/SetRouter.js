import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import SetMainView from "../views/set/SetMainView";
import SetProfileView from "../views/set/SetProfileView";
import SetProfileNickNameView from "../views/set/SetProfileNickNameView";
import SetProfileStatusMessageView from "../views/set/SetProfileStatusMessageView";
import SetProfileEmailView from "../views/set/SetProfileEmailView";
import SetProfilePasswordView from "../views/set/SetProfilePasswordView";
import SetAlarmView from "../views/set/SetAlarmView";
import SetVodView from "../views/set/SetVodView";
import BlankPage from "../views/BlankPage";

export default inject()(
  observer(({ match }) => {
    return (
      <>
        <Switch>
          {/** 설정 > 메인 */}
          <Route exact path={`${match.url}/main`} component={SetMainView} />
          {/** 설정 > 프로필관리 */}
          <Route
            exact
            path={`${match.url}/profile`}
            component={SetProfileView}
          />
          {/** 설정 > 프로필관리 > 닉네임변경 */}
          <Route
            exact
            path={`${match.url}/profile/nickName`}
            component={SetProfileNickNameView}
          />
          {/** 설정 > 프로필관리 > 상태메시지 */}
          <Route
            exact
            path={`${match.url}/profile/statusMessage`}
            component={SetProfileStatusMessageView}
          />
          {/** 설정 > 프로필관리 > 휴대폰번호변경 */}
          <Route
            exact
            path={`${match.url}/profile/mobile`}
            component={BlankPage}
          />
          {/** 설정 > 프로필관리 > 이메일변경 */}
          <Route
            exact
            path={`${match.url}/profile/email`}
            component={SetProfileEmailView}
          />
          {/** 설정 > 프로필관리 > 비밀번호변경 */}
          <Route
            exact
            path={`${match.url}/profile/password`}
            component={SetProfilePasswordView}
          />
          {/** 설정 > 알림설정 */}
          <Route exact path={`${match.url}/alarm`} component={SetAlarmView} />
          {/** 설정 > 접근권한설정 */}
          <Route exact path={`${match.url}/device`} component={BlankPage} />
          {/** 설정 > 동영상설정 */}
          <Route exact path={`${match.url}/vod`} component={SetVodView} />
        </Switch>
      </>
    );
  })
);
