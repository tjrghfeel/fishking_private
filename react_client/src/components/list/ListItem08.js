import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ imgSrc, isLive, playtime, title }) => {
    return (
      <a>
        <div className="imgWrap">
          <img src={imgSrc} className="img-fluid" alt="" />
          {(isLive || playtime) && (
            <span className="play">
              <img src="/assets/img/svg/live-play-big.svg" alt="" />
            </span>
          )}
          {isLive && <span className="play-live">LIVE</span>}
          {playtime && <span className="play-time">{playtime}</span>}
        </div>
        <div className="InfoWrap">
          <h6>{title}</h6>
        </div>
      </a>
    );
  })
);
