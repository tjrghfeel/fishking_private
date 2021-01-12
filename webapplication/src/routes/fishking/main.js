import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";
import Pages from "../../pages";
const {
  BlankPage,
  Fishking: {
    Main: { MyPage },
  },
} = Pages;

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 어복황제 > 메인 > 홈 */}
          <Route exact path={`${match.url}/home`} component={BlankPage} />
          {/** 어복황제 > 메인 > 마이 */}
          <Route exact path={`${match.url}/my`} component={MyPage} />
        </Switch>
      </BrowserRouter>
    );
  })
);
