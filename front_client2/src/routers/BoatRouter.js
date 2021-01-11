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
          {/** 선상 > 메인 */}
          <Route exact path={`${match.url}/main`} component={BlankPage} />
        </Switch>
      </>
    );
  })
);
