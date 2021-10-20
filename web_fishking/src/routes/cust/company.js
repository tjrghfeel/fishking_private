import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route, withRouter } from "react-router-dom";

import CompanyDetailPage from "../../pages/cust/company/CompanyDetailPage";
import CompanyReviewPage from "../../pages/cust/company/CompanyReviewPage";
import CompanySeaFcstPage from "../../pages/cust/company/CompanySeaFcstPage";
import CameraPointDetailPage from "../../pages/cust/company/CameraPointDetailPage";

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
          {/** new메인홈 > 카메라 포인트 클릭 */}
          <Route
              exact
              path={`${match.url}/cameraPoint/:fishingType/detail/:id`}
              component={CameraPointDetailPage}
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
