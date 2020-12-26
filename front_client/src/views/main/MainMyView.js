import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layouts/Navigation";
import ConfirmModal from "../../components/modals/ConfirmModal";
import Http from "../../Http";

export default inject(
  "AppStore",
  "NativeStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {};
      }
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const {
          AppStore: { loggedIn },
        } = this.props;

        if (loggedIn) {
          const resolve = await Http._get("/v2/api/myMenuPage");
          this.setState(resolve);
        }
      }

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const {
          history,
          AppStore: { loggedIn },
          NativeStore: { linking },
        } = this.props;
        return (
          <>
            {/** Navigation */}
            <Navigation
              title={"마이메뉴"}
              showConfig={true}
              showSearch={true}
            />

            {/** 프로필 :: 로그인 */}
            {loggedIn && (
              <div className="container padding">
                <div className="media d-flex align-items-center">
                  <img
                    src={this.state.profileImage}
                    className="profile-thumb-md align-self-center mr-2"
                    alt="profile"
                  />
                  <div className="media-body">
                    <h6>
                      <strong>{this.state.nickName}</strong>
                      <a
                        onClick={() => history.push(`/set/profile`)}
                        className="btn btn-round-grey btn-xs float-right"
                      >
                        프로필
                      </a>
                    </h6>
                  </div>
                </div>
              </div>
            )}
            {/** 프로필 :: 로그아웃 */}
            {!loggedIn && (
              <div className="container padding">
                <div className="media d-flex align-items-center">
                  <div className="media-body">
                    <p className="text-center">
                      회원가입 시 할인쿠폰 및 다양한 서비스 이용이 가능합니다.
                    </p>
                  </div>
                </div>
              </div>
            )}

            {/** 예약/쿠폰 */}
            <div className="container nopadding">
              {loggedIn && (
                <a onClick={() => history.push(`/my/reservation`)}>
                  <div className="card-round-box">
                    <div className="row no-gutters d-flex align-items-center">
                      <div className="col-7">
                        <img
                          src="/assets/img/svg/icon-reservation.svg"
                          alt="Set"
                          className="vam mr-1"
                        />
                        <strong>나의 예약내역 바로가기</strong>
                      </div>
                      <div className="col-5 text-right">
                        <strong className="text-primary large">
                          {Intl.NumberFormat().format(
                            this.state.bookingCount || 0
                          )}
                        </strong>
                        <small className="text-secondary">건</small>
                        <img
                          src="/assets/img/svg/arrow-grey.svg"
                          alt="Set"
                          className="vam ml-1"
                        />
                      </div>
                    </div>
                  </div>
                </a>
              )}
              {!loggedIn && (
                <a
                  onClick={() => history.push(`/common/login`)}
                  className="btn btn-primary btn-round btn-lg btn-block cs-padding"
                >
                  로그인 및 회원가입 하기
                </a>
              )}
              <div className="row no-gutters d-flex align-items-center mt-3">
                <div className="col-2">내 쿠폰</div>
                <div className="col-4 text-right">
                  <a
                    onClick={() => {
                      if (loggedIn) history.push(`/my/coupon`);
                      else history.push(`/common/login`);
                    }}
                  >
                    <strong className="text-primary large">
                      {Intl.NumberFormat().format(this.state.couponCount || 0)}
                    </strong>
                    <small className="text-secondary">장</small>
                    <img
                      src="/assets/img/svg/arrow-grey.svg"
                      alt="Set"
                      className="vam ml-1"
                    />
                  </a>
                </div>
                <div className="col-1"></div>
                <div className="col-2">알림</div>
                <div className="col-3 text-right">
                  <a
                    onClick={() => {
                      if (loggedIn) history.push(`/my/alarm`);
                      else history.push(`/common/login`);
                    }}
                  >
                    <strong className="text-primary large">0</strong>
                    <small className="text-secondary">건</small>
                    <img
                      src="/assets/img/svg/arrow-grey.svg"
                      alt="Set"
                      className="vam ml-1"
                    />
                  </a>
                </div>
              </div>
              <p className="space mt-3 mb-0"></p>
              <a onClick={() => history.push(`/common/coupon/available`)}>
                <div className="row no-gutters d-flex align-items-center mt-2">
                  <div className="col-9">
                    <strong>
                      할인받기 아주 쉬운 <span className="red">혜택쿠폰</span>{" "}
                      받으러가기!
                    </strong>
                  </div>
                  <div className="col-3 text-right">
                    <img
                      src="/assets/img/svg/img-coupon.svg"
                      alt="Set"
                      className="vam ml-1"
                    />
                  </div>
                </div>
              </a>
              <p className="space mt-2"></p>
            </div>

            {/** 마이메뉴 */}
            <div className="container nopadding">
              <nav className="nav nav-pills nav-sel nav-my nav-col-4">
                <a
                  className="nav-link"
                  onClick={() => {
                    if (loggedIn) history.push(`/story/add`);
                    else history.push(`/common/login`);
                  }}
                >
                  <figure>
                    <img src="/assets/img/svg/mymenu-write.svg" alt="" />
                  </figure>
                  <span>글쓰기</span>
                </a>
                <a
                  className="nav-link"
                  onClick={() => {
                    if (loggedIn) history.push(`/my/story/post`);
                    else history.push(`/common/login`);
                  }}
                >
                  <figure>
                    <img src="/assets/img/svg/mymenu-mypost.svg" alt="" />
                  </figure>
                  <span>내글관리</span>
                </a>
                <a
                  className="nav-link"
                  onClick={() => {
                    if (loggedIn) history.push(`/my/zzim`);
                    else history.push(`/common/login`);
                  }}
                >
                  <figure>
                    <img src="/assets/img/svg/mymenu-zzim.svg" alt="" />
                  </figure>
                  <span>찜한업체</span>
                </a>
                <a
                  className="nav-link"
                  onClick={() => history.push(`/boat/main`)}
                >
                  <figure>
                    <img src="/assets/img/svg/mymenu-live.svg" alt="" />
                  </figure>
                  <span>실시간조황</span>
                </a>
                <a
                  className="nav-link"
                  onClick={() => history.push(`/common/tide`)}
                >
                  <figure>
                    <img src="/assets/img/svg/mymenu-tide.svg" alt="" />
                  </figure>
                  <span>물때</span>
                </a>
                <a
                  className="nav-link"
                  onClick={() => history.push(`/common/notice`)}
                >
                  <figure>
                    <img src="/assets/img/svg/mymenu-notice.svg" alt="" />
                  </figure>
                  <span>공지사항</span>
                </a>
                <a
                  className="nav-link"
                  onClick={() => history.push(`/common/event`)}
                >
                  <figure>
                    <img src="/assets/img/svg/mymenu-event.svg" alt="" />
                  </figure>
                  <span>이벤트</span>
                </a>
                <a
                  className="nav-link"
                  onClick={() => history.push(`/common/cs/faq`)}
                >
                  <figure>
                    <img src="/assets/img/svg/mymenu-cs.svg" alt="" />
                  </figure>
                  <span>고객센터</span>
                </a>
              </nav>
              <p className="space mt-2"></p>
            </div>

            {/** 고객센터 */}
            <div className="container nopadding">
              <h6 className="text-center mt-3">고객센터</h6>
              <p className="text-center">
                <small className="grey">
                  365일 10시~18시 운영 (점심시간 12시 ~ 13시 30분)
                </small>
              </p>
              <div className="row no-gutters no-gutters-cs d-flex align-items-center mt-4">
                <div className="col-6">
                  <a
                    href="#none"
                    className="btn btn-yellow btn-round btn-lg btn-block cs-padding"
                  >
                    <img
                      src="/assets/img/svg/icon-talk.svg"
                      alt="Set"
                      className="vam mr-1"
                    />
                    카카오 상담톡
                  </a>
                </div>
                <div className="col-6" style={{ paddingRight: "0px" }}>
                  <a
                    href="#none"
                    className="btn btn-grey btn-round btn-lg btn-block cs-padding"
                    data-toggle="modal"
                    data-target="#callModal"
                  >
                    <img
                      src="/assets/img/svg/icon-call.svg"
                      alt="Set"
                      className="vam mr-1"
                    />
                    1234-7777
                  </a>
                </div>
              </div>
            </div>

            {/** 모달 팝업 */}
            <ConfirmModal
              id={"callModal"}
              title={"전화걸기"}
              textOk={"통화"}
              onClickOK={() =>
                linking("tel:".concat(process.env.REACT_APP_CS_PHONE))
              }
            >
              <p>
                고객센터로 전화연결 하시겠습니까?
                <br />
                365일 10시 ~ 18시
                <br />
                (점심시간 12시 ~ 13시 30분)
              </p>
            </ConfirmModal>
          </>
        );
      }
    }
  )
);
