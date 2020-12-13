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
              (pathname.indexOf("/search/all") !== -1
                ? " ".concat("active")
                : "")
            }
            onClick={() => history.push(`/search/all`)}
          >
            전체
          </a>
          <a
            className={
              "nav-link" +
              (pathname.indexOf("/search/reserve") !== -1
                ? " ".concat("active")
                : "")
            }
            onClick={() => history.push(`/search/reserve`)}
          >
            예약
          </a>
        </nav>
      );
    })
  )
);
