/* global $ */
import React, { useEffect } from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";
import SearchMainTabs from "../../components/layout/SearchMainTabs";
import SlideList01 from "../../components/list/SlideList01";

export default inject("PageStore")(
  observer(({ PageStore }) => {
    useEffect(() => {
      (async () => {
        await PageStore.injectScript("/assets/js/jquery.touchSwipe.min.js");
        await PageStore.injectScript("/assets/js/swiper.min.js");
        await PageStore.injectScript("/assets/js/default.js");
        PageStore.appyCarousel();
        PageStore.applySwipe();
      })();
      return () => {
        PageStore.removeInjectetdScripts();
      };
    });
    /** 모두보기 토글 */
    const onClickToggle = () => {
      $(".toggle-content").slideToggle("slow");
      $(this).toggleClass("active");
      return false;
      $(".toggle-content").toggleClass("expanded");
    };
    return (
      <>
        {/** Navigation */}
        <Navigation title={"검색"} visibleBackIcon={true} />

        {/** 탭메뉴 */}
        <SearchMainTabs />

        {/** 검색 */}
        <div className="container nopadding mt-3 mb-0">
          <form className="form-search">
            <a href="search-all.html">
              <img
                src="/assets/img/svg/form-search.svg"
                alt=""
                className="icon-search"
              />
            </a>
            <input
              className="form-control mr-sm-2"
              type="search"
              placeholder="어떤 낚시를 찾고 있나요?"
              aria-label="Search"
            />
            <a href="search.html">
              <img src="/assets/img/svg/navbar-search.svg" alt="Search" />
            </a>
          </form>
        </div>

        {/** 인기 검색어 */}
        <div className="container nopadding mt-2 mb-0">
          <h5>
            인기 검색어{" "}
            <small className="red">
              <strong>HOT</strong>
            </small>
          </h5>
          <ul className="list-search">
            <li>
              <a href="search-all.html">
                <strong>1</strong> 문어
              </a>
            </li>
            <li>
              <a href="search-all.html">
                <strong>2</strong> 갈치
              </a>
            </li>
            <li>
              <a href="search-all.html">
                <strong>3</strong> 갑오징어
              </a>
            </li>
            <li>
              <a href="search-all.html">
                <strong>4</strong> 갈치{" "}
                <span className="new float-right">NEW</span>
              </a>
            </li>
            <li>
              <a href="search-all.html">
                <strong>5</strong> 신봉{" "}
                <span className="new float-right">NEW</span>
              </a>
            </li>
          </ul>
          <div className="toggle-content">
            <ul className="list-search">
              <li>
                <a href="search-all.html">
                  <strong>6</strong> 문어
                </a>
              </li>
              <li>
                <a href="search-all.html">
                  <strong>7</strong> 갈치
                </a>
              </li>
              <li>
                <a href="search-all.html">
                  <strong>8</strong> 갑오징어
                </a>
              </li>
              <li>
                <a href="search-all.html">
                  <strong>9</strong> 갈치{" "}
                  <span className="new float-right">NEW</span>
                </a>
              </li>
              <li>
                <a href="search-all.html">
                  <strong>10</strong> 신봉{" "}
                  <span className="new float-right">NEW</span>
                </a>
              </li>
            </ul>
          </div>
          <div className="togglewrap">
            <a onClick={onClickToggle} className="toggle-btn"></a>
          </div>
        </div>

        {/** 어복황제 추천 */}
        <div className="container nopadding mt-2 mb-0">
          <SlideList01
            title={"어복황제 추천"}
            textPrimary={"AD"}
            cls={"slideList-md"}
            itemType={"ListItem11"}
            list={[
              {
                imgSrc: "/assets/img/sample/boat1.jpg",
                isGood: false,
                title: "챔피온 1호",
                textPrimary: "돌문어",
                location: "전남 여수시",
                price: 70000,
              },
              {
                imgSrc: "/assets/img/sample/boat1.jpg",
                isGood: true,
                title: "챔피온 1호",
                textPrimary: "돌문어",
                location: "전남 여수시",
                price: 70000,
              },
            ]}
          />
        </div>
      </>
    );
  })
);
