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
            <div
              id="carousel-visual-detail"
              className="carousel slide"
              data-ride="carousel"
            >
              <div className="float-top-left">
                <a onClick={() => PageStore.goBack()}>
                  <img
                    src="/assets/smartsail/img/svg/navbar-back.svg"
                    alt="뒤로가기"
                  />
                </a>
              </div>
              <div className="carousel-inner">
                <div className="carousel-item active">
                  <img
                    src="/assets/smartsail/img/sample/boat1.jpg"
                    className="d-block w-100"
                    alt=""
                  />
                </div>
              </div>
            </div>

            <div className="container nopadding">
              <div className="card mt-3">
                <h4 className="text-center">
                  어복황제1호 <small className="grey">|</small>{" "}
                  <small>
                    현재 <strong className="large orange">10</strong>명
                  </small>
                  <br />
                  <small className="grey">20.10.03 (토)</small>
                </h4>
              </div>
            </div>

            <div className="container nopadding mt-2">
              <div className="card-round-grey">
                <div className="card card-sm">
                  <div className="row no-gutters d-flex align-items-center">
                    <div className="col-6">
                      <a>
                        <p>
                          승선자명: <strong className="large">홍길동</strong>
                          <br />
                          연락처: 010-1234-5678
                        </p>
                      </a>
                    </div>
                    <div className="col-4 text-right">
                      <span className="status relative status3">확인완료</span>
                    </div>
                    <div className="col-2 text-right">
                      <a className="btn btn btn-round-grey btn-xs-round">
                        ― 삭제
                      </a>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <a
              onClick={() => PageStore.push(`/sail/add`)}
              className="add-circle"
            >
              <img
                src="/assets/smartsail/img/svg/icon-add-user.svg"
                alt=""
                className="add-icon"
              />
            </a>
          </React.Fragment>
        );
      }
    }
  )
);
