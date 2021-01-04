import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: {
        downloadThumbnailUrl,
        shipStartTime,
        name,
        fishSpicesInfo,
        placeName,
        totalAmount = 0,
      },
      data,
      onClick,
      onDelete,
    }) => {
      return (
        <>
          <a onClick={() => (onClick ? onClick(data) : null)}>
            <div className="card card-sm">
              <a
                className="float-top-right"
                onClick={() => (onDelete ? onDelete(data) : null)}
              >
                <img
                  src="/assets/img/svg/icon_close_grey.svg"
                  alt=""
                  className="vam"
                />
              </a>
              <div className="row no-gutters">
                <div className="cardimgWrap">
                  <img
                    src={downloadThumbnailUrl}
                    className="img-fluid"
                    alt=""
                  />
                  <span className="play">
                    <img src="/assets/img/svg/live-play.svg" alt="" />
                  </span>
                  <span className="play-time">
                    {shipStartTime.substr(0, 2)}:{shipStartTime.substr(2, 2)}
                  </span>
                </div>
                <div className="cardInfoWrap">
                  <div className="card-body">
                    <h6>{name}</h6>
                    <p>
                      <strong className="text-primary">{fishSpicesInfo}</strong>{" "}
                      <img
                        src="/assets/img/fish/fish_icon_02.svg"
                        alt=""
                        className="fish-cate"
                      />
                      13
                      <br />
                      <span className="grey">{placeName}</span>&nbsp;전남 진도군
                      27km
                      <br />
                    </p>
                    <div className="card-price">
                      <small className="orange">실시간예약</small>
                      <h5>
                        <strong>
                          {Intl.NumberFormat().format(totalAmount)}
                        </strong>
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
