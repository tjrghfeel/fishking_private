import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";
import Clearfix from "../../components/layout/Clearfix";
import ScrollList01 from "../../components/list/ScrollList01";
import SelectDateModal from "../../components/modal/SelectDateModal";

export default inject()(
  observer(() => {
    return (
      <>
        {/** Navgation */}
        <Navigation title={"바다낚시"} visibleSearchIcon={true} />

        {/** Filter */}
        <div className="filterWrap">
          <div className="slideList">
            <ul className="listWrap">
              <li className="item">
                <a href="#none" className="filterLive">
                  <span className="sr-only">라이브</span>
                </a>
              </li>
              <li className="item">
                <a
                  href="#none"
                  className="filterSel"
                  data-toggle="modal"
                  data-target="#selDateModal"
                >
                  날짜
                </a>
              </li>
              <li className="item">
                <a
                  href="#none"
                  className="filterSel"
                  data-toggle="modal"
                  data-target="#selAreaModal"
                >
                  지역
                </a>
              </li>
              <li className="item">
                <a
                  href="#none"
                  className="filterSel"
                  data-toggle="modal"
                  data-target="#selFishModal"
                >
                  어종
                </a>
              </li>
              <li className="item">
                <a
                  href="#none"
                  className="filterSel"
                  data-toggle="modal"
                  data-target="#selSortModal"
                >
                  정렬
                </a>
              </li>
              <li className="item">
                <a
                  href="#none"
                  className="filterSel"
                  data-toggle="modal"
                  data-target="#selOptionModal"
                >
                  옵션
                </a>
              </li>
            </ul>
          </div>
        </div>

        {/** Content */}
        <div className="container nopadding">
          {/** Content > 인기 프리미엄 AD */}
          <Clearfix />
          <ScrollList01
            title={"인기 프리미엄 AD"}
            itemType={"ListItem06"}
            list={[
              {
                imgSrc: "/assets/img/sample/boat1.jpg",
                isLive: true,
                fishList: ["광어", "쭈꾸미", "우럭"],
                title: "어복황제1호",
                location: "전남 진도군",
                distance: 27,
                isRealtime: true,
                price: 40000,
                textNotice: "7~8월 어린이(13세 이하) 선비 무료!",
                textEvent: "9~11월 쭈꾸미/우럭 할인 이벤트 진행",
              },
              {
                imgSrc: "/assets/img/sample/boat1.jpg",
                isLive: true,
                fishList: ["광어", "쭈꾸미", "우럭"],
                title: "어복황제1호",
                location: "전남 진도군",
                distance: 27,
                isRealtime: true,
                price: 40000,
                textNotice: "7~8월 어린이(13세 이하) 선비 무료!",
                textEvent: "9~11월 쭈꾸미/우럭 할인 이벤트 진행",
              },
            ]}
          />
          {/** Content > 프리미엄 AD */}
          <Clearfix />
          <ScrollList01
            title={"프리미엄 AD"}
            itemType={"ListItem06"}
            list={[
              {
                imgSrc: "/assets/img/sample/boat1.jpg",
                playTime: "20:17",
                fishList: ["광어", "쭈꾸미", "우럭"],
                title: "어복황제1호",
                location: "전남 진도군",
                distance: 27,
                isRealtime: true,
                price: 40000,
                textNotice: "7~8월 어린이(13세 이하) 선비 무료!",
                textEvent: "9~11월 쭈꾸미/우럭 할인 이벤트 진행",
              },
              {
                imgSrc: "/assets/img/sample/boat1.jpg",
                playTime: "20:17",
                fishList: ["광어", "쭈꾸미", "우럭"],
                title: "어복황제1호",
                location: "전남 진도군",
                distance: 27,
                isRealtime: true,
                price: 40000,
                textNotice: "7~8월 어린이(13세 이하) 선비 무료!",
                textEvent: "9~11월 쭈꾸미/우럭 할인 이벤트 진행",
              },
            ]}
          />
          {/** 일반 */}
          <Clearfix />
          <ScrollList01
            title={"일반"}
            titleCls={"mb-3"}
            itemType={"ListItem07"}
            list={[
              {
                imgSrc: "/assets/img/sample/boat1.jpg",
                isLive: true,
                fishList: ["광어", "쭈꾸미", "우럭"],
                fishType: "f01",
                fishCount: 13,
                title: "어복황제1호",
                location: "전남 진도군",
                distance: 27,
                isRealtime: true,
                price: 40000,
              },
              {
                imgSrc: "/assets/img/sample/boat1.jpg",
                playTime: "20:17",
                fishList: ["광어", "쭈꾸미", "우럭"],
                fishType: "f02",
                fishCount: 13,
                title: "어복황제1호",
                location: "전남 진도군",
                distance: 27,
                isRealtime: true,
                price: 40000,
              },
            ]}
          />
        </div>

        {/** 모달 팝업 */}
        <SelectDateModal
          id={"selDateModal"}
          onSelected={(date) => console.log(date.toLocaleDateString())}
        />
      </>
    );
  })
);
