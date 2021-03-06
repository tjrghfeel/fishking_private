import React from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router-dom";
import * as path from "path";

export default inject("PageStore")(
  observer(
    withRouter(
      ({
        location: { pathname },
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
                        ? "/assets/cust/img/svg/navbar-back-black.svg"
                        : "/assets/cust/img/svg/navbar-back.svg"
                    }
                    alt="뒤로가기"
                  />
                </a>
              )}
              <span className="navbar-title">{title && title}</span>
              {showSetIcon && (
                <a
                  onClick={() => PageStore.push(`/set/main`)}
                  className="fixed-top-right-two"
                >
                  <img src="/assets/cust/img/svg/navbar-set.svg" alt="Set" />
                </a>
              )}
              {showSearchIcon && (
                <a
                  onClick={() => PageStore.push(`/search/all`)}
                  className="fixed-top-right"
                >
                  <img
                    src="/assets/cust/img/svg/navbar-search.svg"
                    alt="Search"
                  />
                </a>
              )}
              {customButton && (
                <React.Fragment>{[customButton]}</React.Fragment>
              )}
            </nav>
          </React.Fragment>
        );
      }
    )
  )
);
