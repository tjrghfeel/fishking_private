import React, { useEffect } from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route, Redirect, BrowserRouter } from "react-router-dom";

import MainRoute from "./MainRoute";
import MemberRoute from "./MemberRoute";

export default inject("RouteStore")(
  observer(({ RouteStore, history, location }) => {
    useEffect(() => {
      // --> Store 를 통해 Route 관련 처리를 할 수 있도록 설정
      RouteStore.setRouteObject(history, location);
    }, [RouteStore, history, location]);
    /** 렌더링 */
    return (
      <BrowserRouter>
        <Switch>
          {/** 메인 */}
          <Route path={`/main`} component={MainRoute} />
          {/** 멤버 */}
          <Route path={`/member`} component={MemberRoute} />
          {/** 기본 라우팅 */}
          <Redirect from={`*`} to={"/main/home"} />
        </Switch>
      </BrowserRouter>
    );
  })
);
