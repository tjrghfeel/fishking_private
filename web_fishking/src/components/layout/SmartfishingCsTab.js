import React from "react";
import { inject, observer } from "mobx-react";

export default inject("PageStore")(
  observer(({ PageStore, activeIndex }) => {
    return (
      <nav className="nav nav-pills nav-menu nav-justified">
        <a
          className={"nav-link" + (activeIndex === 0 ? " active" : "")}
          onClick={() => PageStore.push(`/cs/notice`)}
        >
          공지사항
        </a>
        <a
          className={"nav-link" + (activeIndex === 1 ? " active" : "")}
          onClick={() => PageStore.push(`/cs/faq`)}
        >
          자주하는 질문
        </a>
        <a
          className={"nav-link" + (activeIndex === 2 ? " active" : "")}
          onClick={() => PageStore.push(`/cs/qna/add`)}
        >
          1:1문의
        </a>
        <a
          className={"nav-link" + (activeIndex === 3 ? " active" : "")}
          onClick={() => PageStore.push(`/set/main`)}
        >
          설정
        </a>
      </nav>
    );
  })
);
