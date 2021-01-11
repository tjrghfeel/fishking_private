import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";
import Views from "../../views";

export default inject()(
  observer(({ match }) => {
    /********** ********** ********** ********** **********/
    /** render */
    /********** ********** ********** ********** **********/
    return (
      <BrowserRouter>
        <Switch>
          {/** 메인 > 홈 */}
          <Route exact path={`${match.url}/home`} component={Views.BlankPage} />
          {/** 메인 > 선상 */}
          <Route exact path={`${match.url}/boat`} component={Views.BlankPage} />
          {/** 메인 > 갯바위 */}
          <Route exact path={`${match.url}/rock`} component={Views.BlankPage} />
          {/** 메인 > 어복스토리 */}
          <Route
            exact
            path={`${match.url}/story`}
            component={Views.BlankPage}
          />
          {/** 메인 > 마이메뉴 */}
          <Route
            exact
            path={`${match.url}/my`}
            component={Views.FishkingMainMyView}
          />
        </Switch>
      </BrowserRouter>
    );
  })
);
