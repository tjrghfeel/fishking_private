import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route, withRouter } from "react-router-dom";

import SearchAllPage from "../../pages/cust/search/SearchAllPage";
import SearchReservePage from "../../pages/cust/search/SearchReservePage";
import SearchKeywordAllPage from "../../pages/cust/search/SearchKeywordAllPage";
import SearchKeywordTabPage from "../../pages/cust/search/SearchKeywordTabPage";

export default inject()(
  observer(
    withRouter(({ match }) => {
      return (
        <Switch>
          {/** 검색 > 전체 */}
          <Route exact path={`${match.url}/all`} component={SearchAllPage} />
          {/** 검색 > 예약 */}
          <Route
            exact
            path={`${match.url}/reserve`}
            component={SearchReservePage}
          />
          {/** 검색 > 키워드 > 통합 */}
          <Route
            exact
            path={`${match.url}/keyword/all`}
            component={SearchKeywordAllPage}
          />
          {/** 검색 > 키워드 > 기타 */}
          <Route
            exact
            path={`${match.url}/keyword/:tab`}
            component={SearchKeywordTabPage}
          />
        </Switch>
      );
    })
  )
);
