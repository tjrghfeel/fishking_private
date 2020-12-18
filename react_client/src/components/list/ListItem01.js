/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: {
        dataKey,
        imgSrc,
        text,
        textPrimary,
        location,
        price,
        isLive,
        playTime,
      },
    }) => {
      return (
        <a>
          <div className="imgWrap">
            <img src={imgSrc} className="img-fluid" alt="" />
            {(isLive || playTime) && (
              <>
                <span className="play">
                  <img src="/assets/img/svg/live-play.svg" alt="" />
                </span>
                {isLive && <span className="play-live">LIVE</span>}
                {playTime && <span className="play-time">{playTime}</span>}
              </>
            )}
          </div>
          <div className="InfoWrap">
            <h6>{text}</h6>
            <p>
              <strong className="text-primary">{textPrimary}</strong>
              {location}
            </p>
            {price && (
              <h6>
                {Intl.NumberFormat().format(price)}
                <small>원</small>
              </h6>
            )}
          </div>
        </a>
      );
    }
  )
);
