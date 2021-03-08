import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: {
        shipImageFileUrl,
        fishSpecies = [],
        shipName,
        sido = "",
        sigungu = "",
        distance = 0,
        lowPrice = 0,
      },
      data,
      onClick,
    }) => {
      return (
        <React.Fragment>
          <a onClick={() => (onClick ? onClick(data) : null)}>
            <div className="card card-lg">
              <div className="card-img">
                <img
                  src={shipImageFileUrl}
                  className="card-img-top img-fluid"
                  alt=""
                />
                {/*<span className="play">*/}
                {/*  <img src="/assets/cust/img/svg/live-play-big.svg" alt="" />*/}
                {/*</span>*/}
                {/*<span className="play-live">LIVE</span>*/}
              </div>
              <div className="card-body">
                <ul className="tag">
                  {fishSpecies.slice(0, 2).map((data, index) => (
                    <li>{data["codeName"]}</li>
                  ))}
                </ul>
                <div className="card-info">
                  <h5 className="card-title">{shipName}</h5>
                  <p>
                    {sido.concat(sigungu)}{" "}
                    {Intl.NumberFormat().format(distance)}km
                  </p>
                  <div className="card-price">
                    {/*<small className="orange">실시간예약</small>*/}
                    <h5>
                      <strong>{Intl.NumberFormat().format(lowPrice)}</strong>
                      <small>원~</small>
                    </h5>
                  </div>
                </div>
              </div>
            </div>
          </a>
        </React.Fragment>
      );
    }
  )
);
