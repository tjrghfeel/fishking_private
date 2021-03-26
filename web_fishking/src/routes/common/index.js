import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route, withRouter } from "react-router-dom";

import MapViewPage from "../../pages/common/MapViewPage";
import MapSearchPage from "../../pages/common/MapSearchPage";

export default inject("PageStore")(
  observer(
    withRouter(({ PageStore, history, match }) => {
      // # >>>>> 기본 설정
      PageStore.setHistory(history);
      return (
        <BrowserRouter>
          <Switch>
            {/** 공통 > 업체 위치 지도보기 */}
            <Route path={`${match.url}/mapview`} component={MapViewPage} />
            {/** 공통 > 업체 검색 지도보기 */}
            <Route path={`${match.url}/mapsearch`} component={MapSearchPage} />
          </Switch>
        </BrowserRouter>
      );
    })
  )
);
