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
              (pathname.indexOf(`/common/zzim/boat`) !== -1
                ? " ".concat("active")
                : "")
            }
            onClick={() => history.push(`/common/zzim/boat`)}
          >
            선상
          </a>
          <a
            className={
              "nav-link" +
              (pathname.indexOf(`/common/zzim/rock`) !== -1
                ? " ".concat("active")
                : "")
            }
            onClick={() => history.push(`/common/zzim/rock`)}
          >
            갯바위
          </a>
        </nav>
      );
    })
  )
);
