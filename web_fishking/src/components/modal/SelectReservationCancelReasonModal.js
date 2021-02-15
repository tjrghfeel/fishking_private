import React, { useState } from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ id = "", onClick }) => {
    const [selected, setSelected] = useState("");
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
                결제 취소 사유 선택
              </h5>
            </div>
            <div className="modal-body">
              <label className="control radio">
                <input
                  type="radio"
                  className="add-contrast"
                  data-role="collar"
                />
                <span className="control-indicator"></span>
                <span className="control-text">다른 업체 이용 예정</span>
              </label>
              <br />
              <label className="control radio">
                <input
                  type="radio"
                  className="add-contrast"
                  data-role="collar"
                />
                <span className="control-indicator"></span>
                <span className="control-text">비싼가격</span>
              </label>
              <br />
              <label className="control radio">
                <input
                  type="radio"
                  className="add-contrast"
                  data-role="collar"
                />
                <span className="control-indicator"></span>
                <span className="control-text">단순변심</span>
              </label>
              <br />
              <label className="control radio">
                <input
                  type="radio"
                  className="add-contrast"
                  data-role="collar"
                />
                <span className="control-indicator"></span>
                <span className="control-text">서비스 불만족</span>
              </label>
              <br />
              <label className="control radio">
                <input
                  type="radio"
                  className="add-contrast"
                  data-role="collar"
                />
                <span className="control-indicator"></span>
                <span className="control-text">날짜 잘못 선택</span>
              </label>
              <br />
              <label className="control radio">
                <input
                  type="radio"
                  className="add-contrast"
                  data-role="collar"
                />
                <span className="control-indicator"></span>
                <span className="control-text">현장에서 결제 예정</span>
              </label>
              <br />
            </div>
            <div className="modal-footer-btm">
              <div className="row no-gutters">
                <div className="col-6">
                  <a
                    onClick={() => {
                      if (selected !== null && onClick) onClick(selected);
                    }}
                    className="btn btn-primary btn-lg btn-block"
                  >
                    취소하기
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
