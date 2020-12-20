import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import StoryAddPage from "../pages/story/StoryAddPage";
import StoryMyPostPage from "../pages/story/StoryMyPostPage";

export default inject()(
  observer(({ match }) => {
    return (
      <>
        <Switch>
          {/** 스토리 > 글쓰기 */}
          <Route exact path={`${match.url}/add`} component={StoryAddPage} />
          {/** 스토리 > 내글관리 > 게시글 */}
          <Route
            exact
            path={`${match.url}/my/post`}
            component={StoryMyPostPage}
          />
        </Switch>
      </>
    );
  })
);
