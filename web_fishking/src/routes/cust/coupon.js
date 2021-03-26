import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route, withRouter } from "react-router-dom";

import CouponMyPage from "../../pages/cust/coupon/CouponMyPage";
import CouponAvailablePage from "../../pages/cust/coupon/CouponAvailablePage";

export default inject()(
  observer(
    withRouter(({ match }) => {
      return (
        <Switch>
          {/** 쿠폰 > 내쿠폰함 */}
          <Route exact path={`${match.url}/my`} component={CouponMyPage} />
          {/** 쿠폰 > 혜택쿠폰 */}
          <Route
            exact
            path={`${match.url}/available`}
            component={CouponAvailablePage}
          />
        </Switch>
      );
    })
  )
);
