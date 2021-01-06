import React, { useState, useEffect } from "react";
import { inject, observer } from "mobx-react";

export default inject("DataStore")(
  observer(
    ({
      data: {
        name,
        id,
        address,
        distance = 0,
        fishSpicesCount,
        fishingType,
        downloadThumbnailUrl,
        price = 0,
        fishSpicesInfo,
      },
      data,
      onClick,
      onDelete,
      DataStore: { getEnumValueByIndex },
    }) => {
      const [fishTypeName, setFishTypeName] = useState("");
      useEffect(() => {
        (async () => {
          const fishEnum = await getEnumValueByIndex(
            "fishingType",
            fishingType
          );
          if (fishEnum !== null) {
            setFishTypeName(fishEnum.value);
          }
        })();
      }, [getEnumValueByIndex, setFishTypeName]);

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
                  <span className="play-time">{/* play-time */}</span>
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
                      />{" "}
                      {fishSpicesCount}
                      <br />
                      <span className="grey">{fishTypeName}</span>&nbsp;
                      {address} {Intl.NumberFormat().format(distance)}km
                      <br />
                    </p>
                    <div className="card-price">
                      <small className="orange">실시간예약</small>
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
