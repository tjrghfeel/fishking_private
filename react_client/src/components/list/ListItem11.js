import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ imgSrc, isGood, title, textPrimary, location, price }) => {
    return (
      <a href="boat-detail.html">
        <div className="imgWrap">
          <img src={imgSrc} className="img-fluid" alt="" />
          <span
            className={"icon-goods" + (isGood ? " ".concat("active") : "")}
          ></span>
        </div>
        <div className="InfoWrap">
          <h6>{title}</h6>
          <p>
            <strong className="text-primary">{textPrimary}</strong>
            {location}
          </p>
          <h6>
            {Intl.NumberFormat().format(price)}
            <small>원</small>
          </h6>
        </div>
      </a>
    );
  })
);
