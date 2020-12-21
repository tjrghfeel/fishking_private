import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import CommonAlarmPage from "../pages/common/CommonAlarmPage";
import CommonZzimMyPage from "../pages/common/CommonZzimMyPage";
import CommonZzimMyBoatPage from "../pages/common/CommonZzimMyBoatPage";
import CommonZzimMyRockPage from "../pages/common/CommonZzimMyRockPage";

export default inject()(
  observer(({ match }) => {
    return (
      <>
        <Switch>
          {/** 메인 > 마이메뉴 > 알림 */}
          <Route
            exact
            path={`${match.url}/alarm`}
            component={CommonAlarmPage}
          />
          {/** 메인 > 찜한업체 */}
          <Route
            exact
            path={`${match.url}/zzim/my`}
            component={CommonZzimMyPage}
          />
          {/** 메인 > 찜한업체 > 선상 */}
          <Route
            exact
            path={`${match.url}/zzim/boat`}
            component={CommonZzimMyBoatPage}
          />
          {/** 메인 > 찜한업체 > 갯바위 */}
          <Route
            exact
            path={`${match.url}/zzim/rock`}
            component={CommonZzimMyRockPage}
          />
        </Switch>
      </>
    );
  })
);
