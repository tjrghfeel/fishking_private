import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: {
        couponType = "amount",
        couponImage,
        couponName,
        saleValues = 0,
        couponDescription,
        isUsable = true,
      },
      index,
      data,
      onClick,
    }) => {
      return (
        <div
          className={"row no-gutters" + (index === 0 ? " ".concat("mt-3") : "")}
        >
          <div className="col-12">
            <a onClick={() => (onClick ? onClick(data) : null)}>
              <div
                className={
                  "card-rect" + (!isUsable ? " ".concat("disabled") : "")
                }
              >
                <div className="row no-gutters d-flex align-items-center">
                  <div className="coupon-img">
                    <img src={couponImage} alt="" className="icon-md" />
                  </div>
                  <div className="coupon-text">
                    <small>{couponName}</small>
                    <h5>
                      <span className="red">
                        {couponType === "정액" && (
                          <React.Fragment>
                            {Intl.NumberFormat().format(saleValues)} 원
                          </React.Fragment>
                        )}
                        {couponType !== "정액" && (
                          <React.Fragment>
                            {Intl.NumberFormat().format(saleValues)} %
                          </React.Fragment>
                        )}
                      </span>{" "}
                      할인
                    </h5>
                    <small className="grey">{couponDescription}</small>
                  </div>
                </div>
                <hr className="mt-2 mb-2" />
                <small>
                  쿠폰 다운받기&nbsp;
                  <img
                    src="/assets/img/svg/icon-down.svg"
                    alt=""
                    className="vam"
                  />
                </small>
              </div>
            </a>
          </div>
        </div>
      );
    }
  )
);
