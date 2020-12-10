import React, { useEffect, useState } from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";
import StoryTabs from "../../components/layout/StoryTabs";
import SlideList01 from "../../components/list/SlideList01";
import ScrollList03 from "../../components/list/ScrollList03";

export default inject("PageStore")(
  observer(({ PageStore }) => {
    const [filter, setFilter] = useState(null);
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
        <Navigation title={"어복스토리"} visibleSearchIcon={true} />

        {/** 탭메뉴 */}
        <StoryTabs />

        {/** Filter */}
        <div className="filterWrap text-right">
          <div className="custom-control custom-radio custom-control-inline">
            <input
              type="radio"
              id="customRadioInline1"
              name="customRadioInline1"
              className="custom-control-input"
              onChange={() => setFilter("최신순")}
            />
            <label
              className="custom-control-label"
              htmlFor="customRadioInline1"
            >
              최신순
            </label>
          </div>
          <div className="custom-control custom-radio custom-control-inline">
            <input
              type="radio"
              id="customRadioInline2"
              name="customRadioInline1"
              className="custom-control-input"
              onChange={() => setFilter("인기순")}
            />
            <label
              className="custom-control-label"
              htmlFor="customRadioInline2"
            >
              인기순
            </label>
          </div>
        </div>

        {/** 인기영상 */}
        <div className="container nopadding mt-4">
          <SlideList01
            cls={"slideTv card-md"}
            itemType={"ListItem08"}
            hasMore={true}
            list={[
              {
                imgSrc: "/assets/img/sample/live2.jpg",
                isLive: true,
                title: "새해를 고향붕어와 함께 붕어 제대로 낚시하기",
              },
              {
                imgSrc: "/assets/img/sample/live2.jpg",
                playtime: "20:17",
                title: "새해를 고향붕어와 함께 붕어 제대로 낚시하기",
              },
            ]}
          />
        </div>
        <p className="space"></p>

        {/** List */}
        <ScrollList03
          list={[
            {
              imgSrc: "/assets/img/sample/photo1.jpg",
              playtime: "20:17",
              title: "바다황제호 8월 31일 쭈꾸미 조황",
              contents:
                "안녕하세요. 어보황제3호 선장입니다. 태풍의 영향을 받아서인지 포인트에",
            },
          ]}
        />
      </>
    );
  })
);
