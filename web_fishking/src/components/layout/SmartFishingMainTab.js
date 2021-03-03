import React from "react";
import { inject, observer } from "mobx-react";

export default inject("PageStore")(
  observer(({ activeIndex = 0, PageStore }) => {
    return (
      <nav className="nav nav-pills nav-bar nav-justified">
        <a
          className={"nav-link" + (activeIndex === 0 ? " active" : "")}
          onClick={() => PageStore.push(`/dashboard`)}
        >
          <figure className="dashboard"></figure>
          대시보드
        </a>
        <a
          className={"nav-link" + (activeIndex === 1 ? " active" : "")}
          onClick={() => PageStore.push(`/reservation`)}
        >
          <figure className="reservation"></figure>
          예약
        </a>
        <a
          className={"nav-link" + (activeIndex === 2 ? " active" : "")}
          onClick={() => PageStore.push(`/fish`)}
        >
          <figure className="fish"></figure>
          조황
        </a>
        <a
          className={"nav-link" + (activeIndex === 3 ? " active" : "")}
          onClick={() => PageStore.push(`/goods`)}
        >
          <figure className="goods"></figure>
          상품
        </a>
        <a
          className={"nav-link" + (activeIndex === 4 ? " active" : "")}
          onClick={() => PageStore.push(`/boat`)}
        >
          <figure className="boat"></figure>
          선상
        </a>
        <a
          className={"nav-link" + (activeIndex === 5 ? " active" : "")}
          onClick={() => PageStore.push(`/paid`)}
        >
          <figure className="paid"></figure>
          정산
        </a>
        <a
          className={"nav-link" + (activeIndex === 6 ? " active" : "")}
          onClick={() => PageStore.push(`/cs`)}
        >
          <figure className="cs"></figure>
          고객센터
        </a>
      </nav>
    );
  })
);
