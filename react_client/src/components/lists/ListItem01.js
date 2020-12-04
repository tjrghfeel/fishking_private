/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";

const ListItem01 = inject("RouteStore")(
  observer(
    ({
      RouteStore,
      navigateTo,
      imgSrc,
      isLive,
      playTime,
      title,
      textPrimary,
      location,
      price,
    }) => {
      const go = (pathname) => {
        RouteStore.go(pathname);
      };
      return (
        <li className="item">
          <a onClick={() => go(navigateTo)}>
            <div className="imgWrap">
              <img src={imgSrc} className="img-fluid" alt="" />
              {(isLive || playTime) && (
                <span className="play">
                  <img src="/assets/img/svg/live-play.svg" alt="" />
                </span>
              )}
              {isLive && <span className="play-live">LIVE</span>}
              {playTime && <span className="play-time">20:17</span>}
            </div>
            <div className="InfoWrap">
              <h6>{title}</h6>
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
        </li>
      );
    }
  )
);

export default ListItem01;
