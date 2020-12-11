/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";
import ConfirmModal from "../../components/modal/ConfirmModal";

export default inject(
  "MemberStore",
  "ExternalStore"
)(
  observer(({ ExternalStore, MemberStore: { memberId }, history }) => {
    /** 마이메뉴 데이터 */
    const menus = [
      {
        text: "글쓰기",
        pathname: "story-add.html",
        imgSrc: "/assets/img/svg/mymenu-write.svg",
        requiredLogin: true,
      },
      {
        text: "내글관리",
        pathname: "my-post.html",
        imgSrc: "/assets/img/svg/mymenu-mypost.svg",
        requiredLogin: true,
      },
      {
        text: "찜한업체",
        pathname: "my-zzim.html",
        imgSrc: "/assets/img/svg/mymenu-zzim.svg",
        requiredLogin: true,
      },
      {
        text: "실시간조황",
        pathname: "boat.html",
        imgSrc: "/assets/img/svg/mymenu-live.svg",
        requiredLogin: false,
      },
      {
        text: "물때",
        pathname: "tide.html",
        imgSrc: "/assets/img/svg/mymenu-tide.svg",
        requiredLogin: false,
      },
      {
        text: "공지사항",
        pathname: "notice.html",
        imgSrc: "/assets/img/svg/mymenu-notice.svg",
        requiredLogin: false,
      },
      {
        text: "이벤트",
        pathname: "event.html",
        imgSrc: "/assets/img/svg/mymenu-event.svg",
        requiredLogin: false,
      },
      {
        text: "고객센터",
        pathname: "cs-faq.html",
        imgSrc: "/assets/img/svg/mymenu-cs.svg",
        requiredLogin: false,
      },
    ];
    return (
      <>
        {/** Navigation */}
        <Navigation
          title={"마이메뉴"}
          visibleConfigIcon={true}
          visibleSearchIcon={true}
        />

        {/** 프로필 */}
        <div className="container padding">
          <div className="media d-flex align-items-center">
            {!memberId && (
              <div className="media-body">
                <p className="text-center">
                  회원가입 시 할인쿠폰 및 다양한 서비스 이용이 가능합니다.
                </p>
              </div>
            )}
            {memberId && (
              <>
                <img
                  src="/assets/img/sample/profile3.jpg"
                  className="profile-thumb-md align-self-center mr-2"
                  alt="profile"
                />
                <div className="media-body">
                  <h6>
                    <strong>바다공주</strong>
                    <a
                      href="my-profile-edit.html"
                      className="btn btn-round-grey btn-xs float-right"
                    >
                      프로필
                    </a>
                  </h6>
                </div>
              </>
            )}
          </div>
        </div>

        {/** 예약/쿠폰 */}
        <div className="container nopadding">
          {!memberId && (
            <a
              onClick={() => history.push(`/member/login`)}
              className="btn btn-primary btn-round btn-lg btn-block cs-padding"
            >
              로그인 및 회원가입 하기
            </a>
          )}
          {memberId && (
            <a href="my-reservation.html">
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
                    <strong className="text-primary large">1</strong>
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
          <div className="row no-gutters d-flex align-items-center mt-3">
            <div className="col-2">내 쿠폰</div>
            <div className="col-4 text-right">
              <a
                onClick={() =>
                  memberId
                    ? history.push(`my-coupon.html`)
                    : history.push(`/member/login`)
                }
              >
                <strong className="text-primary large"></strong>
                <small className="text-secondary"></small>
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
                onClick={() =>
                  memberId
                    ? history.push(`my-alarm.html`)
                    : history.push(`/member/login`)
                }
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
          <a href="coupon.html">
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
            {menus.map((data, index) => (
              <a
                key={index}
                className="nav-link"
                onClick={() => {
                  if (!data.requiredLogin || memberId !== null) {
                    history.push(data.pathname);
                  } else {
                    history.push(`/member/login`);
                  }
                }}
              >
                <figure>
                  <img src={data.imgSrc} alt="" />
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
            ExternalStore.link_call(process.env.REACT_APP_CS_PHONE)
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
  })
);
