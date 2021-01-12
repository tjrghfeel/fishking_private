import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";
import Pages from "../../pages";
const {
  Common: {
    Alarm: { MyPage },
  },
} = Pages;

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 공통 > 알림 > 기본 */}
          <Route exact path={`${match.url}/my`} component={MyPage} />
        </Switch>
      </BrowserRouter>
    );
  })
);
