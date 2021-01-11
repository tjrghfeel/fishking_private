import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import BlankPage from "../../views/BlankPage";
import FishkingMainMyView from "../../views/fishking/main/FishkingMainMyView";

export default inject()(
  observer(({ match }) => {
    /********** ********** ********** ********** **********/
    /** render */
    /********** ********** ********** ********** **********/
    return (
      <BrowserRouter>
        <Switch>
          {/** 메인 > 홈 */}
          <Route exact={`${match.url}/home`} component={BlankPage} />
          {/** 메인 > 선상 */}
          <Route exact={`${match.url}/boat`} component={BlankPage} />
          {/** 메인 > 갯바위 */}
          <Route exact={`${match.url}/rock`} component={BlankPage} />
          {/** 메인 > 어복스토리 */}
          <Route exact={`${match.url}/story`} component={BlankPage} />
          {/** 메인 > 마이메뉴 */}
          <Route exact={`${match.url}/my`} component={FishkingMainMyView} />
        </Switch>
      </BrowserRouter>
    );
  })
);
