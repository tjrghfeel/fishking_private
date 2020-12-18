import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import StoryAddPage from "../pages/story/StoryAddPage";

export default inject()(
  observer(({ match }) => {
    return (
      <>
        <Switch>
          {/** 스토리 > 글쓰기 */}
          <Route exact path={`${match.url}/add`} component={StoryAddPage} />
        </Switch>
      </>
    );
  })
);
