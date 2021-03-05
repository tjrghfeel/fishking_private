import React from "react";
import { inject, observer } from "mobx-react";

export default inject("PageStore")(
  observer(({ activeIndex = 0, PageStore }) => {
    return (
      <nav className="nav nav-pills nav-menu nav-justified">
        <a
          className={"nav-link" + (activeIndex === 0 ? " active" : "")}
          onClick={() => PageStore.push(`/tide/today`)}
        >
          오늘의 물때정보
        </a>
        <a
          className={"nav-link" + (activeIndex === 1 ? " active" : "")}
          onClick={() => PageStore.push(`/tide/daily`)}
        >
          날짜별 물때정보
        </a>
      </nav>
    );
  })
);
