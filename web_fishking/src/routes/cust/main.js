import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route, withRouter } from "react-router-dom";

import MainHomePage from "../../pages/cust/main/MainHomePage";
import MainCompanyBoatPage from "../../pages/cust/main/MainCompanyBoatPage";
import MainCompanyRockPage from "../../pages/cust/main/MainCompanyRockPage";
import MainRockPage from "../../pages/cust/main/MainRockPage";
import MainStoryDiaryPage from "../../pages/cust/main/MainStoryDiaryPage";
import MainStoryTvPage from "../../pages/cust/main/MainStoryTvPage";
import MainStoryUserPage from "../../pages/cust/main/MainStoryUserPage";
import MainMyPage from "../../pages/cust/main/MainMyPage";

export default inject()(
  observer(
    withRouter(({ match }) => {
      return (
        <Switch>
          {/** 메인 > 홈 */}
          <Route exact path={`${match.url}/home`} component={MainHomePage} />
          {/** 메인 > 업체 > 선상 */}
          <Route
            exact
            path={`${match.url}/company/boat`}
            component={MainCompanyBoatPage}
          />
          {/** 메인 > 업체 > 갯바위 */}
          <Route
            exact
            path={`${match.url}/company/rock`}
            component={MainCompanyRockPage}
          />
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
            component={MainStoryTvPage}
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
      );
    })
  )
);
