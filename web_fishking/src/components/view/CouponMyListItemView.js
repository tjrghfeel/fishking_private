import React, { useState } from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: { couponName, couponType, saleValues, useDate, couponDescription },
      data,
      onClick,
    }) => {
      const [layerDisplay, setLayerDisplay] = useState("none");
      return (
        <div className="container nopadding">
          <div className="card-rect-box-grey">
            <a
              onClick={() => setLayerDisplay("inline")}
              className="btn btn-circle btn-circle-sm pt-0 mt-2 mr-2 float-right"
            >
              <img
                src="/assets/img/svg/icon-question.svg"
                alt=""
                className="vam"
              />
            </a>
            <div className="layerwrap">
              <div
                id="LayerPopArea"
                className="layer-pop-area"
                style={{ display: layerDisplay }}
              >
                <a
                  onClick={() => setLayerDisplay("none")}
                  className="btn btn-circle btn-circle-sm pt-0 mt-2 mr-2 float-right"
                >
                  <img
                    src="/assets/img/svg/icon_close_white.svg"
                    alt=""
                    className="vam"
                  />
                </a>
                <h5>쿠폰 상세보기</h5>
                <ul className="list-white">
                  <li>본 쿠폰은 다른 쿠폰과의 중복 사용이 불가합니다.</li>
                  <li>
                    지급된 쿠폰은 마이메뉴 > 쿠폰함에서 확인 할 수 있습니다.
                  </li>
                  <li>
                    본 쿠폰 이벤트는 사전 공지 없이 당사의 사정에 따라 변경 또는
                    조기 종료될 수 있습니다.
                  </li>
                  <li>
                    쿠폰 금액 이하의 상품을 예약할 경우 차액은 소멸되며,
                    쿠폰금액 이상의 상품을 예약할 경우 차액을 결제해 주셔야
                    합니다.
                  </li>
                </ul>
              </div>
            </div>
            <div className="padding text-center pt-4">
              {couponName}
              <h2 className="red">
                {couponType === "정률" && (
                  <React.Fragment>
                    {Intl.NumberFormat().format(saleValues)} % 할인
                  </React.Fragment>
                )}
                {couponType === "정액" && (
                  <React.Fragment>
                    {Intl.NumberFormat().format(saleValues)} 원 할인
                  </React.Fragment>
                )}
              </h2>
              <small className="grey">
                {useDate && <React.Fragment>~ {useDate}</React.Fragment>}
                <br />
                {couponDescription}
              </small>
            </div>
            <a
              onClick={() => (onClick ? onClick(data) : null)}
              className="btn btn-primary btn-lg btn-block mt-3 btn-btm"
            >
              바로예약
            </a>
          </div>
        </div>
      );
    }
  )
);
