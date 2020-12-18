import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import CommonAlarmPage from "../pages/common/CommonAlarmPage";

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
        </Switch>
      </>
    );
  })
);
