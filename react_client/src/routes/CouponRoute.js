import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import CouponAvailablePage from "../pages/coupon/CouponAvailablePage";
import CouponMyPage from "../pages/coupon/CouponMyPage";

export default inject()(
  observer(({ match }) => {
    return (
      <>
        <Switch>
          {/** 쿠폰 > 다운로드 가능한 쿠폰 */}
          <Route
            exact
            path={`${match.url}/available`}
            component={CouponAvailablePage}
          />
          {/** 쿠폰 > 쿠폰함 */}
          <Route exact path={`${match.url}/my`} component={CouponMyPage} />
        </Switch>
      </>
    );
  })
);
