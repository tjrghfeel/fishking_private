/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: {
        imgSrc,
        isLive,
        playTime,
        fishList,
        title,
        location,
        distance = 0,
        isRealtime,
        price = 0,
        textNotice,
        textEvent,
      },
    }) => {
      return (
        <a>
          <div className="card card-lg">
            <div className="card-img">
              <img src={imgSrc} className="card-img-top img-fluid" alt="" />
              {(isLive || playTime) && (
                <span className="play">
                  <img src="./assets/img/svg/live-play-big.svg" alt="" />
                </span>
              )}
              {isLive && <span className="play-live">LIVE</span>}
              {playTime && <span className="play-time">{playTime}</span>}
            </div>
            <div className="card-body">
              <ul className="tag">
                {fishList &&
                  fishList.map((data, index) => <li key={index}>{data}</li>)}
              </ul>
              <div className="card-info">
                <h5 className="card-title">{title}</h5>
                <p>
                  {location} {Intl.NumberFormat().format(distance)}km
                </p>
                <div className="card-price">
                  {isRealtime && <small className="orange">실시간예약</small>}
                  <h5>
                    <strong>{Intl.NumberFormat().format(price)}</strong>
                    <small>원~</small>
                  </h5>
                </div>
              </div>
              {(textNotice || textEvent) && (
                <>
                  <hr />
                  <ul className="notice">
                    {textNotice && (
                      <li className="icon-notice">{textNotice}</li>
                    )}
                    {textEvent && <li className="icon-event">{textEvent}</li>}
                  </ul>
                </>
              )}
            </div>
          </div>
        </a>
      );
    }
  )
);
