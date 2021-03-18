import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, SmartFishingMainTab },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {};
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        this.loadPageData();
      }
      loadPageData = async () => {
        const { APIStore } = this.props;
        const resolve = await APIStore._get(`/v2/api/smartfishing/dashboard`);
        console.log(JSON.stringify(resolve));
      };

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
                    src="/assets/smartfishing/img/svg/navbar-logo-smartfishing.svg"
                    alt="스마트출조"
                  />
                </span>
              }
              showBackIcon={true}
            />
            <SmartFishingMainTab activeIndex={0} />

            <div className="container nopadding mt-2">
              <h5>결제현황</h5>
              <div className="card-round-box-grey text-center">
                <div className="row mt-3 mb-3">
                  <div className="col-4">
                    <strong>예약</strong>
                    <p className="mt-2 mb-2">
                      <img
                        src="/assets/smartfishing/img/svg/chart.svg"
                        alt=""
                      />
                    </p>
                    <p>
                      <small className="grey">승인필요</small> :{" "}
                      <strong className="large orange">13</strong>
                    </p>
                    <p>
                      <small className="grey">예약완료</small> :{" "}
                      <strong className="large text-primary">87</strong>
                    </p>
                  </div>
                  <div className="col-4">
                    <strong>진행</strong>
                    <p className="mt-2 mb-2">
                      <img
                        src="/assets/smartfishing/img/svg/chart.svg"
                        alt=""
                      />
                    </p>
                    <p>
                      <small className="grey">대기예약</small> :{" "}
                      <strong className="large orange">13</strong>
                    </p>
                    <p>
                      <small className="grey">예약확정</small> :{" "}
                      <strong className="large text-primary">87</strong>
                    </p>
                  </div>
                  <div className="col-4">
                    <strong>최종</strong>
                    <p className="mt-2 mb-2">
                      <img
                        src="/assets/smartfishing/img/svg/chart.svg"
                        alt=""
                      />
                    </p>
                    <p>
                      <small className="grey">예약취소</small> :{" "}
                      <strong className="large orange">13</strong>
                    </p>
                    <p>
                      <small className="grey">출조완료</small> :{" "}
                      <strong className="large text-primary">87</strong>
                    </p>
                  </div>
                </div>
              </div>
            </div>

            <p className="space mt-2"></p>
            <div className="container nopadding mt-2">
              <h5 className="mb-1">오늘의 승인필요</h5>
            </div>
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
