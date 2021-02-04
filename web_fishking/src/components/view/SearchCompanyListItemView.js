import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: {
        thumbnailUrl,
        isVideo = false,
        name,
        fishingType,
        address,
        distance,
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
                  <img src={thumbnailUrl} className="img-fluid" alt="" />
                  {isVideo && (
                    <span className="play">
                      <img src="/cust/assets/img/svg/live-play.svg" alt="" />
                    </span>
                  )}
                </div>
                <div className="cardInfoWrap">
                  <div className="card-body">
                    <h6>{name}</h6>
                    <p>
                      <span className="grey">{fishingType}&nbsp;|</span>
                      &nbsp;{address}{" "}
                      {Intl.NumberFormat().format(new Number(distance || 0))}km
                      <br />
                    </p>
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
