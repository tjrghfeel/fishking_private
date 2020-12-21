/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ data: { imgSrc, text, count } }) => {
    return (
      <a>
        <div className="imgWrap imgFish">
          <img src={imgSrc} alt="" />
        </div>
        <div className="InfoWrap">
          <h6>
            {text}{" "}
            <strong className="text-primary">
              {Intl.NumberFormat().format(count)}
            </strong>
          </h6>
        </div>
      </a>
    );
  })
);
