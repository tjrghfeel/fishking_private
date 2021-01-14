import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import StoryAddPage from "../pages/story/StoryAddPage";
import StoryMyPostPage from "../pages/story/StoryMyPostPage";
import StoryMyCommentPage from "../pages/story/StoryMyCommentPage";
import StoryMyScrapPage from "../pages/story/StoryMyScrapPage";
import StoryMyReviewPage from "../pages/story/StoryMyReviewPage";

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 스토리 > 글쓰기 */}
          <Route exact path={`${match.url}/add`} component={StoryAddPage} />

          {/** 스토리 > 내글관리 > 게시글 */}
          <Route
            exact
            path={`${match.url}/my/post`}
            component={StoryMyPostPage}
          />
          {/** 스토리 > 내글관리 > 댓글 */}
          <Route
            exact
            path={`${match.url}/my/comment`}
            component={StoryMyCommentPage}
          />
          {/** 스토리 > 내글관리 > 스크랩 */}
          <Route
            exact
            path={`${match.url}/my/scrap`}
            component={StoryMyScrapPage}
          />
          {/** 스토리 > 내글관리 > 리뷰 */}
          <Route
            exact
            path={`${match.url}/my/review`}
            component={StoryMyReviewPage}
          />

          {/** 스토리 > 조황일지 */}
          <Route exact path={`${match.url}/post`} component={StoryAddPage} />
          {/** 스토리 > 어복TV */}
          <Route exact path={`${match.url}/tv`} component={StoryAddPage} />
          {/** 스토리 > 유저조행기 */}
          <Route exact path={`${match.url}/user`} component={StoryAddPage} />
        </Switch>
      </BrowserRouter>
    );
  })
);
