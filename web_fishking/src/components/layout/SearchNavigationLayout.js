import React, { useState, useEffect, useCallback } from "react";
import { inject, observer } from "mobx-react";

export default inject("PageStore")(
  observer(({ text, onSubmit, PageStore }) => {
    const pathname = PageStore.history.location.pathname;
    const [value, setValue] = useState("");
    useEffect(() => {
      setValue(text);
    }, [text, setValue]);
    const onChange = useCallback(
      (inputText) => {
        setValue(inputText);
      },
      [setValue]
    );
    return (
      <nav className="navbar fixed-top navbar-dark bg-primary">
        <a onClick={() => PageStore.goBack()} className="nav-left">
          <img src="/assets/cust/img/svg/navbar-back.svg" alt="뒤로가기" />
        </a>
        <form className="form-inline ml-5 float-right" style={{ width: "80%" }}>
          <input
            className="form-control mr-sm-2"
            type="text"
            placeholder=""
            aria-label="Search"
            value={value}
            onChange={(e) => onChange(e.target.value)}
            onKeyDown={(e) => {
              if (e.keyCode === 13 && onSubmit) {
                onSubmit(e.target.value);
                e.preventDefault();
              }
            }}
          />
          <a
            onClick={() => (onSubmit ? onSubmit(value) : null)}
            className="float-right"
          >
            <img src="/assets/cust/img/svg/navbar-search.svg" alt="Search" />
          </a>
        </form>
      </nav>
    );
  })
);
