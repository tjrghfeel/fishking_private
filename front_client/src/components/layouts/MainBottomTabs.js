import React, { useCallback, useEffect, useState } from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router";

export default inject()(
  observer(
    withRouter(({ history, activedIndex = -1 }) => {
      const [activeIndex, setActiveIndex] = useState(0);
      const parsePathname = useCallback(
        (pathname) => {
          if (activedIndex !== -1) {
            setActiveIndex(activedIndex);
          } else {
            if (pathname.indexOf(`/main/home`) !== -1) setActiveIndex(0);
            else if (pathname.indexOf(`/main/reserve/boat`) !== -1)
              setActiveIndex(1);
            else if (pathname.indexOf(`/main/reserve/rock`) !== -1)
              setActiveIndex(2);
            else if (pathname.indexOf(`/main/story`) !== -1) setActiveIndex(3);
            else if (pathname.indexOf(`/main/my`) !== -1) setActiveIndex(4);
          }
        },
        [activedIndex, setActiveIndex]
      );
      const navigateTo = useCallback(
        (pathname) => {
          parsePathname(pathname);
          history.push(pathname);
        },
        [history, parsePathname]
      );
      useEffect(() => {
        const pathname = history.location.pathname;
        parsePathname(pathname);
      }, [history, parsePathname]);
      return (
        <div className="tab_barwrap fixed-bottom">
          <div className="container nopadding">
            <nav className="nav nav-pills nav-tab nav-justified">
              <a
                className={"nav-link" + (activeIndex === 0 ? " active" : "")}
                onClick={() => navigateTo(`/main/home`)}
              >
                <figure className="tab_home"></figure>홈
              </a>
              <a
                className={"nav-link" + (activeIndex === 1 ? " active" : "")}
                onClick={() => navigateTo(`/main/reserve/boat`)}
              >
                <figure className="tab_boat"></figure>
                선상
              </a>
              <a
                className={"nav-link" + (activeIndex === 2 ? " active" : "")}
                onClick={() => navigateTo(`/main/reserve/rock`)}
              >
                <figure className="tab_rock"></figure>
                갯바위
              </a>
              <a
                className={"nav-link" + (activeIndex === 3 ? " active" : "")}
                onClick={() => navigateTo(`/main/story/main`)}
              >
                <figure className="tab_story"></figure>
                어복스토리
              </a>
              <a
                className={"nav-link" + (activeIndex === 4 ? " active" : "")}
                onClick={() => navigateTo(`/main/my`)}
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
