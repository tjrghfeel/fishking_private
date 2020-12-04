/* eslint-disable jsx-a11y/anchor-is-valid */
import React, { useCallback, useState } from "react";
import { inject, observer } from "mobx-react";

const MainTabs = inject("RouteStore")(
  observer(({ RouteStore }) => {
    const [location, setLocation] = useState(
      RouteStore.history.location.pathname
    );
    // -- > 네비게이션
    const navigateTo = useCallback(
      (pathname) => {
        RouteStore.go(pathname);
        setLocation(pathname);
      },
      [RouteStore, setLocation]
    );
    // -- > 탭 메뉴 리스트
    const menus = [
      { pathname: `/main/index`, iconName: "tab_home", text: "홈" },
      { pathname: `/main/boat`, iconName: "tab_boat", text: "선상" },
      { pathname: `/main/rock`, iconName: "tab_rock", text: "갯바위" },
      { pathname: `/main/story`, iconName: "tab_story", text: "어복스토리" },
      { pathname: `/main/my`, iconName: "tab_my", text: "마이메뉴" },
    ];
    // --> 렌더링
    return (
      <div className="tab_barwrap fixed-bottom">
        <div className="container nopadding">
          <nav className="nav nav-pills nav-tab nav-justified">
            {menus.map((data, index) => (
              <a
                key={index}
                className={
                  "nav-link" +
                  (location.indexOf(data.pathname) !== -1 ? " active" : "")
                }
                onClick={() => navigateTo(data.pathname)}
              >
                <figure className={data.iconName}></figure>
                {data.text}
              </a>
            ))}
          </nav>
        </div>
      </div>
    );
  })
);

export default MainTabs;
