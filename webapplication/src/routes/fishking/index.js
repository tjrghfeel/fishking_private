import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import MainRoute from "./main";
import MemberRoute from "./member";
import ReservationRoute from "./reservation";
import CsRoute from "./cs";
import CommonRoute from "./common";

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 어복황제 > 메인 */}
          <Route path={`${match.url}/main`} component={MainRoute} />

          {/** 어복황제 > 멤버 */}
          <Route path={`${match.url}/member`} component={MemberRoute} />

          {/** 어복황제 > 예약 */}
          <Route
            path={`${match.url}/reservation`}
            component={ReservationRoute}
          />

          {/** 어복황제 > 고객센터 */}
          <Route path={`${match.url}/cs`} component={CsRoute} />

          {/** 어복황제 > 공통 */}
          <Route path={`${match.url}/common`} component={CommonRoute} />
        </Switch>
      </BrowserRouter>
    );
  })
);
