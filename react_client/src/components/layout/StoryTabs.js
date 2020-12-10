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
              (pathname.indexOf(`/main/story/main`) !== -1
                ? " ".concat("active")
                : "")
            }
            onClick={() => history.push(`/main/story/main`)}
          >
            조황일지
          </a>
          <a
            className={
              "nav-link" +
              (pathname.indexOf(`/main/story/tv`) !== -1
                ? " ".concat("active")
                : "")
            }
            onClick={() => history.push(`/main/story/tv`)}
          >
            어복TV
          </a>
          <a
            className={
              "nav-link" +
              (pathname.indexOf(`/main/story/user`) !== -1
                ? " ".concat("active")
                : "")
            }
            onClick={() => history.push(`/main/story/user`)}
          >
            유저조행기
          </a>
        </nav>
      );
    })
  )
);
