import React, { useEffect } from "react";
import { inject, observer } from "mobx-react";

import Navigation from "../../components/layouts/Navigation";
import Clearfix from "../../components/layouts/Clearfix";
import Container from "../../components/layouts/Container";
import SlideList from "../../components/lists/SlideList";
import ListItem01 from "../../components/lists/ListItem01";
import CarouselList from "../../components/lists/CarouselList";
import ListItem02 from "../../components/lists/ListItem02";
import ListItem03 from "../../components/lists/ListItem03";
import Space from "../../components/layouts/Space";

const IndexPage = inject("PageStore")(
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
        {/** 네비게이션 */}
        <Navigation type={"index"} />

        {/** Visual Carousel */}
        <CarouselList
          id={"carousel-visual"}
          renderItem={(data) => (
            <img src={data.imgSrc} className="d-block w-100" alt="" />
          )}
          list={[
            { imgSrc: "/assets/img/slide1.jpg" },
            { imgSrc: "/assets/img/slide1.jpg" },
            { imgSrc: "/assets/img/slide1.jpg" },
          ]}
        />

        <Clearfix />

        {/** Quick Link */}
        <Container>
          <nav className="nav nav-pills quick nav-justified">
            <a className="nav-link" href="search-reserv.html">
              <img src="/assets/img/svg/quick-boat.svg" alt="바다낚시" /> 선상
              실시간 예약
            </a>{" "}
            &nbsp;&nbsp;
            <a className="nav-link" href="search-reserv.html">
              <img src="/assets/img/svg/quick-rocks.svg" alt="갯바위낚시" />{" "}
              갯바위 실시간 예약
            </a>
          </nav>
        </Container>
        <Clearfix />

        {/** Content */}
        <Container cls={"nopadding"}>
          {/**** Content > 실시간 조황 */}
          <SlideList
            title={"실시간 조황"}
            hasMore={true}
            navigateMore={"boat.html"}
            renderItem={(data) => <ListItem01 {...data} />}
            list={[
              {
                navigateTo: "boat-detail.html",
                imgSrc: "/assets/img/sample/live1.jpg",
                isLive: true,
                title: "챔피온 1호",
                textPrimary: "돌문어",
                location: "전남 여수시",
              },
              {
                navigateTo: "boat-detail.html",
                imgSrc: "/assets/img/sample/live1.jpg",
                playTime: "20:17",
                title: "챔피온 1호",
                textPrimary: "돌문어",
                location: "전남 여수시",
              },
            ]}
          ></SlideList>
          {/**** Content > 출조 정보 */}
          <SlideList
            title={"출조 정보"}
            hasMore={true}
            navigateMore={"boat.html"}
            renderItem={(data) => <ListItem01 {...data} />}
            list={[
              {
                navigateTo: "boat-detail.html",
                imgSrc: "/assets/img/sample/boat1.jpg",
                playTime: "20:17",
                title: "챔피온 1호",
                textPrimary: "돌문어",
                location: "전남 여수시",
                price: 70000,
              },
              {
                navigateTo: "boat-detail.html",
                imgSrc: "/assets/img/sample/boat2.jpg",
                playTime: "20:17",
                title: "챔피온 1호",
                textPrimary: "돌문어",
                location: "전남 여수시",
                price: 70000,
              },
            ]}
          ></SlideList>

          {/**** Content > 광고배너 */}
          <div className="mainAdWrap">
            <h5>
              추천 업체 &nbsp;
              <small className="text-primary">
                <strong>AD</strong>
              </small>
            </h5>
            <CarouselList
              id={"carouselRecommend"}
              renderItem={(data) => <ListItem02 {...data} />}
              list={[
                {
                  navigateTo: "boat-detail.html",
                  imgSrc: "/assets/img/sample/boat2.jpg",
                  title: "어복황제1호",
                  textPrimary: "쭈꾸미",
                  location: "전남 진도군 27km",
                  price: 70000,
                },
                {
                  navigateTo: "boat-detail.html",
                  imgSrc: "/assets/img/sample/boat2.jpg",
                  title: "어복황제1호",
                  textPrimary: "쭈꾸미",
                  location: "전남 진도군 27km",
                  price: 70000,
                },
              ]}
            />
          </div>

          {/**** Content > 조황 일지 */}
          <SlideList
            title={"조황 일지"}
            hasMore={true}
            navigateMore={"boat.html"}
            renderItem={(data) => <ListItem01 {...data} />}
            list={[
              {
                navigateTo: "boat-detail.html",
                imgSrc: "/assets/img/sample/photo8.jpg",
                title: "이글스호 백조기 조황",
                textPrimary: "돌문어",
                location: "전남 여수시",
              },
              {
                navigateTo: "boat-detail.html",
                imgSrc: "/assets/img/sample/photo7.jpg",
                title: "이글스호 백조기 조황",
                textPrimary: "돌문어",
                location: "전남 여수시",
              },
            ]}
          />

          {/**** Content > 지역별 조황 */}
          <div className="mainMapWrap">
            <h5>지역별 조황</h5>
            <div className="mainMap">
              <a href="boat.html">
                <div className="infoMap NorthWestSea">
                  서해북부 <strong>7</strong>
                </div>
              </a>
              <a href="boat.html">
                <div className="infoMap CentralWestSea">
                  서해중부 <strong>9</strong>
                </div>
              </a>
              <a href="boat.html">
                <div className="infoMap SouthWestSea">
                  서해남부 <strong>12</strong>
                </div>
              </a>
              <a href="boat.html">
                <div className="infoMap WestSouthSea">
                  남해서부 <strong>11</strong>
                </div>
              </a>
              <a href="boat.html">
                <div className="infoMap CentralSouthSea">
                  남해중부 <strong>17</strong>
                </div>
              </a>
              <a href="boat.html">
                <div className="infoMap EastSouthSea">
                  남해동부 <strong>12</strong>
                </div>
              </a>
              <a href="boat.html">
                <div className="infoMap SouthEastSea">
                  동해남부 <strong>7</strong>
                </div>
              </a>
              <a href="boat.html">
                <div className="infoMap CentralEastSea">
                  동해중부 <strong>5</strong>
                </div>
              </a>
              <a href="boat.html">
                <div className="infoMap NorthEastSea">
                  동해북부 <strong>3</strong>
                </div>
              </a>
              <a href="boat.html">
                <div className="infoMap Jeju">
                  제주도 <strong>11</strong>
                </div>
              </a>
            </div>
          </div>

          {/**** Content > 어종별 조황 */}
          <SlideList
            cls={"fishList"}
            title={"어종별 조황"}
            hasMore={true}
            navigateMore={"boat.html"}
            renderItem={(data) => <ListItem03 {...data} />}
            list={[
              {
                navigateTo: "boat.html",
                imgSrc: "/assets/img/fish/fish_icon_01w.svg",
                title: "갈치",
                count: 17,
              },
              {
                navigateTo: "boat.html",
                imgSrc: "/assets/img/fish/fish_icon_02w.svg",
                title: "광어",
                count: 17,
              },
            ]}
          />
          <Space />
        </Container>
      </>
    );
  })
);

export default IndexPage;
