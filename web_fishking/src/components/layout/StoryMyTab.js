import React from "react";
import { inject, observer } from "mobx-react";

export default inject("PageStore")(
  observer(({ PageStore, activeIndex }) => {
    return (
      <nav className="nav nav-pills nav-menu nav-justified">
        <a
          className={"nav-link" + (activeIndex === 0 ? " active" : "")}
          onClick={() => PageStore.push(`/story/my/post`)}
        >
          게시글
        </a>
        <a
          className={"nav-link" + (activeIndex === 1 ? " active" : "")}
          onClick={() => PageStore.push(`/story/my/comment`)}
        >
          댓글
        </a>
        <a
          className={"nav-link" + (activeIndex === 2 ? " active" : "")}
          onClick={() => PageStore.push(`/story/my/scrap`)}
        >
          스크랩
        </a>
        <a
          className={"nav-link" + (activeIndex === 3 ? " active" : "")}
          onClick={() => PageStore.push(`/story/my/review`)}
        >
          리뷰
        </a>
      </nav>
    );
  })
);
