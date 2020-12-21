/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";

// 메인 > 마이메뉴 > 혜택쿠폰 > 리스트 아이템

export default inject()(
  observer(
    ({
      data,
      data: {
        cls,
        couponType,
        couponName,
        discount = 0,
        couponDescription,
        disabled = false,
        onClick,
      },
    }) => {
      return (
        <div className={"row no-gutters" + (cls ? " ".concat(cls) : "")}>
          <div className="col-12">
            <a onClick={() => (onClick ? onClick(data) : null)}>
              <div
                className={
                  "card-rect" + (disabled ? " ".concat("disabled") : "")
                }
              >
                <div className="row no-gutters d-flex align-items-center">
                  <div className="coupon-img">
                    <img
                      src={couponType ? "/assets/img/svg/img-coupon1.svg" : ""}
                      alt=""
                      className="icon-md"
                    />
                  </div>
                  <div className="coupon-text">
                    <small>{couponName}</small>
                    <h5>
                      <span className="red">
                        {Intl.NumberFormat().format(discount)}원
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
