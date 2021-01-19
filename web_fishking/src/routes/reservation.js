import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import ReservationMyPage from "../pages/reservation/ReservationMyPage";
import ReservationMyDetailPage from "../pages/reservation/ReservationMyDetailPage";

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 예약 > 나의예약내역 */}
          <Route exact path={`${match.url}/my`} component={ReservationMyPage} />
          {/** 예약 > 나의예약내역 > 상세 */}
          <Route
            exact
            path={`${match.url}/my/detail/:id`}
            component={ReservationMyDetailPage}
          />
        </Switch>
      </BrowserRouter>
    );
  })
);
