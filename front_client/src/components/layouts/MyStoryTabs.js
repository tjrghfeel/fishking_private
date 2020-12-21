import React from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router-dom";

export default inject()(
  observer(
    withRouter(({ history, location: { pathname } }) => {
      return (
        <nav className="nav nav-pills nav-menu nav-justified">
          <a
            className={
              "nav-link" +
              (pathname.indexOf("/my/story/post") !== -1
                ? " ".concat("active")
                : "")
            }
            onClick={() => history.push(`/my/story/post`)}
          >
            게시글
          </a>
          <a
            className={
              "nav-link" +
              (pathname.indexOf("/my/story/comment") !== -1
                ? " ".concat("active")
                : "")
            }
            onClick={() => history.push(`/my/story/comment`)}
          >
            댓글
          </a>
          <a
            className={
              "nav-link" +
              (pathname.indexOf("/my/story/scrap") !== -1
                ? " ".concat("active")
                : "")
            }
            onClick={() => history.push(`/my/story/scrap`)}
          >
            스크랩
          </a>
          <a
            className={
              "nav-link" +
              (pathname.indexOf("/my/story/review") !== -1
                ? " ".concat("active")
                : "")
            }
            onClick={() => history.push(`/my/story/review`)}
          >
            리뷰
          </a>
        </nav>
      );
    })
  )
);
