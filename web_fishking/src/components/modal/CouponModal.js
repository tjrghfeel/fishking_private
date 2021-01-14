import React, { useState } from "react";
import { inject, observer } from "mobx-react";

export default inject("ModalStore")(
  observer(({ ModalStore: { onOk } }) => {
    const [text, setText] = useState("");
    return (
      <div
        className="modal fade"
        id="couponModal"
        tabIndex="-1"
        aria-labelledby="couponModalLabel"
        aria-hidden="true"
      >
        <div className="modal-dialog modal-sm modal-dialog-centered">
          <div className="modal-content">
            <div className="modal-header">
              <h5 className="modal-title text-center" id="couponModalLabel">
                쿠폰 등록
              </h5>
            </div>
            <div className="modal-body text-center">
              <form className="form-line mt-3">
                <div className="form-group">
                  <label htmlFor="inputNum" className="sr-only">
                    쿠폰 코드
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="쿠폰 코드를 입력하세요."
                    value={text}
                    onChange={(e) => setText(e.target.value)}
                  />
                </div>
              </form>
              <ul className="list text-left">
                <li>쿠폰기간, 혜택, 사용조건을 꼭 확인하세요.</li>
                <li>등록시 대/소문자는 구분하지 않습니다.</li>
              </ul>
            </div>
            <div className="modal-footer-btm">
              <div className="row no-gutters">
                <div className="col-6">
                  <a
                    onClick={() => {
                      onOk(text);
                      setText("");
                    }}
                    className="btn btn-primary btn-lg btn-block"
                    data-dismiss="modal"
                  >
                    확인
                  </a>
                </div>
                <div className="col-6">
                  <a
                    onClick={() => setText("")}
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
