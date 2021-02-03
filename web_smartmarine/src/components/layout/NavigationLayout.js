import React from "react";
import { inject, observer } from "mobx-react";

export default inject("PageStore")(
  observer(
    ({
      PageStore,
      title,
      showBackIcon,
      showSetIcon,
      showSearchIcon,
      transparent = false,
      customButton,
      backPathname,
    }) => {
      return (
        <React.Fragment>
          <nav
            className={
              "navbar fixed-top navbar-dark " +
              (transparent ? "" : "bg-primary")
            }
          >
            {showBackIcon && (
              <a
                onClick={() => {
                  if (backPathname) PageStore.push(backPathname);
                  else PageStore.goBack();
                }}
                className="nav-left"
              >
                <img
                  src={
                    transparent
                      ? "/assets/img/svg/navbar-back-black.svg"
                      : "/assets/img/svg/navbar-back.svg"
                  }
                  alt="뒤로가기"
                />
              </a>
            )}
            <span className="navbar-title">{title && title}</span>
            {showSetIcon && (
              <a
                onClick={() => PageStore.push(`/police/set/main`)}
                className="fixed-top-right-two"
              >
                <img src="/assets/img/svg/navbar-set.svg" alt="Set" />
              </a>
            )}
            {showSearchIcon && (
              <a
                onClick={() => PageStore.push(`/police/search/all`)}
                className="fixed-top-right"
              >
                <img src="/assets/img/svg/navbar-search.svg" alt="Search" />
              </a>
            )}
            {customButton && <React.Fragment>{[customButton]}</React.Fragment>}
          </nav>
        </React.Fragment>
      );
    }
  )
);
