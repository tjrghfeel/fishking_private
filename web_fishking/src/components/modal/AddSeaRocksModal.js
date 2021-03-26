import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ id = "" }) => {
    return (
      <div
        className="modal fade modal-full"
        id={id}
        tabIndex="-1"
        aria-labelledby={id.concat("Label")}
        aria-hidden="true"
      >
        <div className="modal-dialog">
          <div className="modal-content">
            <div className="modal-header bg-primary d-flex justify-content-center">
              <a data-dismiss="modal" className="nav-left">
                <img
                  src="/assets/smartfishing/img/svg/navbar-back.svg"
                  alt="뒤로가기"
                />
              </a>
              <h5 className="modal-title" id={id.concat("Label")}>
                신규 갯바위 추가
              </h5>
              <a className="nav-right">
                <img
                  src="/assets/smartfishing/img/svg/navbar-refresh.svg"
                  alt="Refresh"
                />
                <span>초기화</span>
              </a>
            </div>
            <div className="modal-body">
              <div className="padding">
                <div className="form-group">
                  <label className="d-block">
                    지역을 선택하신 후 원하시는 갯바위를 체크하세요.
                  </label>
                  <div className="input-group mb-3">
                    <select className="form-control" id="">
                      <option>시/도</option>
                    </select>
                    <select className="form-control" id="">
                      <option>시/군/구</option>
                    </select>
                    <select className="form-control" id="">
                      <option>읍/면/동</option>
                    </select>
                  </div>
                </div>

                <div className="mapwrap"></div>

                <div className="form-group text-right mt-3">
                  <a className="btn btn-third btn-sm">↑ 포인트 추가</a>
                </div>

                <div className="form-group">
                  <label htmlFor="">
                    갯바위 명칭<strong className="required"></strong>
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    id=""
                    placeholder="갯바위 명칭을 입력하세요"
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="">
                    평균수심(m)<strong className="required"></strong>
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    id=""
                    placeholder="평균수심(m)를 입력하세요"
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="">
                    저질<strong className="required"></strong>
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    id=""
                    placeholder="저질을 입력하세요"
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="">
                    적정물 때<strong className="required"></strong>
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    id=""
                    placeholder="적정물 때를 입력하세요"
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="">
                    소개 (수온, 주요대상어, 특징 등){" "}
                    <strong className="required"></strong>
                  </label>
                  <textarea
                    className="form-control"
                    placeholder=""
                    rows="7"
                  ></textarea>
                </div>
              </div>
            </div>
            <a
              className="btn btn-primary btn-lg btn-block btn-btm"
              data-toggle="modal"
              data-target="#selRocksModal"
            >
              신규 갯바위 등록
            </a>
          </div>
        </div>
      </div>
    );
  })
);
