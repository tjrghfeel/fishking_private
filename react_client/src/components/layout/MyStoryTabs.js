/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router";

export default inject()(
  observer(
    withRouter(({ history, location: { pathname } }) => {
      return (
        <nav className="nav nav-pills nav-menu nav-justified">
          <a
            className={
              "nav-link" +
              (pathname.indexOf(`/story/my/post`) !== -1
                ? " ".concat("active")
                : "")
            }
            onClick={() => history.push(`/story/my/post`)}
          >
            게시글
          </a>
          <a
            className={
              "nav-link" +
              (pathname.indexOf(`/story/my/comment`) !== -1
                ? " ".concat("active")
                : "")
            }
            onClick={() => history.push(`/story/my/comment`)}
          >
            댓글
          </a>
          <a
            className={
              "nav-link" +
              (pathname.indexOf(`/story/my/scrap`) !== -1
                ? " ".concat("active")
                : "")
            }
            onClick={() => history.push(`/story/my/scrap`)}
          >
            스크랩
          </a>
          <a
            className={
              "nav-link" +
              (pathname.indexOf(`/story/my/review`) !== -1
                ? " ".concat("active")
                : "")
            }
            onClick={() => history.push(`/story/my/review`)}
          >
            리뷰
          </a>
        </nav>
      );
    })
  )
);
