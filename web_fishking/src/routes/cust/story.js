import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route, withRouter } from "react-router-dom";

import StoryAddPage from "../../pages/cust/story/StoryAddPage";
import StoryMyPostPage from "../../pages/cust/story/StoryMyPostPage";
import StoryMyCommentPage from "../../pages/cust/story/StoryMyCommentPage";
import StoryMyScrapPage from "../../pages/cust/story/StoryMyScrapPage";
import StoryMyReviewPage from "../../pages/cust/story/StoryMyReviewPage";
import StoryDetailPage from "../../pages/cust/story/StoryDetailPage";
import StoryCommentPage from "../../pages/cust/story/StoryCommentPage";
import StoryTvLiveDetailPage from "../../pages/cust/story/StoryTvLiveDetailPage";
import StoryTvTubeDetailPage from "../../pages/cust/story/StoryTvTubeDetailPage";

export default inject()(
  observer(
    withRouter(({ match }) => {
      return (
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
          {/** 스토리 > 조황일지/유저조행기 > 상세 */}
          <Route
            exact
            path={`${match.url}/:category/detail/:id`}
            component={StoryDetailPage}
          />
          {/** 스토리 > 조황일지/유저조행기 > 댓글 */}
          <Route
            exact
            path={`${match.url}/:category/comment/:id`}
            component={StoryCommentPage}
          />
          {/** 스토리 > 어복TV > 라이브 */}
          <Route
            exact
            path={`${match.url}/tv/:shipId/:cameraId`}
            component={StoryTvLiveDetailPage}
          />
          {/** 스토리 > 어복TV > 유튜브 */}
          <Route
            exact
            path={`${match.url}/tv/:id`}
            component={StoryTvTubeDetailPage}
          />
        </Switch>
      );
    })
  )
);
