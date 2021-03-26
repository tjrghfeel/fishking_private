import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route, withRouter } from "react-router-dom";

import ZzimMainPage from "../../pages/cust/zzim/ZzimMainPage";
import ZzimBoatPage from "../../pages/cust/zzim/ZzimBoatPage";
import ZzimRockPage from "../../pages/cust/zzim/ZzimRockPage";

export default inject()(
  observer(
    withRouter(({ match }) => {
      return (
        <Switch>
          {/** 찜 > 홈 */}
          <Route exact path={`${match.url}/main`} component={ZzimMainPage} />
          {/** 찜 > 선상 */}
          <Route exact path={`${match.url}/boat`} component={ZzimBoatPage} />
          {/** 찜 > 갯바위 */}
          <Route exact path={`${match.url}/rock`} component={ZzimRockPage} />
        </Switch>
      );
    })
  )
);
