import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import ReservationMyPage from "../../pages/cust/reservation/ReservationMyPage";
import ReservationMyDetailPage from "../../pages/cust/reservation/ReservationMyDetailPage";
import ReservationReviewAddPage from "../../pages/cust/reservation/ReservationReviewAddPage";

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
          {/** 예약 > 리뷰작성 */}
          <Route
            exact
            path={`${match.url}/review/add/:goodsId`}
            component={ReservationReviewAddPage}
          />
        </Switch>
      </BrowserRouter>
    );
  })
);
