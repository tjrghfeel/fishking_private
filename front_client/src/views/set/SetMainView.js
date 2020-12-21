import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layouts/Navigation";

export default inject("AppStore")(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const {
          AppStore: { loggedIn, version },
          history,
        } = this.props;
        return (
          <>
            {/** Navigation */}
            <Navigation title={"설정"} showBack={true} />

            {/** 입력 */}
            <div className="container nopadding mt-0">
              <div className="pt-0">
                <hr className="full mt-0 mb-3" />
                {loggedIn && (
                  <>
                    <a onClick={() => history.push(`/set/profile`)}>
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
                  </>
                )}
                <a onClick={() => history.push(`/set/alarm`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">알림설정</div>
                    <div className="col-8 text-right"></div>
                    <div className="col-1 text-right pl-1">
                      <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
                <a onClick={() => history.push(`/set/device`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">접근 권한 설정</div>
                    <div className="col-8 text-right"></div>
                    <div className="col-1 text-right pl-1">
                      <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
                <a onClick={() => history.push(`/set/vod`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">동영상 설정</div>
                    <div className="col-8 text-right"></div>
                    <div className="col-1 text-right pl-1">
                      <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
                <a onClick={() => history.push(`/doc/main`)}>
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
                  <>
                    <a onClick={() => history.push(`/common/signout`)}>
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
                  </>
                )}
                <a onClick={() => history.push(`/common/version`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">버전정보</div>
                    <div className="col-8 text-right">
                      <strong>{version}</strong> &nbsp;{" "}
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
          </>
        );
      }
    }
  )
);
