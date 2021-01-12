import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import PolicyRoute from "./policy";
import SetRoute from "./set";
import CouponRoute from "./coupon";
import AlarmRoute from "./alarm";

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 공통 > 약관및정책 */}
          <Route path={`${match.url}/policy`} component={PolicyRoute} />
          {/** 공통 > 설정 */}
          <Route path={`${match.url}/set`} component={SetRoute} />
          {/** 공통 > 쿠폰 */}
          <Route path={`${match.url}/coupon`} component={CouponRoute} />
          {/** 공통 > 알림 */}
          <Route path={`${match.url}/alarm`} component={AlarmRoute} />
        </Switch>
      </BrowserRouter>
    );
  })
);
