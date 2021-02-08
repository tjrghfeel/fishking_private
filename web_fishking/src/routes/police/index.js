import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";

import BlankPage from "../../pages/BlankPage";

export default inject("PageStore")(
  observer(({ PageStore, history, match }) => {
    PageStore.setHistory(history);
    return (
      <BrowserRouter>
        <Switch>
          {/** 빈화면 */}
          <Route path={`${match.url}/blank`} component={BlankPage} />

          {/** 기본 리디렉션 */}
          <Redirect from={`*`} to={`${match.url}/blank`} />
        </Switch>
      </BrowserRouter>
    );
  })
);
