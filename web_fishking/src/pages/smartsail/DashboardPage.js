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
        return (
          <React.Fragment>
            <NavigationLayout
              title={
                <span className="navbar-title">
                  <img
                    src="/assets/smartsail/img/svg/navbar-logo-smartship.svg"
                    alt="승선확인"
                  />
                </span>
              }
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
            <SmartSailMainTab activeIndex={0} />

            <div className="container nopadding mt-2">
              <h5>금일승선현황</h5>
              <div className="card-round-box-grey text-center">
                <div className="row mt-3 mb-3 d-flex align-items-center">
                  <div className="col-6">
                    <p className="mt-2 mb-2">
                      <img src="/assets/smartsail/img/svg/chart-b.svg" alt="" />
                    </p>
                  </div>
                  <div className="col-6 text-left">
                    <p>
                      <a>
                        <small className="grey">승선대기</small> :{" "}
                        <strong className="large">13</strong>
                      </a>
                    </p>
                    <p>
                      <a>
                        <small className="grey">승선확인</small> :{" "}
                        <strong className="large text-primary">87</strong>
                      </a>
                    </p>
                    <p>
                      <a>
                        <small className="grey">확인실패</small> :{" "}
                        <strong className="large orange">87</strong>
                      </a>
                    </p>
                    <p>
                      <a>
                        <small className="grey">승선취소</small> :{" "}
                        <strong className="large text-danger">13</strong>
                      </a>
                    </p>
                  </div>
                </div>
              </div>
            </div>

            <p className="space mt-2"></p>
            <div className="container nopadding mt-2">
              <div className="row mt-1 d-flex align-items-center">
                <div className="col-4">
                  <h5>금일 승선 대기자</h5>
                </div>
                <div className="col-8 text-right">
                  <div className="custom-control custom-radio custom-control-inline">
                    <input
                      type="radio"
                      id="customRadioInline1"
                      name="customRadioInline1"
                      className="custom-control-input"
                    />
                    <label
                      className="custom-control-label"
                      htmlFor="customRadioInline1"
                    >
                      최신순
                    </label>
                  </div>
                  <div className="custom-control custom-radio custom-control-inline">
                    <input
                      type="radio"
                      id="customRadioInline2"
                      name="customRadioInline1"
                      className="custom-control-input"
                    />
                    <label
                      className="custom-control-label"
                      htmlFor="customRadioInline2"
                    >
                      선상명순
                    </label>
                  </div>
                  <div className="custom-control custom-radio custom-control-inline">
                    <input
                      type="radio"
                      id="customRadioInline3"
                      name="customRadioInline1"
                      className="custom-control-input"
                    />
                    <label
                      className="custom-control-label"
                      htmlFor="customRadioInline3"
                    >
                      승선자순
                    </label>
                  </div>
                </div>
              </div>
            </div>

            <div className="container nopadding mt-2">
              <div className="card-round-grey">
                <div className="card card-sm">
                  <div className="row no-gutters d-flex align-items-center">
                    <div className="col-2 cardProfileWrap text-center">
                      <img
                        src="/assets/smartsail/img/sample/profile5.jpg"
                        className="profile-thumb-md align-self-center mb-1"
                        alt="profile"
                      />
                      <br />
                      <strong>김새론</strong>
                    </div>
                    <div className="col-6">
                      <a>
                        <p>
                          선상명: <strong className="large">챔피언 1호</strong>
                          <br />
                          상품명:{" "}
                          <strong className="large text-info">
                            우럭(오전)
                          </strong>
                          <br />
                          연락처: 010-1234-5678
                        </p>
                      </a>
                    </div>
                    <div className="col-4 text-right">
                      <a className="btn btn-round btn-dark">
                        <img
                          src="/assets/smartsail/img/svg/icon-jimun.svg"
                          className="vam"
                        />
                        지문입력
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
