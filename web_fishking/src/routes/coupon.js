import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import CouponMyPage from "../pages/coupon/CouponMyPage";
import CouponAvailablePage from "../pages/coupon/CouponAvailablePage";

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
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
      </BrowserRouter>
    );
  })
);
