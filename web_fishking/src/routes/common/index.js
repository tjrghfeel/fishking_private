import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";

import MapViewPage from "../../pages/common/MapViewPage";

export default inject("PageStore")(
  observer(({ PageStore, history, match }) => {
    // # >>>>> 기본 설정
    PageStore.setHistory(history);
    return (
      <BrowserRouter>
        <Switch>
          {/** 공통 > 지도보기 */}
          <Route path={`${match.url}/mapview`} component={MapViewPage} />
        </Switch>
      </BrowserRouter>
    );
  })
);
