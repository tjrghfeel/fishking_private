import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, PoliceMainTab },
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
                    src="/assets/police/img/svg/navbar-logo-smartmarine.svg"
                    alt="승선확인"
                  />
                </span>
              }
              showBackIcon={true}
            />

            <div className="container nopadding mt-2">
              <h5 className="mb-1">
                선박현황 <small className="grey">(2020.01.01기준)</small>
              </h5>
            </div>
            <div className="container nopadding mt-2">
              <div className="card-round-grey">
                <div className="card card-sm">
                  <div className="row no-gutters">
                    <div className="col-6">
                      <span className="grey">업체 수 : </span>
                    </div>
                    <div className="col-6 text-right">
                      <strong className="large">53</strong>
                    </div>
                  </div>
                  <hr className="mt-1 mb-2" />
                  <div className="row no-gutters mb-1">
                    <div className="col-6">
                      <span className="grey">선박 수 : </span>
                    </div>
                    <div className="col-6 text-right">
                      <strong className="large text-primary">198</strong>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div className="container nopadding mt-2">
              <h5 className="mb-1">
                승객현황 <small className="grey">(2020.01.01기준)</small>
              </h5>
            </div>
            <div className="container nopadding mt-2">
              <div className="card-round-grey">
                <div className="card card-sm">
                  <div className="row no-gutters">
                    <div className="col-6">
                      <span className="grey">금일 예약 인원 : </span>
                    </div>
                    <div className="col-6 text-right">
                      <strong className="large">678</strong>
                    </div>
                  </div>
                  <hr className="mt-1 mb-2" />
                  <div className="row no-gutters">
                    <div className="col-6">
                      <span className="grey">금일 승선완료 인원 : </span>
                    </div>
                    <div className="col-6 text-right">
                      <strong className="large text-primary">627</strong>
                    </div>
                  </div>
                  <hr className="mt-1 mb-2" />
                  <div className="row no-gutters mb-1">
                    <div className="col-6">
                      <span className="grey">승선 대기 인원 : </span>
                    </div>
                    <div className="col-6 text-right">
                      <strong className="large red">55</strong>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div className="container nopadding mt-2">
              <h5>
                승선현황 <small className="grey">(2020.01.01기준)</small>
              </h5>
              <div className="card-round-box-grey text-center">
                <div className="row mt-3 mb-3 d-flex align-items-center">
                  <div className="col-6">
                    <p className="mt-2 mb-2">
                      <img src="/assets/police/img/svg/chart-b.svg" alt="" />
                    </p>
                  </div>
                  <div className="col-6 text-left">
                    <p>
                      <a>
                        <small className="grey">승선완료</small> :{" "}
                        <strong className="large text-primary">87</strong>
                      </a>
                    </p>
                    <p>
                      <a>
                        <small className="grey">미완료</small> :{" "}
                        <strong className="large text-danger">13</strong>
                      </a>
                    </p>
                  </div>
                </div>
              </div>
            </div>
            <p className="clearfix">
              <br />
            </p>

            <PoliceMainTab activeIndex={0} />
          </React.Fragment>
        );
      }
    }
  )
);
