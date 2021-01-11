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
              (pathname.indexOf("/common/cs/faq") !== -1
                ? " ".concat("active")
                : "")
            }
            onClick={() => history.push(`/common/cs/faq`)}
          >
            자주하는 질문
          </a>
          <a
            className={
              "nav-link" +
              (pathname.indexOf("/common/cs/apply") !== -1
                ? " ".concat("active")
                : "")
            }
            onClick={() => history.push(`/common/cs/apply`)}
          >
            업체등록요청
          </a>
          <a
            className={
              "nav-link" +
              (pathname.indexOf("/common/cs/qna") !== -1
                ? " ".concat("active")
                : "")
            }
            onClick={() => history.push(`/common/cs/qna`)}
          >
            1:1문의하기
          </a>
        </nav>
      );
    })
  )
);
