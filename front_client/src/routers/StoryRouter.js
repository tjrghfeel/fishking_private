import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import StoryAddView from "../views/story/StoryAddView";
import BlankPage from "../views/BlankPage";
import ViewStore from "../stores/ViewStore";

export default inject("ViewStore")(
  observer(({ match, history }) => {
    ViewStore.setHistory(history);
    return (
      <>
        <Switch>
          {/** 스토리 > 글쓰기 */}
          <Route exact path={`${match.url}/add`} component={StoryAddView} />
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
