import React, { useState, useEffect } from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: {
        name,
        id,
        address,
        distance = 0,
        fishSpicesImgUrl,
        fishSpicesCount,
        fishingType,
        downloadThumbnailUrl,
        price = 0,
        fishSpicesInfo,
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
                  src="/assets/cust/img/svg/icon_close_grey.svg"
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
                </div>
                <div className="cardInfoWrap">
                  <div className="card-body">
                    <h6>{name}</h6>
                    <p>
                      {fishSpicesCount > 0 && (
                        <React.Fragment>
                          <strong className="text-primary">
                            {fishSpicesInfo}
                          </strong>{" "}
                          <img
                            src={fishSpicesImgUrl}
                            alt=""
                            className="fish-cate"
                          />{" "}
                          {fishSpicesCount}
                          <br />
                        </React.Fragment>
                      )}
                      <span className="grey">{fishingType}</span>&nbsp;
                      {address} {Intl.NumberFormat().format(distance)}km
                      <br />
                    </p>
                    <div className="card-price">
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
