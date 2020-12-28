import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import BlankPage from "../views/BlankPage";

export default inject()(
  observer(({ match }) => {
    return (
      <>
        <Switch>
          {/** 검색 > 전체 */}
          <Route exact path={`${match.url}/all`} component={BlankPage} />
          {/** 검색 > 예약 */}
          <Route exact path={`${match.url}/reserve`} component={BlankPage} />
        </Switch>
      </>
    );
  })
);
