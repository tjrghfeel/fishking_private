/* global Kakao */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout, MainTab },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "ModalStore",
  "NativeStore"
)(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      arr_menu = [
        {
          text: "글쓰기",
          pathname: "/story/add",
          img: "/assets/cust/img/svg/mymenu-write.svg",
          requiredLoggedIn: true,
        },
        {
          text: "내글관리",
          pathname: "/story/my/post",
          img: "/assets/cust/img/svg/mymenu-mypost.svg",
          requiredLoggedIn: true,
        },
        {
          text: "찜한업체",
          pathname: "/zzim/boat",
          img: "/assets/cust/img/svg/mymenu-zzim.svg",
          requiredLoggedIn: true,
        },
        {
          text: "실시간조황",
          pathname: "/main/company/boat?hasRealTimeVideo=true",
          img: "/assets/cust/img/svg/mymenu-live.svg",
          requiredLoggedIn: false,
        },
        {
          text: "물때",
          pathname: "/tide/today",
          img: "/assets/cust/img/svg/mymenu-tide.svg",
          requiredLoggedIn: false,
        },
        {
          text: "공지사항",
          pathname: "/cs/notice/list",
          img: "/assets/cust/img/svg/mymenu-notice.svg",
          requiredLoggedIn: false,
        },
        {
          text: "이벤트",
          pathname: "/event/list",
          img: "/assets/cust/img/svg/mymenu-event.svg",
          requiredLoggedIn: false,
        },
        {
          text: "고객센터",
          pathname: "/cs/faq",
          img: "/assets/cust/img/svg/mymenu-cs.svg",
          requiredLoggedIn: false,
        },
      ];
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const { PageStore, ModalStore } = this.props;
        const {
          msg = null,
          token = null,
        } = PageStore.getQueryParams();
        if(msg === 'niceResultParsingError'){
          ModalStore.openModal("Alert", { body: "오류" });
        }
        else if(msg === 'success'){
          localStorage.setItem("@accessToken_cust", token);
          localStorage.setItem("@accessToken", token);
          ModalStore.openModal("Alert", { body: "휴대폰 번호가 수정되었습니다." });
        }
        else if(msg === 'niceCertificationFail'){
          localStorage.setItem("@accessToken_cust", token);
          localStorage.setItem("@accessToken", token);
          ModalStore.openModal("Alert", { body: "본인인증에 실패하였습니다." });
        }

        this.loadPageData();
      }

      loadPageData = async () => {
        const { APIStore, PageStore } = this.props;
        if (PageStore.loggedIn) {
          const resolve = await APIStore._get("/v2/api/myMenuPage");
          PageStore.setState(resolve);
        } else {
          PageStore.setState({});
        }
      };

      goToMenu = (item) => {
        const { PageStore } = this.props;
        if (item.requiredLoggedIn && !PageStore.loggedIn) {
            PageStore.push(`/member/login`);
        } else {
          PageStore.push(item.pathname);
        }
      };

      requestTalk = () => {
        Kakao.Channel.chat({
          channelPublicId: "_NzxabK",
        });
      };

      requestCall = () => {
        const { ModalStore, NativeStore } = this.props;
        ModalStore.openModal("Confirm", {
          title: "전화걸기",
          body: (
            <React.Fragment>
              <p>
                고객센터로 전화연결 하시겠습니까?
                <br />
                평일 10시 ~ 18시
                <br />
                (점심시간 12시 ~ 13시 30분)
              </p>
            </React.Fragment>
          ),
          textOk: "통화",
          onOk: () =>
            NativeStore.linking("tel:".concat(process.env.REACT_APP_CS_PHONE)),
        });
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const {
          PageStore: { loggedIn = false, state = {} },
          PageStore,
        } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout
              title={"마이메뉴"}
              showSetIcon={true}
              showSearchIcon={true}
            />

            {/** 프로필 */}
            {loggedIn && (
              <div className="container padding">
                <div className="media d-flex align-items-center">
                  <img
                    src={state.profileImage}
                    className="profile-thumb-md align-self-center mr-2"
                    alt="profile"
                  />
                  <div className="media-body">
                    <h6>
                      <strong>{state.nickName}</strong>
                      <a
                        onClick={() => PageStore.push(`/set/profile`)}
                        className="btn btn-round-grey btn-xs float-right"
                      >
                        프로필
                      </a>
                    </h6>
                  </div>
                </div>
              </div>
            )}
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
                <a onClick={() => PageStore.push(`/reservation/my`)}>
                  <div className="card-round-box">
                    <div className="row no-gutters d-flex align-items-center">
                      <div className="col-7">
                        <img
                          src="/assets/cust/img/svg/icon-reservation.svg"
                          alt="Set"
                          className="vam mr-1"
                        />
                        <strong>나의 예약내역 바로가기</strong>
                      </div>
                      <div className="col-5 text-right">
                        <strong className="text-primary large">
                          {Intl.NumberFormat().format(state.bookingCount || 0)}
                        </strong>
                        <small className="text-secondary">건</small>
                        <img
                          src="/assets/cust/img/svg/arrow-grey.svg"
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
                  onClick={() => PageStore.push(`/member/login`)}
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
                      if (loggedIn) PageStore.push(`/coupon/my`);
                      else PageStore.push(`/member/login`);
                    }}
                  >
                    <strong className="text-primary large">
                      {Intl.NumberFormat().format(state.couponCount || 0)}
                    </strong>
                    <small className="text-secondary">장</small>
                    <img
                      src="/assets/cust/img/svg/arrow-grey.svg"
                      alt="Set"
                      className="vam ml-1"
                    />
                  </a>
                </div>
                <div className="col-1"></div>
                <div className="col-2">알림</div>
                <div className="col-3 text-right">
                  <a onClick={() => {
                    if (loggedIn) PageStore.push(`/cs/alarm`);
                    else PageStore.push(`/member/login`);
                  }}>
                    <strong className="text-primary large">
                      {Intl.NumberFormat().format(state.alertCount || 0)}
                    </strong>
                    <small className="text-secondary">건</small>
                    <img
                      src="/assets/cust/img/svg/arrow-grey.svg"
                      alt="Set"
                      className="vam ml-1"
                    />
                  </a>
                </div>
              </div>
              <p className="space mt-3 mb-0"></p>
              <a onClick={() => {
                if (loggedIn) PageStore.push(`/coupon/available`)
                else PageStore.push(`/member/login`);
              }}>
                <div className="row no-gutters d-flex align-items-center mt-2">
                  <div className="col-9">
                    <strong>
                      할인받기 아주 쉬운 <span className="red">혜택쿠폰</span>{" "}
                      받으러가기!
                    </strong>
                  </div>
                  <div className="col-3 text-right">
                    <img
                      src="/assets/cust/img/svg/img-coupon.svg"
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
                {this.arr_menu.map((data, index) => (
                  <a
                    key={index}
                    className="nav-link"
                    onClick={() => this.goToMenu(data)}
                  >
                    <figure>
                      <img src={data.img} alt="" />
                    </figure>
                    <span>{data.text}</span>
                  </a>
                ))}
              </nav>
              <p className="space mt-2"></p>
            </div>

            {/** 고객센터 */}
            <div className="container nopadding">
              <h6 className="text-center mt-3">고객센터</h6>
              <p className="text-center">
                <small className="grey">
                  평일 10시~18시 운영 (점심시간 12시 ~ 13시 30분)
                </small>
              </p>
              <div className="row no-gutters no-gutters-cs d-flex align-items-center mt-4">
                <div className="col-6">
                  <a
                    onClick={this.requestTalk}
                    className="btn btn-yellow btn-round btn-lg btn-block cs-padding"
                  >
                    <img
                      src="/assets/cust/img/svg/icon-talk.svg"
                      alt="Set"
                      className="vam mr-1"
                    />
                    카카오 상담톡
                  </a>
                </div>
                <div className="col-6" style={{ paddingRight: "0px" }}>
                  <a
                    onClick={this.requestCall}
                    className="btn btn-grey btn-round btn-lg btn-block cs-padding"
                  >
                    <img
                      src="/assets/cust/img/svg/icon-call.svg"
                      alt="Set"
                      className="vam mr-1"
                    />
                    {process.env.REACT_APP_CS_PHONE}
                  </a>
                </div>
              </div>
            </div>

            <MainTab activeIndex={4} />
          </React.Fragment>
        );
      }
    }
  )
);
