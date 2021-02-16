import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import EventListPage from "../../pages/cust/event/EventListPage";
import EventDetailPage from "../../pages/cust/event/EventDetailPage";
import EventCommentPage from "../../pages/cust/event/EventCommentPage";

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 이벤트 > 목록 */}
          <Route exact path={`${match.url}/list`} component={EventListPage} />

          {/** 이벤트 > 상세 */}
          <Route
            exact
            path={`${match.url}/detail/:eventId`}
            component={EventDetailPage}
          />

          {/** 이벤트 > 댓글 */}
          <Route
            exact
            path={`${match.url}/comment/:eventId`}
            component={EventCommentPage}
          />
        </Switch>
      </BrowserRouter>
    );
  })
);
