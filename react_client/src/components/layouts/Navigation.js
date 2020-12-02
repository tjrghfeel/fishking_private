/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";

const Navigation = inject("RouteStore")(
  observer(({ type, hasSearch, hasConfig, title, RouteStore }) => {
    const navigateTo = (pathname) => {
      RouteStore.go(pathname);
    };
    return (
      <nav className="navbar fixed-top navbar-dark bg-primary">
        {type === "index" && (
          <>
            <a className="navbar-brand">
              <img src="/assets/img/svg/navbar-logo.svg" alt="어복황제" />
            </a>
            <form className="form-inline">
              <input
                className="form-control mr-sm-2"
                type="search"
                placeholder=""
                aria-label="Search"
                disabled
              />
              <a onClick={() => navigateTo("search.html")}>
                <img src="/assets/img/svg/navbar-search.svg" alt="Search" />
              </a>
            </form>
          </>
        )}
        {type !== "index" && (
          <>
            <span className="navbar-title">{title}</span>
            {hasConfig && (
              <a
                className="fixed-top-right-two"
                onClick={() => navigateTo("set.html")}
              >
                <img src="/assets/img/svg/navbar-set.svg" alt="Set" />
              </a>
            )}
            {hasSearch && (
              <a
                className="fixed-top-right"
                onClick={() => navigateTo("search.html")}
              >
                <img src="/assets/img/svg/navbar-search.svg" alt="Search" />
              </a>
            )}
          </>
        )}
      </nav>
    );
  })
);

export default Navigation;
