import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, SmartFishingMainTab },
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
        return (
          <React.Fragment>
            <NavigationLayout title={"예약관리"} showBackIcon={true} />
            <SmartFishingMainTab activeIndex={1} />

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
                      value="2020-01-01"
                    />
                    <span className="input-group-btn">
                      <a
                        className="btn btn-default"
                        data-toggle="modal"
                        data-target="#calModal"
                      >
                        <img
                          src="/assets/smartfishing/img/svg/input_cal.svg"
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
                      value="2020-02-01"
                    />
                    <span className="input-group-btn">
                      <a
                        className="btn btn-default"
                        data-toggle="modal"
                        data-target="#calModal"
                      >
                        <img
                          src="/assets/smartfishing/img/svg/input_cal.svg"
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
                    <option>결제상태 전체</option>
                    <option>입금대기</option>
                    <option>결제완료</option>
                    <option>취소신청</option>
                    <option>취소완료</option>
                  </select>
                </li>
                <li>
                  <div className="input-group keyword">
                    <select className="custom-select">
                      <option>예약자명</option>
                      <option>연락처</option>
                      <option>선상명</option>
                      <option>상품명</option>
                    </select>
                    <input
                      type="text"
                      className="form-control"
                      placeholder="검색어 입력"
                      value=""
                    />
                  </div>
                </li>
                <li>
                  <label htmlFor="selPay" className="sr-only">
                    결제방법
                  </label>
                  <select className="form-control" id="selPay">
                    <option>결제방법 전체</option>
                    <option>신용카드</option>
                    <option>무통장입금</option>
                    <option>실시간계좌이체</option>
                    <option>휴대폰</option>
                  </select>
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
                <div className="card card-sm">
                  <div className="row no-gutters d-flex align-items-center">
                    <div className="cardProfileWrap text-center">
                      <img
                        src="/assets/smartfishing/img/sample/profile5.jpg"
                        className="profile-thumb-md align-self-center mt-3 mb-1"
                        alt="profile"
                      />
                      <br />
                      <strong>김새론</strong>
                    </div>
                    <div className="cardProfileInfoWrap">
                      <div className="card-body">
                        <h6>
                          어복황제3호 <small className="grey">| 갈치</small>
                        </h6>
                        <p>
                          <a>
                            <strong className="text-primary">
                              예약번호: 20082501
                            </strong>
                          </a>
                          <br />
                          출조일: 2020년 08월 25일(화) 09:00 ~<br />
                          결제일: 2020-02-06 오후 11:56:20
                          <br />
                          예약인원; 2명
                          <br />
                          결제금액: 140,000원
                          <br />
                          <strong className="text-primary">결제완료</strong>
                        </p>
                      </div>
                    </div>
                    <div className="cardProfileStatusWrap">
                      <a
                        className="btn btn-primary btn-sm"
                        data-toggle="modal"
                        data-target="#confirmModal"
                      >
                        예약승인
                      </a>
                    </div>
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
