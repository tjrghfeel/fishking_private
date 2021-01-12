import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  Common: {
    Layout: { Navigation },
  },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      requestLogout = async () => {
        const { APIStore, PageStore } = this.props;
        const resolve = APIStore._post("/v2/api/logout");
        if (resolve) {
          PageStore.setLogin(null);
          PageStore.push(`/fishking/member/login`);
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const {
          PageStore,
          PageStore: { loggedIn },
        } = this.props;
        return (
          <React.Fragment>
            {/** Navigation */}
            <Navigation title={"설정"} showBackIcon={true} />

            {/** 입력 */}
            <div className="container nopadding mt-0">
              <div className="pt-0">
                <hr className="full mt-0 mb-3" />
                {loggedIn && (
                  <React.Fragment>
                    <a onClick={() => PageStore.push(`/common/set/profile`)}>
                      <div className="row no-gutters align-items-center">
                        <div className="col-3 pl-2">프로필 관리</div>
                        <div className="col-8 text-right"></div>
                        <div className="col-1 text-right pl-1">
                          <img
                            src="/assets/img/svg/cal-arrow-right.svg"
                            alt=""
                          />
                        </div>
                      </div>
                    </a>
                    <hr className="full mt-3 mb-3" />
                  </React.Fragment>
                )}
                <a onClick={() => PageStore.push(`/common/set/alarm`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">알림설정</div>
                    <div className="col-8 text-right"></div>
                    <div className="col-1 text-right pl-1">
                      <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
                <a>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">접근 권한 설정</div>
                    <div className="col-8 text-right"></div>
                    <div className="col-1 text-right pl-1">
                      <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
                <a onClick={() => PageStore.push(`/common/set/vod`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">동영상 설정</div>
                    <div className="col-8 text-right"></div>
                    <div className="col-1 text-right pl-1">
                      <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
                <a onClick={() => PageStore.push(`/common/policy/main`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">약관 및 정책</div>
                    <div className="col-8 text-right"></div>
                    <div className="col-1 text-right pl-1">
                      <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
                {loggedIn && (
                  <React.Fragment>
                    <a
                      onClick={() => PageStore.push(`/fishking/member/signout`)}
                    >
                      <div className="row no-gutters align-items-center">
                        <div className="col-3 pl-2">탈퇴하기</div>
                        <div className="col-8 text-right"></div>
                        <div className="col-1 text-right pl-1">
                          <img
                            src="/assets/img/svg/cal-arrow-right.svg"
                            alt=""
                          />
                        </div>
                      </div>
                    </a>
                    <hr className="full mt-3 mb-3" />
                    <a onClick={this.requestLogout}>
                      <div className="row no-gutters align-items-center">
                        <div className="col-3 pl-2">로그아웃</div>
                        <div className="col-8 text-right"></div>
                        <div className="col-1 text-right pl-1">
                          <img
                            src="/assets/img/svg/cal-arrow-right.svg"
                            alt=""
                          />
                        </div>
                      </div>
                    </a>
                    <hr className="full mt-3 mb-3" />
                  </React.Fragment>
                )}
                <a>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">버전정보</div>
                    <div className="col-8 text-right">
                      <strong>1.7.7</strong> &nbsp;{" "}
                      <span className="status-icon status6">최신버전</span>
                    </div>
                    <div className="col-1 text-right pl-1">
                      <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
