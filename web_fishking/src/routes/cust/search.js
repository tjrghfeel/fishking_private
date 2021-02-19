import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import SearchAllPage from "../../pages/cust/search/SearchAllPage";
import SearchReservePage from "../../pages/cust/search/SearchReservePage";

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 검색 > 전체 */}
          <Route exact path={`${match.url}/all`} component={SearchAllPage} />

          {/** 검색 > 예약 */}
          <Route
            exact
            path={`${match.url}/reserve`}
            component={SearchReservePage}
          />
        </Switch>
      </BrowserRouter>
    );
  })
);
