/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router";

export default inject()(
  observer(
    withRouter(
      ({
        history,
        type,
        title,
        backgroundTheme = "colored" | "blank",
        showConfig,
        showSearch,
        showBack,
        customButton,
      }) => {
        return (
          <nav
            className={
              "navbar fixed-top navbar-dark" +
              (backgroundTheme !== "blank" ? " ".concat("bg-primary") : "")
            }
          >
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
                  <a onClick={() => history.push(`/search/all`)}>
                    <img src="/assets/img/svg/navbar-search.svg" alt="Search" />
                  </a>
                </form>
              </>
            )}
            {type !== "index" && (
              <>
                {showBack && (
                  <a onClick={() => history.goBack()} className="nav-left">
                    <img
                      src={
                        backgroundTheme !== "blank"
                          ? "/assets/img/svg/navbar-back.svg"
                          : "/assets/img/svg/navbar-back-black.svg"
                      }
                      alt="뒤로가기"
                    />
                  </a>
                )}
                {title && <span className="navbar-title">{title}</span>}
                {showConfig && (
                  <a
                    onClick={() => history.push(`/set/main`)}
                    className="fixed-top-right-two"
                  >
                    <img src="/assets/img/svg/navbar-set.svg" alt="Set" />
                  </a>
                )}
                {showSearch && (
                  <a
                    onClick={() => history.push(`/search/all`)}
                    className="fixed-top-right"
                  >
                    <img src="/assets/img/svg/navbar-search.svg" alt="Search" />
                  </a>
                )}
                {customButton && (
                  <React.Fragment>{[customButton]}</React.Fragment>
                )}
              </>
            )}
          </nav>
        );
      }
    )
  )
);
