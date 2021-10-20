import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route, withRouter } from "react-router-dom";

import CompanyDetailPage from "../../pages/cust/company/CompanyDetailPage";
import CompanyReviewPage from "../../pages/cust/company/CompanyReviewPage";
import HarborDetailPage from "../../pages/cust/company/HarborDetailPage";
import CompanySeaFcstPage from "../../pages/cust/company/CompanySeaFcstPage";

export default inject()(
  observer(
    withRouter(({ match }) => {
      return (
        <Switch>
          {/** 업체 > 선상/갯바위 > 상세 */}
          <Route
            exact
            path={`${match.url}/:fishingType/detail/:id`}
            component={CompanyDetailPage}
          />
          {/** 업체 > 선상/갯바위 > 상세 > 리뷰 */}
          <Route
            exact
            path={`${match.url}/review/:id`}
            component={CompanyReviewPage}
          />
          {/** new메인홈 > 항구클릭 */}
          <Route
              exact
              path={`${match.url}/harbor/:fishingType/detail/:id`}
              component={HarborDetailPage}
          />
          {/** 업체 > 해상예보 */}
          <Route
              exact
              path={`${match.url}/:fishingType/seaFcst/:id`}
              component={CompanySeaFcstPage}
          />
        </Switch>
      );
    })
  )
);
