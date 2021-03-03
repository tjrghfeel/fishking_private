import React from "react";
import { inject, observer } from "mobx-react";

export default inject("PageStore")(
  observer(({ activeIndex = 0, PageStore }) => {
    return (
      <div className="tab_barwrap fixed-bottom">
        <div className="container nopadding">
          <nav className="nav nav-pills nav-tab nav-justified">
            <a
              className={"nav-link" + (activeIndex === 0 ? " active" : "")}
              onClick={() => PageStore.push(`/dashboard`)}
            >
              <figure className="tab_home"></figure>
              대시보드
            </a>
            <a
              className={"nav-link" + (activeIndex === 1 ? " active" : "")}
              onClick={() => PageStore.push(`/map`)}
            >
              <figure className="tab_map"></figure>
              출항정보
            </a>
            <a
              className={"nav-link" + (activeIndex === 2 ? " active" : "")}
              onClick={() => PageStore.push(`/boat`)}
            >
              <figure className="tab_boat"></figure>
              선상정보
            </a>
            <a
              className={"nav-link" + (activeIndex === 3 ? " active" : "")}
              onClick={() => PageStore.push(`/my`)}
            >
              <figure className="tab_my"></figure>
              더보기
            </a>
          </nav>
        </div>
      </div>
    );
  })
);
