import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";
import Pages from "../../pages";
const {
  BlankPage,
  Fishking: {
    Common: { NoticePage, NoticeDetailPage },
  },
} = Pages;

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 어복황제 > 공통 > 공지사항 */}
          <Route exact path={`${match.url}/notice`} component={NoticePage} />
          {/** 어복황제 > 공통 > 공지사항 > 상세 */}
          <Route
            exact
            path={`${match.url}/notice/:id`}
            component={NoticeDetailPage}
          />
        </Switch>
      </BrowserRouter>
    );
  })
);
