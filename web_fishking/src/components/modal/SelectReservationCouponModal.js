import React, { useState, useCallback } from "react";
import { inject, observer } from "mobx-react";
import ReservationCouponListItemView from "../view/ReservationCouponListItemView";

export default inject()(
  observer(
    ({ id = "", price = 0, data = { size: 0, coupons: [] }, onSelect }) => {
      const [selected, setSelected] = useState(null);
      const [discount, setDiscount] = useState(0);
      const onClick = useCallback(
        (item) => {
          if (item === null) {
            setDiscount(0);
          } else if (item.couponType === "정액") {
            setDiscount(item.saleValues || 0);
          } else {
            setDiscount(Math.round(price * (item.saleValues / 100) || 0));
          }
          setSelected(item);
        },
        [setDiscount, setSelected, price]
      );
      return (
        <div
          className="modal show modal-full-btm-half"
          id={id}
          tabIndex="-1"
          aria-labelledby={id.concat("Label")}
          aria-hidden="true"
        >
          <div className="modal-dialog">
            <div className="modal-content">
              <a href="#none" data-dismiss="modal" className="float-top-right">
                <img src="/assets/cust/img/svg/icon_close_grey.svg" alt="" />
              </a>
              <h5 className="modal-title" id={id.concat("Label")}>
                쿠폰 선택
              </h5>
              <div className="modal-body">
                <h6>
                  상품 전용 쿠폰{" "}
                  <small className="grey">
                    (적용 가능 쿠폰 {Intl.NumberFormat().format(data.size)}개)
                  </small>
                </h6>

                <label className="control radio mt-2">
                  <input
                    type="radio"
                    name="reservation-coupon-listitem"
                    className="add-contrast"
                    data-role="collar"
                    onChange={(e) => onClick(null)}
                  />
                  <span className="control-indicator"></span>
                  <span className="control-text">선택없음</span>
                </label>
                <br />
                {data.coupons?.map((data, index) => (
                  <ReservationCouponListItemView
                    data={data}
                    onClick={onClick}
                  />
                ))}

                <hr className="full mt-2 mb-2" />
                <div className="row no-gutters d-flex align-items-center">
                  <div className="col-6">
                    <h6>총 할인 금액</h6>
                  </div>
                  <div className="col-6">
                    <h5 className="text-right red">
                      - {Intl.NumberFormat().format(discount)}원
                    </h5>
                  </div>
                </div>
              </div>
              <a
                onClick={() => (onSelect ? onSelect(selected, discount) : null)}
                className="btn btn-primary btn-lg btn-block btn-btm"
                data-dismiss="modal"
              >
                쿠폰적용
              </a>
            </div>
          </div>
        </div>
      );
    }
  )
);
