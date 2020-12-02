/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";

const ListItemMore = inject("RouteStore")(
  observer(({ RouteStore, navigateTo }) => {
    const go = (pathname) => {
      RouteStore.go(pathname);
    };
    return (
      <li className="item more">
        <a onClick={() => navigateTo(go)} className="moreLink">
          <div className="inner">
            <span>더보기</span>
          </div>
        </a>
      </li>
    );
  })
);

export default ListItemMore;
