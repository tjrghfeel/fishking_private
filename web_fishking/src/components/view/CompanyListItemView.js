import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: {
        shipImageFileUrl,
        shipName,
        fishSpecies = [],
        fishSpeciesCount = 0,
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
            <div className="card card-sm">
              <div className="row no-gutters">
                <div className="cardimgWrap">
                  <img src={"https://www.fishkingapp.com"+shipImageFileUrl} className="img-fluid" alt="" />
                  {/*<span className="play">*/}
                  {/*  <img src="/assets/cust/img/svg/live-play.svg" alt="" />*/}
                  {/*</span>*/}
                  {/*<span className="play-time">20:17</span>*/}
                </div>
                <div className="cardInfoWrap">
                  <div className="card-body">
                    <h6>{shipName}</h6>
                    <p>
                      <strong className="text-primary">
                        {fishSpecies.slice(0, 2).map((data, index) => {
                          if (index > 0) {
                            return (
                              <React.Fragment key={index}>
                                {", " + data.codeName}
                              </React.Fragment>
                            );
                          } else {
                            return (
                              <React.Fragment key={index}>
                                {data.codeName}
                              </React.Fragment>
                            );
                          }
                        })}
                      </strong>{" "}
                      <img
                        src="/assets/cust/img/fish/fish_icon_18.svg"
                        alt=""
                        className="fish-cate"
                      />
                      {Intl.NumberFormat().format(fishSpeciesCount)}
                      <br />
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
            </div>
          </a>
          <hr />
        </React.Fragment>
      );
    }
  )
);
