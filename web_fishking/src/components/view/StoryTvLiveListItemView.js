import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ data: {thumbnailUrl, shipName}, data, onClick }) => {
    return (
      <li className="item">
        <a onClick={() => onClick? onClick(data) : null}>
          <div className="imgWrap">
            <img
              src={thumbnailUrl}
              className="img-fluid"
              alt=""
            />
            <span className="play">
              <img src="/assets/cust/img/svg/live-play-big.svg" alt="" />
            </span>
            <span className="play-live">LIVE</span>
          </div>
          <div className="InfoWrap">
            <h6>{shipName}</h6>
          </div>
        </a>
      </li>
    );
  })
);
