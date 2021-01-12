import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";
import Pages from "../../pages";
const {
  BlankPage,
  Common: {
    Coupon: { MyPage },
  },
} = Pages;

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 공통 > 쿠폰 > 쿠폰함 */}
          <Route exact path={`${match.url}/my`} component={MyPage} />
        </Switch>
      </BrowserRouter>
    );
  })
);
