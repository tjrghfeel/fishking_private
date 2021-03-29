import React from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router-dom";

export default inject("PageStore")(
  observer(
    withRouter(({ activeIndex = 0, keyword, PageStore, onClick }) => {
      return (
        <nav className="nav nav-pills nav-menu nav-justified">
          <a
            className={"nav-link" + (activeIndex === 0 ? " active" : "")}
            onClick={() =>
              PageStore.push(`/search/keyword/all?keyword=${keyword}`)
            }
          >
            통합
          </a>
          <a
            className={"nav-link" + (activeIndex === 1 ? " active" : "")}
            onClick={() => {
              PageStore.push(`/search/keyword/ship?keyword=${keyword}`);
              if (onClick) onClick("ship");
            }}
          >
            선박
          </a>
          <a
            className={"nav-link" + (activeIndex === 2 ? " active" : "")}
            onClick={() => {
              PageStore.push(`/search/keyword/diary?keyword=${keyword}`);
              if (onClick) onClick("diary");
            }}
          >
            조황일지
          </a>
          <a
            className={"nav-link" + (activeIndex === 3 ? " active" : "")}
            onClick={() => {
              PageStore.push(`/search/keyword/live?keyword=${keyword}`);
              if (onClick) onClick("live");
            }}
          >
            어복TV
          </a>
          <a
            className={"nav-link" + (activeIndex === 4 ? " active" : "")}
            onClick={() => {
              PageStore.push(`/search/keyword/blog?keyword=${keyword}`);
              if (onClick) onClick("blog");
            }}
          >
            조행기
          </a>
        </nav>
      );
    })
  )
);
