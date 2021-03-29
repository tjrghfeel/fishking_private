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
            <NavigationLayout title={"카메라설정"} showBackIcon={true} />

            <div className="container nopadding">
              <div className="card mt-3">
                <h4 className="text-center">
                  어복황제3호 <br /> <strong className="large orange">5</strong>{" "}
                  <small className="grey">대의 카메라 제어중</small>
                </h4>
              </div>
            </div>
            <p className="space mt-2"></p>

            <div className="container nopadding mt-3 card card-sm">
              <div className="row no-gutters mt-3 mb-2">
                <div className="col-4 pl-3">
                  <div className="cardimgWrap">
                    <img
                      src="/assets/smartsail/img/sample/boat1.jpg"
                      className="img-fluid"
                      alt=""
                    />
                  </div>
                </div>
                <div className="col-5">
                  <h6>카메라 좌측</h6>
                  <p>
                    <span className="grey">
                      조회수 4,321
                      <br />
                      27명 시청중
                    </span>
                  </p>
                </div>
                <div className="col-3 text-center">
                  <nav>
                    <div
                      className="nav nav-tabs btn-set mt-3 mr-3 vam"
                      id="nav-tab"
                      role="tablist"
                    >
                      <a
                        className="nav-link active btn btn-on"
                        id="nav-home-tab"
                        data-toggle="tab"
                        href="#nav-on"
                        role="tab"
                        aria-controls="nav-on"
                        aria-selected="true"
                      >
                        ON
                      </a>
                      <a
                        className="nav-link btn btn-off"
                        id="nav-profile-tab"
                        data-toggle="tab"
                        href="#nav-off"
                        role="tab"
                        aria-controls="nav-off"
                        aria-selected="false"
                      >
                        OFF
                      </a>
                    </div>
                  </nav>
                </div>
              </div>
              <hr className="full mt-2 mb-3" />
            </div>

            <div className="fixed-bottom">
              <div className="row no-gutters">
                <div className="col-12">
                  <a className="btn btn-primary btn-lg btn-block">확인</a>
                </div>
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
