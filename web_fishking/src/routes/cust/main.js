import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import MainHomePage from "../../pages/cust/main/MainHomePage";
import MainCompanyPage from "../../pages/cust/main/MainCompanyPage";
import MainRockPage from "../../pages/cust/main/MainRockPage";
import MainStoryDiaryPage from "../../pages/cust/main/MainStoryDiaryPage";
import MainStoryTvPage from "../../pages/cust/main/MainStoryTvPage";
import MainStoryUserPage from "../../pages/cust/main/MainStoryUserPage";
import MainMyPage from "../../pages/cust/main/MainMyPage";

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 메인 > 홈 */}
          <Route exact path={`${match.url}/home`} component={MainHomePage} />
          {/** 메인 > 업체 > 선상 | 갯바위 */}
          <Route
            exact
            path={`${match.url}/company/:fishingType`}
            component={MainCompanyPage}
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
      </BrowserRouter>
    );
  })
);
