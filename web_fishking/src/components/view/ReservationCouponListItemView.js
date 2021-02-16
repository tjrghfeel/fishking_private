import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: { couponType, saleValues = 0, couponName, useDate },
      data,
      onClick,
    }) => {
      return (
        <React.Fragment>
          <label className="control radio">
            <input
              name="reservation-coupon-listitem"
              type="radio"
              className="add-contrast"
              data-role="collar"
              onChange={(e) => {
                if (e.target.checked && onClick) onClick(data);
              }}
            />
            <span className="control-indicator"></span>
            <span className="control-text">
              <strong className="red">
                {Intl.NumberFormat()
                  .format(saleValues)
                  .concat(couponType === "정액" ? "원" : "%")}
              </strong>{" "}
              {couponName} <small className="grey">({useDate} 까지)</small>
            </span>
          </label>
          <br />
        </React.Fragment>
      );
    }
  )
);
