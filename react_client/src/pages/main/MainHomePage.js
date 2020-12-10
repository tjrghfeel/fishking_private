/* eslint-disable jsx-a11y/anchor-is-valid */
import React, { useEffect } from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";
import Clearfix from "../../components/layout/Clearfix";
import SlideList01 from "../../components/list/SlideList01";
import CarouselList01 from "../../components/list/CarouselList01";

export default inject("PageStore")(
  observer(({ PageStore }) => {
    useEffect(() => {
      (async () => {
        await PageStore.injectScript("/assets/js/jquery.touchSwipe.min.js");
        await PageStore.injectScript("/assets/js/swiper.min.js");
        await PageStore.injectScript("/assets/js/default.js");
      })();
      return () => {
        PageStore.removeInjectetdScripts();
      };
    });
    return (
      <>
        {/** Navigation */}
        <Navigation type={"index"} />

        {/** Visual Carousel */}
        <CarouselList01
          carouselType={"ListItem03"}
          list={[
            { imgSrc: "/assets/img/slide1.jpg" },
            { imgSrc: "/assets/img/slide1.jpg" },
            { imgSrc: "/assets/img/slide1.jpg" },
          ]}
        />
        <Clearfix />

        {/** Quick Link */}
        <div className="container">
          <nav className="nav nav-pills quick nav-justified">
            <a className="nav-link">
              <img src="/assets/img/svg/quick-boat.svg" alt="바다낚시" /> 선상
              실시간 예약
            </a>{" "}
            &nbsp;&nbsp;
            <a className="nav-link">
              <img src="/assets/img/svg/quick-rocks.svg" alt="갯바위낚시" />{" "}
              갯바위 실시간 예약
            </a>
          </nav>
        </div>
        <Clearfix />

        {/** Content */}
        <div className="container nopadding">
          {/** Content > 실시간 조황 */}
          <SlideList01
            title={"실시간 조황"}
            itemType={"ListItem01"}
            hasMore={true}
            list={[
              {
                imgSrc: "/assets/img/sample/boat1.jpg",
                text: "챔피온 1호",
                textPrimary: "돌문어",
                location: "전남 여수시",
                isLive: true,
              },
              {
                imgSrc: "/assets/img/sample/boat2.jpg",
                text: "챔피온 2호",
                textPrimary: "돌문어",
                location: "전남 여수시",
                playTime: "20:17",
              },
            ]}
          />
          {/** Content > 출조 정보 */}
          <SlideList01
            title={"출조 정보"}
            itemType={"ListItem01"}
            hasMore={true}
            list={[
              {
                imgSrc: "/assets/img/sample/boat1.jpg",
                text: "챔피온 1호",
                textPrimary: "돌문어",
                location: "전남 여수시",
                isLive: true,
                price: 70000,
              },
              {
                imgSrc: "/assets/img/sample/boat2.jpg",
                text: "챔피온 2호",
                textPrimary: "돌문어",
                location: "전남 여수시",
                playTime: "20:17",
                price: 70000,
              },
            ]}
          />
          {/** Content > 추천 업체 */}
          <div className="mainAdWrap">
            <h5>
              추천 업체 &nbsp;
              <small className="text-primary">
                <strong>AD</strong>
              </small>
            </h5>
            <CarouselList01
              carouselType={"ListItem04"}
              list={[
                {
                  imgSrc: "/assets/img/sample/boat2.jpg",
                  text: "어복황제 1호",
                  textPrimary: "쭈꾸미",
                  location: "전남 완도군",
                  distance: "27",
                  price: 70000,
                },
                {
                  imgSrc: "/assets/img/sample/boat2.jpg",
                  text: "어복황제 1호",
                  textPrimary: "쭈꾸미",
                  location: "전남 완도군",
                  distance: "27",
                  price: 70000,
                },
              ]}
            />
          </div>
          {/** Content > 조황 일지 */}
          <SlideList01
            title={"조황 일지"}
            itemType={"ListItem01"}
            hasMore={true}
            list={[
              {
                imgSrc: "/assets/img/sample/photo8.jpg",
                text: "이글스호 백조기 조황",
                textPrimary: "돌문어",
                location: "전남 여수시",
              },
              {
                imgSrc: "/assets/img/sample/photo8.jpg",
                text: "이글스호 백조기 조황",
                textPrimary: "돌문어",
                location: "전남 여수시",
              },
            ]}
          />
          {/** Content > 지역별 조황 */}
          <div className="mainMapWrap">
            <h5>지역별 조황</h5>
            <div className="mainMap">
              <a>
                <div className="infoMap NorthWestSea">
                  서해북부 <strong>7</strong>
                </div>
              </a>
              <a>
                <div className="infoMap CentralWestSea">
                  서해중부 <strong>9</strong>
                </div>
              </a>
              <a>
                <div className="infoMap SouthWestSea">
                  서해남부 <strong>12</strong>
                </div>
              </a>
              <a>
                <div className="infoMap WestSouthSea">
                  남해서부 <strong>11</strong>
                </div>
              </a>
              <a>
                <div className="infoMap CentralSouthSea">
                  남해중부 <strong>17</strong>
                </div>
              </a>
              <a>
                <div className="infoMap EastSouthSea">
                  남해동부 <strong>12</strong>
                </div>
              </a>
              <a>
                <div className="infoMap SouthEastSea">
                  동해남부 <strong>7</strong>
                </div>
              </a>
              <a>
                <div className="infoMap CentralEastSea">
                  동해중부 <strong>5</strong>
                </div>
              </a>
              <a>
                <div className="infoMap NorthEastSea">
                  동해북부 <strong>3</strong>
                </div>
              </a>
              <a>
                <div className="infoMap Jeju">
                  제주도 <strong>11</strong>
                </div>
              </a>
            </div>
          </div>
          {/** 어종별 조황 */}
          <SlideList01
            cls={"fishList"}
            title={"어종별 조황"}
            itemType={"ListItem05"}
            hasMore={true}
            list={[
              {
                imgSrc: "/assets/img/fish/fish_icon_01w.svg",
                text: "갈치",
                count: 17,
              },
              {
                imgSrc: "/assets/img/fish/fish_icon_02w.svg",
                text: "광어",
                count: 17,
              },
            ]}
          />
        </div>
      </>
    );
  })
);
