import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ data: {}, data, onClick }) => {
    return (
      <li className="item">
        <a href="story-tv-live.html">
          <div className="imgWrap">
            <img
              src="/assets/cust/img/sample/live4.jpg"
              className="img-fluid"
              alt=""
            />
            <span className="play">
              <img src="/assets/cust/img/svg/live-play-big.svg" alt="" />
            </span>
            <span className="play-live">LIVE</span>
          </div>
          <div className="InfoWrap">
            <h6>어복황제 1호</h6>
          </div>
        </a>
      </li>
    );
  })
);
