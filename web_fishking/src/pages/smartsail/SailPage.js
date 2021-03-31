import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, SmartSailMainTab },
} = Components;

export default inject("PageStore")(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout
              title={"승선관리"}
              customButton={
                <a className="fixed-top-right new">
                  <strong>N</strong>
                  <img
                    src="/assets/smartsail/img/svg/icon-alarm.svg"
                    alt="알림내역"
                  />
                  <span className="sr-only">알림내역</span>
                </a>
              }
              showBackIcon={true}
            />
            <SmartSailMainTab activeIndex={1} />

            <div className="filterlinewrap container nopadding">
              <ul className="nav nav-tabs nav-filter">
                <li>
                  <div className="input-group">
                    <label htmlFor="StartDate" className="sr-only">
                      예약일자
                    </label>
                    <input
                      type="text"
                      className="form-control"
                      id="StartDate"
                      placeholder=""
                      value="2020-12-01"
                    />
                    <span className="input-group-btn">
                      <a
                        className="btn btn-default"
                        data-toggle="modal"
                        data-target="#calModal"
                      >
                        <img
                          src="/assets/smartsail/img/svg/input_cal.svg"
                          alt=""
                        />
                      </a>
                    </span>
                    <span className="input-group-addon">~</span>
                    <label htmlFor="EndDate" className="sr-only">
                      예약일자
                    </label>
                    <input
                      type="text"
                      className="form-control"
                      id="EndDate"
                      placeholder=""
                      value="2020-12-31"
                    />
                    <span className="input-group-btn">
                      <a
                        className="btn btn-default"
                        data-toggle="modal"
                        data-target="#calModal"
                      >
                        <img
                          src="/assets/smartsail/img/svg/input_cal.svg"
                          alt=""
                        />
                      </a>
                    </span>
                  </div>
                </li>
                <li>
                  <label htmlFor="selPayStatus" className="sr-only">
                    결제상태
                  </label>
                  <select className="form-control" id="selPayStatus">
                    <option>상태전체</option>
                    <option>이용예정</option>
                    <option>이용완료</option>
                    <option>취소완료</option>
                  </select>
                </li>
                <li className="full-line">
                  <div className="input-group keyword">
                    <select className="custom-select">
                      <option>예약자명</option>
                      <option>승선자명</option>
                    </select>
                    <input
                      type="text"
                      className="form-control"
                      placeholder="검색어 입력"
                      value=""
                    />
                  </div>
                </li>
                <li className="full">
                  <p>
                    <a className="btn btn-primary btn-sm">검색</a>
                    <a className="btn btn-grey btn-sm">초기화</a>
                  </p>
                </li>
              </ul>
            </div>
            <p className="clearfix"></p>

            <div className="container nopadding mt-2">
              <div className="card-round-grey">
                <span className="status status2">이용예정</span>
                <span className="dday">D-42</span>
                <a onClick={() => PageStore.push(`/sail/detail`)}>
                  <div className="card card-sm">
                    <div className="row no-gutters">
                      <div className="cardimgWrap">
                        <img
                          src="/assets/smartsail/img/sample/boat2.jpg"
                          className="img-fluid"
                          alt=""
                        />
                      </div>
                      <div className="cardInfoWrap">
                        <div className="card-body">
                          <h6>어복황제3호</h6>
                          <p>
                            <span className="grey">전남 진도군 27km</span>
                          </p>
                        </div>
                      </div>
                    </div>
                    <hr className="mt-1 mb-1" />
                    <div className="row no-gutters">
                      <div className="col-6 padding-sm">
                        <small className="grey">
                          이용일
                          <br />
                          예약번호
                        </small>
                      </div>
                      <div className="col-6 padding-sm">
                        <small>
                          2020년 08월 25일(화) 09:00 ~<br />
                          20082501
                        </small>
                      </div>
                    </div>
                  </div>
                </a>
                <div className="row no-gutters">
                  <div className="col-12 padding-sm">
                    <a className="btn btn-info btn-block btn-sm mt-1 mb-1">
                      승선자 추가 (현재 <strong>10명</strong>)
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
