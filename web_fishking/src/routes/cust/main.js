import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import MainHomePage from "../../pages/cust/main/MainHomePage";
import MainBoatPage from "../../pages/cust/main/MainBoatPage";
import MainRockPage from "../../pages/cust/main/MainRockPage";
import MainStoryDiaryPage from "../../pages/cust/main/MainStoryDiaryPage";
import MainStoryUserPage from "../../pages/cust/main/MainStoryUserPage";
import MainMyPage from "../../pages/cust/main/MainMyPage";

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
          {/** 메인 > 스토리 > 조황일지 */}
          <Route
            exact
            path={`${match.url}/story/diary`}
            component={MainStoryDiaryPage}
          />
          {/** 메인 > 스토리 > 어복TV */}
          <Route
            exact
            path={`${match.url}/story/tv`}
            component={MainStoryDiaryPage}
          />
          {/** 메인 > 스토리 > 유저조행기 */}
          <Route
            exact
            path={`${match.url}/story/user`}
            component={MainStoryUserPage}
          />
          {/** 메인 > 마이 */}
          <Route exact path={`${match.url}/my`} component={MainMyPage} />
        </Switch>
      </BrowserRouter>
    );
  })
);
