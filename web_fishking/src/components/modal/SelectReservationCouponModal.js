import React, { useState } from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ id = "", list = [], onSelect }) => {
    const [selected, setSelected] = useState([]);
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
                <small className="grey">(적용 가능 쿠폰 6개)</small>
              </h6>

              <label className="control radio mt-2">
                <input
                  type="radio"
                  className="add-contrast"
                  data-role="collar"
                />
                <span className="control-indicator"></span>
                <span className="control-text">선택없음</span>
              </label>
              <br />
              <label className="control radio">
                <input
                  type="radio"
                  className="add-contrast"
                  data-role="collar"
                />
                <span className="control-indicator"></span>
                <span className="control-text">
                  <strong className="red">3,000원</strong> 시즌 얼리버드 쿠폰{" "}
                  <small className="grey">(2020.09.30 까지)</small>
                </span>
              </label>
              <br />
              <label className="control radio">
                <input
                  type="radio"
                  className="add-contrast"
                  data-role="collar"
                />
                <span className="control-indicator"></span>
                <span className="control-text">
                  <strong className="red">1,500원</strong> Welcome 바다낚시 쿠폰{" "}
                  <small className="grey">(2020.09.30 까지)</small>
                </span>
              </label>
              <br />
              <label className="control radio">
                <input
                  type="radio"
                  className="add-contrast"
                  data-role="collar"
                />
                <span className="control-indicator"></span>
                <span className="control-text">
                  <strong className="red">1,000원</strong> 신규 고객 감사쿠폰{" "}
                  <small className="grey">(2020.09.30 까지)</small>
                </span>
              </label>
              <hr className="full mt-2 mb-3" />

              <h6>
                장바구니 쿠폰{" "}
                <small className="grey">(적용 가능 쿠폰 6개)</small>
              </h6>
              <label className="control radio mt-2">
                <input
                  type="radio"
                  className="add-contrast"
                  data-role="collar"
                />
                <span className="control-indicator"></span>
                <span className="control-text">선택없음</span>
              </label>
              <hr className="full mt-1 mb-3" />
              <ul className="list">
                <li>
                  <small className="grey">
                    상품쿠폰과 장바구니 쿠폰은 할인금액에 따라 중복 사용이
                    제한될 수 있습니다.
                  </small>
                </li>
                <li>
                  <small className="grey">
                    쿠폰 금액 이하의 상품을 예약할 경우 차액은 소멸되며, 쿠폰
                    금액 이상의 상품을 예약할 경우 차액을 결제해 주셔야 합니다.
                  </small>
                </li>
              </ul>
              <hr className="full mt-2 mb-2" />
              <div className="row no-gutters d-flex align-items-center">
                <div className="col-6">
                  <h6>총 할인 금액</h6>
                </div>
                <div className="col-6">
                  <h5 className="text-right red">- 5,000원</h5>
                </div>
              </div>
            </div>
            <a
              onClick={() => (onSelect ? onSelect(selected) : null)}
              className="btn btn-primary btn-lg btn-block btn-btm"
              data-dismiss="modal"
            >
              쿠폰적용
            </a>
          </div>
        </div>
      </div>
    );
  })
);
