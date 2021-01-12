import React from "react";
import { inject, observer } from "mobx-react";

export default inject("PageStore")(
  observer(({ activeIndex = 0, PageStore }) => {
    return (
      <React.Fragment>
        <div className="tab_barwrap fixed-bottom">
          <div className="container nopadding">
            <nav className="nav nav-pills nav-tab nav-justified">
              <a
                className={"nav-link" + (activeIndex === 0 ? " active" : "")}
                onClick={() => PageStore.push(`/fishking/main/home`)}
              >
                <figure className="tab_home"></figure>홈
              </a>
              <a
                className={"nav-link" + (activeIndex === 1 ? " active" : "")}
                onClick={() => PageStore.push(`/fishking/main/boat`)}
              >
                <figure className="tab_boat"></figure>
                선상
              </a>
              <a
                className={"nav-link" + (activeIndex === 2 ? " active" : "")}
                onClick={() => PageStore.push(`/fishking/main/rock`)}
              >
                <figure className="tab_rock"></figure>
                갯바위
              </a>
              <a
                className={"nav-link" + (activeIndex === 3 ? " active" : "")}
                onClick={() => PageStore.push(`/fishking/main/story`)}
              >
                <figure className="tab_story"></figure>
                어복스토리
              </a>
              <a
                className={"nav-link" + (activeIndex === 4 ? " active" : "")}
                onClick={() => PageStore.push(`/fishking/main/my`)}
              >
                <figure className="tab_my"></figure>
                마이메뉴
              </a>
            </nav>
          </div>
        </div>
      </React.Fragment>
    );
  })
);
