import React from "react";
import { inject, observer } from "mobx-react";

export default inject("PageStore")(
  observer(({ PageStore, activeIndex }) => {
    return (
      <nav className="nav nav-pills nav-menu nav-justified">
        <a
          className={"nav-link" + (activeIndex === 0 ? " active" : "")}
          onClick={() => PageStore.push(`/main/story/diary`)}
        >
          조황일지
        </a>
        <a
          className={"nav-link" + (activeIndex === 1 ? " active" : "")}
          onClick={() => PageStore.push(`/main/story/tv`)}
        >
          어복TV
        </a>
        <a
          className={"nav-link" + (activeIndex === 2 ? " active" : "")}
          onClick={() => PageStore.push(`/main/story/user`)}
        >
          유저조행기
        </a>
      </nav>
    );
  })
);
