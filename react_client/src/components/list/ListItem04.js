import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: { imgSrc, text, textPrimary, location, distance, price = 0 },
    }) => {
      return (
        <div className="card">
          <div className="row no-gutters">
            <div className="cardimgWrap">
              <img src={imgSrc} className="img-fluid" alt="" />
            </div>
            <div className="cardInfoWrap">
              <div className="card-body">
                <h6>{text}</h6>
                <p>
                  <strong className="text-primary">{textPrimary}</strong>
                  <br />
                  {location} {distance}km
                </p>
                <h6 className="btm-right">
                  {Intl.NumberFormat().format(price)}
                  <small>원</small>
                </h6>
              </div>
            </div>
          </div>
        </div>
      );
    }
  )
);
