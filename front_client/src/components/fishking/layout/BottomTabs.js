import React from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router";

export default inject("ViewStore")(
  observer(
    withRouter(({ ViewStore: { push }, activedIndex = -1 }) => {
      return (
        <div className="tab_barwrap fixed-bottom">
          <div className="container nopadding">
            <nav className="nav nav-pills nav-tab nav-justified">
              <a
                className={"nav-link" + (activedIndex === 0 ? " active" : "")}
                onClick={() => push(`/fishking/main/home`)}
              >
                <figure className="tab_home"></figure>홈
              </a>
              <a
                className={"nav-link" + (activedIndex === 1 ? " active" : "")}
                onClick={() => push(`/fishking/main/boat`)}
              >
                <figure className="tab_boat"></figure>
                선상
              </a>
              <a
                className={"nav-link" + (activedIndex === 2 ? " active" : "")}
                onClick={() => push(`/fishking/main/rock`)}
              >
                <figure className="tab_rock"></figure>
                갯바위
              </a>
              <a
                className={"nav-link" + (activedIndex === 3 ? " active" : "")}
                onClick={() => push(`/fishking/main/story`)}
              >
                <figure className="tab_story"></figure>
                어복스토리
              </a>
              <a
                className={"nav-link" + (activedIndex === 4 ? " active" : "")}
                onClick={() => push(`/fishking/main/my`)}
              >
                <figure className="tab_my"></figure>
                마이메뉴
              </a>
            </nav>
          </div>
        </div>
      );
    })
  )
);
