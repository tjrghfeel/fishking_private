import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import BlankPage from "../views/BlankPage";
import ViewStore from "../stores/ViewStore";

export default inject("ViewStore")(
  observer(({ match, history }) => {
    ViewStore.setHistory(history);
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
