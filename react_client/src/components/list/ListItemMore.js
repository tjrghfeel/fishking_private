/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ onClick }) => {
    return (
      <li className="item more">
        <a className="moreLink" onClick={() => (onClick ? onClick() : null)}>
          <div className="inner">
            <span>더보기</span>
          </div>
        </a>
      </li>
    );
  })
);
