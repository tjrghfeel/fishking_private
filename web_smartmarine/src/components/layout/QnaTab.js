import React from "react";
import { inject, observer } from "mobx-react";

export default inject("PageStore")(
  observer(({ PageStore, activeIndex }) => {
    return (
      <nav className="nav nav-pills nav-menu nav-justified">
        <a
          className={"nav-link" + (activeIndex === 0 ? " active" : "")}
          onClick={() => PageStore.push(`/cs/qna/add`)}
        >
          문의하기
        </a>
        <a
          className={"nav-link" + (activeIndex === 1 ? " active" : "")}
          onClick={() => PageStore.push(`/cs/qna/list`)}
        >
          문의내역
        </a>
      </nav>
    );
  })
);
