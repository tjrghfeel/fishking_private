/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";

import { fishIcon } from "../../commons/Constant";

export default inject()(
  observer(
    ({
      data,
      data: {
        imgSrc,
        isLive,
        playTime,
        title,
        fishList,
        fishType,
        fishCount = 0,
        fishingType,
        location,
        distance = 0,
        isRealtime,
        price = 0,
        onClose,
      },
    }) => {
      return (
        <>
          <a>
            <div className="card card-sm">
              {onClose && (
                <a
                  className="float-top-right"
                  onClick={() => (onClose ? onClose(data) : null)}
                >
                  <img
                    src="/assets/img/svg/icon_close_grey.svg"
                    alt=""
                    className="vam"
                  />
                </a>
              )}
              <div className="row no-gutters">
                <div className="cardimgWrap">
                  <img src={imgSrc} className="img-fluid" alt="" />
                  {(isLive || playTime) && (
                    <span className="play">
                      <img src="/assets/img/svg/live-play.svg" alt="" />
                    </span>
                  )}
                  {isLive && <span className="play-live">LIVE</span>}
                  {playTime && <span className="play-time">{playTime}</span>}
                </div>
                <div className="cardInfoWrap">
                  <div className="card-body">
                    <h6>{title}</h6>
                    <p>
                      {fishList && (
                        <>
                          <strong className="text-primary">
                            {fishList.map((data, index) => {
                              if (index === 0) {
                                return (
                                  <React.Fragment key={index}>
                                    {data}
                                  </React.Fragment>
                                );
                              } else {
                                return (
                                  <React.Fragment key={index}>
                                    {", ".concat(data)}
                                  </React.Fragment>
                                );
                              }
                            })}
                          </strong>{" "}
                        </>
                      )}
                      {fishType && (
                        <>
                          <img
                            src={fishIcon[fishType]}
                            alt=""
                            className="fish-cate"
                          />
                          {fishCount}
                        </>
                      )}
                      <br />
                      {fishingType && (
                        <React.Fragment>
                          <span className="grey">선상&nbsp;|</span>&nbsp;
                        </React.Fragment>
                      )}
                      {location} {Intl.NumberFormat().format(distance)}km
                    </p>
                    <div className="card-price">
                      {isRealtime && (
                        <small className="orange">실시간예약</small>
                      )}
                      <h5>
                        <strong>{Intl.NumberFormat().format(price)}</strong>
                        <small>원~</small>
                      </h5>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </a>
          <hr />
        </>
      );
    }
  )
);
