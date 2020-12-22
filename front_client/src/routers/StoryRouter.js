import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import BlankPage from "../views/BlankPage";

export default inject()(
  observer(({ match }) => {
    return (
      <>
        <Switch>
          {/** 스토리 > 글쓰기 */}
          <Route exact path={`${match.url}/add`} component={BlankPage} />
          {/** 스토리 > 상세 */}
          <Route exact path={`${match.url}/detail/:id`} component={BlankPage} />
          {/** 스토리 > 댓글 */}
          <Route
            exact
            path={`${match.url}/comment/:id`}
            component={BlankPage}
          />
        </Switch>
      </>
    );
  })
);
