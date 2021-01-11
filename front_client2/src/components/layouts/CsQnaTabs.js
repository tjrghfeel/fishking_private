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
              (pathname === "/common/cs/qna" ? " ".concat("active") : "")
            }
            onClick={() => history.push(`/common/cs/qna`)}
          >
            문의하기
          </a>
          <a
            className={
              "nav-link" +
              (pathname.indexOf("/common/cs/qna/") !== -1
                ? " ".concat("active")
                : "")
            }
            onClick={() => history.push(`/common/cs/qna/list`)}
          >
            문의내역
          </a>
        </nav>
      );
    })
  )
);
