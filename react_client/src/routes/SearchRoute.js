import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import SearchMainPage from "../pages/search/SearchAllPage";
import SearchReservePage from "../pages/search/SearchReservePage";
import BlankPage from "../pages/BlankPage";

export default inject()(
  observer(({ match }) => {
    return (
      <>
        <Switch>
          {/** 검색 > 전체 */}
          <Route exact path={`${match.url}/all`} component={SearchMainPage} />
          {/** 검색 > 예약 */}
          <Route
            exact
            path={`${match.url}/reserve`}
            component={SearchReservePage}
          />
        </Switch>
      </>
    );
  })
);
