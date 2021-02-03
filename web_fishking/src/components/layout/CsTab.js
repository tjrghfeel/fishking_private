import React from "react";
import { inject, observer } from "mobx-react";

export default inject("PageStore")(
  observer(({ PageStore, activeIndex }) => {
    return (
      <nav className="nav nav-pills nav-menu nav-justified">
        <a
          className={"nav-link" + (activeIndex === 0 ? " active" : "")}
          onClick={() => PageStore.push(`/cs/faq`)}
        >
          자주하는 질문
        </a>
        <a
          className={"nav-link" + (activeIndex === 1 ? " active" : "")}
          onClick={() => PageStore.push(`/cs/apply`)}
        >
          업체등록요청
        </a>
        <a
          className={"nav-link" + (activeIndex === 2 ? " active" : "")}
          onClick={() => PageStore.push(`/cs/qna/add`)}
        >
          1:1문의하기
        </a>
      </nav>
    );
  })
);
