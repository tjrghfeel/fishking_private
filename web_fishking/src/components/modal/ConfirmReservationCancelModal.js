import React, { useState } from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ id = "", onClick }) => {
    const [checked, setChecked] = useState(false);
    return (
      <div
        className="modal fade"
        id={id}
        tabIndex="-1"
        aria-labelledby={id.concat("Label")}
        aria-hidden="true"
      >
        <div className="modal-dialog modal-sm modal-dialog-centered">
          <div className="modal-content">
            <div className="modal-header">
              <h5 className="modal-title text-center" id={id.concat("Label")}>
                결제 취소
              </h5>
            </div>
            <div className="modal-body">
              <h6 className="text-center red">
                <img
                  src="/assets/cust/img/svg/icon-alert.svg"
                  alt=""
                  className="vam"
                />
                &nbsp;결제한 모든 상품이 취소처리 됩니다.
              </h6>
              <hr />
              <ul className="list">
                <li>
                  결제 시 사용하신 쿠폰은 취소 확정 시 취소 및 환불 규정에 따라
                  소멸 처리되며 재발급이 불가합니다.
                </li>
                <li className="red">
                  해당 상품은 출항 12시간 전까지만 취소 가능합니다.
                </li>
              </ul>
              <hr />
              <label className="control radio">
                <input
                  type="checkbox"
                  className="add-contrast"
                  data-role="collar"
                  onChange={(e) => setChecked(e.target.checked)}
                />
                <span className="control-indicator"></span>
                <span className="control-text">
                  모두 확인하였으며 이에 동의합니다
                </span>
              </label>
            </div>
            <div className="modal-footer-btm">
              <div className="row no-gutters">
                <div className="col-6">
                  <a
                    onClick={() => {
                      if (checked && onClick) onClick();
                    }}
                    className="btn btn-primary btn-lg btn-block"
                  >
                    다음
                  </a>
                </div>
                <div className="col-6">
                  <a
                    className="btn btn-third btn-lg btn-block"
                    data-dismiss="modal"
                  >
                    닫기
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  })
);
