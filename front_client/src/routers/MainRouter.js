import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import MainBottomTabs from "../components/layouts/MainBottomTabs";

import MainMyPage from "../views/main/MainMyView";
import BlankPage from "../views/BlankPage";

export default inject()(
  observer(({ match }) => {
    return (
      <>
        <Switch>
          {/** 메인 > 홈 */}
          <Route
            exact
            path={`${match.url}/home`}
            component={() => <div>Home</div>}
          />
          {/** 메인 > 선상 */}
          <Route exact path={`${match.url}/boat`} component={BlankPage} />
          {/** 메인 > 갯바위 */}
          <Route exact path={`${match.url}/rocks`} component={BlankPage} />
          {/** 메인 > 어복스토리 */}
          <Route exact path={`${match.url}/story`} component={BlankPage} />
          {/** 메인 > 마이메뉴 */}
          <Route exact path={`${match.url}/my`} component={MainMyPage} />
        </Switch>
        <MainBottomTabs />
      </>
    );
  })
);
