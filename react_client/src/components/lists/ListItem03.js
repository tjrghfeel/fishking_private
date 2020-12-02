/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";

const ListItem03 = inject("RouteStore")(
  observer(({ RouteStore, navigateTo, imgSrc, title, count }) => {
    const go = (pathname) => {
      RouteStore.go(pathname);
    };
    return (
      <li className="item">
        <a onClick={() => go(navigateTo)}>
          <div className="imgWrap imgFish">
            <img src={imgSrc} alt="" />
          </div>
          <div className="InfoWrap">
            <h6>
              {title}{" "}
              <strong className="text-primary">
                {Intl.NumberFormat().format(count)}
              </strong>
            </h6>
          </div>
        </a>
      </li>
    );
  })
);

export default ListItem03;
