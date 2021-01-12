import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";
import Pages from "../../pages";
const {
  BlankPage,
  Fishking: {
    Reservation: { MyPage },
  },
} = Pages;

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 어복황제 > 예약 > 나의예약내역 */}
          <Route exact path={`${match.url}/my`} component={MyPage} />
        </Switch>
      </BrowserRouter>
    );
  })
);
