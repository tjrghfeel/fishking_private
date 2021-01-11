import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/common/layout/Navigation";

export default inject()(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      onChangeAlarmPost = async (is) => {
        // TODO : [PUB-OK/API-NO] 설정 > 알림 : 내 글 알림 설정 변경
      };
      onChangeAlarmCoupon = async (is) => {
        // TODO : [PUB-OK/API-NO] 설정 > 알림 : 혜택 알림 설정 변경
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** w**********/
      render() {
        return (
          <>
            {/** Navigation */}
            <Navigation title={"알림 설정"} showBack={true} />

            {/** 입력 :: 내 글 알림 */}
            <div className="container nopadding mt-3">
              <div className="row no-gutters mt-3 mb-2">
                <div className="col">
                  <strong>내 글 알림</strong>
                  <br />
                  <small className="grey">
                    작성한 내글에 대한 댓글 알림 입니다.
                  </small>
                </div>
                <div className="col">
                  <nav>
                    <div
                      className="nav nav-tabs btn-set"
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
                        onClick={() => this.onChangeAlarmPost(true)}
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
                        onClick={() => this.onChangeAlarmPost(false)}
                      >
                        OFF
                      </a>
                    </div>
                  </nav>
                </div>
              </div>
              <p className="space"></p>
            </div>
            {/** 입력 :: 혜택 알림 */}
            <div className="container nopadding mt-1">
              <div className="row no-gutters mt-3 mb-2">
                <div className="col">
                  <strong>혜택 알림</strong>
                  <br />
                  <small className="grey">
                    할인, 쿠폰 등 이벤트 혜택 알림 입니다.
                  </small>
                </div>
                <div className="col">
                  <nav>
                    <div
                      className="nav nav-tabs btn-set"
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
                        onChange={() => this.onChangeAlarmCoupon(true)}
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
                        onChange={() => this.onChangeAlarmCoupon(false)}
                      >
                        OFF
                      </a>
                    </div>
                  </nav>
                </div>
              </div>
              <p className="space"></p>
            </div>
          </>
        );
      }
    }
  )
);
