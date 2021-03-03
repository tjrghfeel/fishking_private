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
          onClick={() => PageStore.push(`/sail`)}
        >
          <figure className="sail"></figure>
          승선관리
        </a>
        <a
          className={"nav-link" + (activeIndex === 2 ? " active" : "")}
          onClick={() => PageStore.push(`/camera`)}
        >
          <figure className="camera"></figure>
          카메라관리
        </a>
        <a
          className={"nav-link" + (activeIndex === 3 ? " active" : "")}
          onClick={() => PageStore.push(`/cs`)}
        >
          <figure className="cs"></figure>
          고객센터
        </a>
      </nav>
    );
  })
);
