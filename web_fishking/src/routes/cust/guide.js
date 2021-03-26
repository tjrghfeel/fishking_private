import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route, withRouter } from "react-router-dom";

import GuideMainPage from "../../pages/cust/guide/GuideMainPage";
import GuideTimePage from "../../pages/cust/guide/GuideTimePage";
import GuideAllPage from "../../pages/cust/guide/GuideAllPage";
import GuideBoatPage from "../../pages/cust/guide/GuideBoatPage";
import GuideRockPage from "../../pages/cust/guide/GuideRockPage";
import GuideSmartPage from "../../pages/cust/guide/GuideSmartPage";
import GuideLivePage from "../../pages/cust/guide/GuideLivePage";

export default inject()(
  observer(
    withRouter(({ match }) => {
      return (
        <Switch>
          {/** 가이드 > 메인 */}
          <Route exact path={`${match.url}/main`} component={GuideMainPage} />

          {/** 가이드 > 시간.체험 */}
          <Route exact path={`${match.url}/time`} component={GuideTimePage} />

          {/** 가이드 > 종일.생활 */}
          <Route exact path={`${match.url}/all`} component={GuideAllPage} />

          {/** 가이드 > 선상낚시 */}
          <Route exact path={`${match.url}/boat`} component={GuideBoatPage} />

          {/** 가이드 > 갯바위낚시 */}
          <Route exact path={`${match.url}/rock`} component={GuideRockPage} />

          {/** 가이드 > 스마트인검 */}
          <Route exact path={`${match.url}/smart`} component={GuideSmartPage} />

          {/** 가이드 > 실시간조황 */}
          <Route exact path={`${match.url}/live`} component={GuideLivePage} />
        </Switch>
      );
    })
  )
);
