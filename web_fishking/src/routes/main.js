import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import MainHomePage from "../pages/main/MainHomePage";
import MainBoatPage from "../pages/main/MainBoatPage";
import MainRockPage from "../pages/main/MainRockPage";
import MainStoryPage from "../pages/main/MainStoryPage";
import MainMyPage from "../pages/main/MainMyPage";

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 메인 > 홈 */}
          <Route exact path={`${match.url}/home`} component={MainHomePage} />
          {/** 메인 > 선상 */}
          <Route exact path={`${match.url}/boat`} component={MainBoatPage} />
          {/** 메인 > 갯바위 */}
          <Route exact path={`${match.url}/rock`} component={MainRockPage} />
          {/** 메인 > 스토리 */}
          <Route exact path={`${match.url}/story`} component={MainStoryPage} />
          {/** 메인 > 마이 */}
          <Route exact path={`${match.url}/my`} component={MainMyPage} />
        </Switch>
      </BrowserRouter>
    );
  })
);
