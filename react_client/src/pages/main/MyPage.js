/* eslint-disable jsx-a11y/anchor-is-valid */
import React, { useEffect } from "react";
import { inject, observer } from "mobx-react";

import Navigation from "../../components/layouts/Navigation";
import Container from "../../components/layouts/Container";
import Space from "../../components/layouts/Space";

const MyPage = inject(
  "AppStore",
  "MyStore"
)(
  observer(({ AppStore, MyStore }) => {
    useEffect(() => {
      (async () => {
        if (AppStore.memberId) {
          await MyStore.loadMyMenuPageData(AppStore.memberId);
        }
      })();
      return () => {
        if (MyStore.myMenuPageData) {
          MyStore.clearMyMenuPageData();
        }
      };
    });
    /** 마이메뉴 리스트 */
    const menus = [
      {
        pathname: "story-add.html",
        imgSrc: "/assets/img/svg/mymenu-write.svg",
        text: "글쓰기",
        loggedIn: true,
      },
      {
        pathname: "my-post.html",
        imgSrc: "/assets/img/svg/mymenu-mypost.svg",
        text: "내글관리",
        loggedIn: true,
      },
      {
        pathname: "my-zzim.html",
        imgSrc: "/assets/img/svg/mymenu-zzim.svg",
        text: "찜한업체",
        loggedIn: true,
      },
      {
        pathname: "boat.html",
        imgSrc: "/assets/img/svg/mymenu-live.svg",
        text: "실시간조황",
      },
      {
        pathname: "tide.html",
        imgSrc: "/assets/img/svg/mymenu-tide.svg",
        text: "물때",
      },
      {
        pathname: "notice.html",
        imgSrc: "/assets/img/svg/mymenu-notice.svg",
        text: "공지사항",
      },
      {
        pathname: "my-event.html",
        imgSrc: "/assets/img/svg/mymenu-event.svg",
        text: "이벤트",
      },
      {
        pathname: "cs-faq.html",
        imgSrc: "/assets/img/svg/mymenu-cs.svg",
        text: "고객센터",
      },
    ];
    return (
      <>
        {/** 네비게이션 */}
        <Navigation title={"마이메뉴"} hasSearch={true} hasConfig={true} />

        {/** 프로필 */}
        <Container cls={"padding"}>
          <div className="media d-flex align-items-center">
            {MyStore.myMenuPageData === null && (
              <div className="media-body">
                <p className="text-center">
                  회원가입 시 할인쿠폰 및 다양한 서비스 이용이 가능합니다.
                </p>
              </div>
            )}
            {MyStore.myMenuPageData !== null && (
              <>
                <img
                  src={MyStore.myMenuPageData.profileImage}
                  className="profile-thumb-md align-self-center mr-2"
                  alt="profile"
                />
                <div className="media-body">
                  <h6>
                    <strong>{MyStore.myMenuPageData.nickName}</strong>
                    <a
                      onClick={() => this.navigateTo("로그인및회원가입하기")}
                      className="btn btn-round-grey btn-xs float-right"
                    >
                      프로필
                    </a>
                  </h6>
                </div>
              </>
            )}
          </div>
        </Container>

        {/** 예약&쿠폰 */}
        <Container cls={"nopadding"}>
          {MyStore.myMenuPageData === null && (
            <a
              onClick={() => this.navigateTo("로그인")}
              className="btn btn-primary btn-round btn-lg btn-block cs-padding"
            >
              로그인 및 회원가입 하기
            </a>
          )}
          {MyStore.myMenuPageData !== null && (
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
                    <strong className="text-primary large">
                      {MyStore.myMenuPageData.bookingCount || 0}
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

          <div className="row no-gutters d-flex align-items-center mt-3">
            <div className="col-2">내 쿠폰</div>
            <div className="col-4 text-right">
              <a onClick={() => this.navigateToMyCoupon()}>
                <strong className="text-primary large">
                  {(MyStore.myMenuPageData !== null &&
                    MyStore.myMenuPageData.couponCount) ||
                    0}
                </strong>
                <small className="text-secondary">건</small>
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
              <a onClick={() => this.navigateToAlarm()}>
                <strong className="text-primary large">
                  {(MyStore.myMenuPageData !== null &&
                    MyStore.myMenuPageData.alarmCount) ||
                    0}
                </strong>
                <small className="text-secondary">건</small>
                <img
                  src="/assets/img/svg/arrow-grey.svg"
                  alt="Set"
                  className="vam ml-1"
                />
              </a>
            </div>
          </div>
          <Space cls={"mt-3 mb-0"} />
          <a onClick={() => this.navigateToCoupon()}>
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
          <Space cls={"mt-2"} />
        </Container>

        {/** 마이메뉴 */}
        <Container cls={"nopadding"}>
          <nav className="nav nav-pills nav-sel nav-my nav-col-4">
            {menus.map((data, index) => (
              <a
                key={index}
                className="nav-link"
                onClick={() => this.navigateToMyMenu(data)}
              >
                <figure>
                  <img src={data.imgSrc} alt="" />
                </figure>
                <span>{data.text}</span>
              </a>
            ))}
          </nav>
          <Space cls={"mt-2"} />
        </Container>

        {/** 고객센터 */}
        <Container cls={"nopadding"}>
          <h6 className="text-center mt-3">고객센터</h6>
          <p className="text-center">
            <small className="grey">
              365일 10시~18시 운영 (점심시간 12시 ~ 13시 30분)
            </small>
          </p>
          <div className="row no-gutters no-gutters-cs d-flex align-items-center mt-4">
            <div className="col-6">
              <a
                onClick={() => this.openKakao()}
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
                onClick={() => this.openCall()}
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
        </Container>
      </>
    );
  })
);

export default MyPage;
