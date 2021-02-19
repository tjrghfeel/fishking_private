import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      id = "",
      data: { fishSpecies = [], minPersonnel = 0, maxPersonnel = 0 },
    }) => {
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
                    src="/assets/cust/img/svg/navbar-back.svg"
                    alt="뒤로가기"
                  />
                </a>
                <h5 className="modal-title" id={id.concat("Label")}>
                  상품상세
                </h5>
              </div>
              <div className="modal-body">
                <div className="padding">
                  <h5 className="text-center">
                    {fishSpecies.length > 0 && fishSpecies[0]}
                  </h5>

                  <div className="card card-box">
                    <h6 className="card-header">기본정보</h6>
                    <div className="card-body">
                      <dl className="dl-horizontal">
                        <dt>공지사항</dt>
                        <dd>붕돌 무료제공</dd>
                        <dt>이용자</dt>
                        <dd>초보자</dd>
                        <dt>탑승인원</dt>
                        <dd>
                          최소 {Intl.NumberFormat().format(minPersonnel)}명,
                          최대 {Intl.NumberFormat().format(maxPersonnel)}명
                        </dd>
                        <dt>낚시시간</dt>
                        <dd>오전 7시 30분 ~ 오후 12시 (4시간 30분)</dd>
                        {fishSpecies.length > 0 && (
                          <React.Fragment>
                            <dt>대상어종</dt>
                            <dd>
                              {fishSpecies.map((data, index) => {
                                if (index === 0) {
                                  return (
                                    <React.Fragment key={index}>
                                      {data}
                                    </React.Fragment>
                                  );
                                } else {
                                  return (
                                    <React.Fragment key={index}>
                                      {", ".concat(data)}
                                    </React.Fragment>
                                  );
                                }
                              })}
                            </dd>
                          </React.Fragment>
                        )}
                        <dt>미끼정보</dt>
                        <dd>갯지렁이류, 생미끼, 오징어류</dd>
                      </dl>
                    </div>
                  </div>

                  <div className="card card-box">
                    <h6 className="card-header">서비스 제공</h6>
                    <div className="card-body">
                      <dl className="dl-horizontal">
                        <dt>무료서비스</dt>
                        <dd>
                          회떠드림
                          <br />
                          커피, 음료제공
                          <br />
                          낚시 강습
                        </dd>
                        <dt>유료서비스</dt>
                        <dd>라면제공 : 3,000원</dd>
                      </dl>
                    </div>
                  </div>

                  <div className="card card-box">
                    <h6 className="card-header">준비물</h6>
                    <div className="card-body">
                      <dl className="dl-horizontal">
                        <dt>현장구매</dt>
                        <dd>미끼</dd>
                        <dt>유료서비스</dt>
                        <dd>라면제공 : 3,000원</dd>
                      </dl>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      );
    }
  )
);
