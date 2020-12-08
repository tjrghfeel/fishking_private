/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ type, title, visibleConfigIcon, visibleSearchIcon }) => {
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
              <a>
                <img src="/assets/img/svg/navbar-search.svg" alt="Search" />
              </a>
            </form>
          </>
        )}
        {type !== "index" && (
          <>
            <span className="navbar-title">{title}</span>
            {visibleConfigIcon && (
              <a className="fixed-top-right-two">
                <img src="/assets/img/svg/navbar-set.svg" alt="Set" />
              </a>
            )}
            {visibleSearchIcon && (
              <a className="fixed-top-right">
                <img src="/assets/img/svg/navbar-search.svg" alt="Search" />
              </a>
            )}
          </>
        )}
      </nav>
    );
  })
);
