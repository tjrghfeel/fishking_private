/* eslint-disable jsx-a11y/anchor-is-valid */
import React, { useState, useRef } from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";
import Clearfix from "../../components/layout/Clearfix";
import ScrollList01 from "../../components/list/ScrollList01";
import SelectDateModal from "../../components/modal/SelectDateModal";
import SelectAreaModal from "../../components/modal/SelectAreaModal";
import SelectFishModal from "../../components/modal/SelectFishModal";
import SelectSortModal from "../../components/modal/SelectReserveSortModal";
import SelectOptionModal from "../../components/modal/SelectOptionModal";

export default inject()(
  observer(
    ({
      match: {
        params: { type },
      },
    }) => {
      const [filterLive, setFilterLive] = useState(false); // 필터::Live
      const [filterDate, setFilterDate] = useState(null); // 필터::날짜
      const [filterArea, setFilterArea] = useState([]); // 필터::지역
      const [filterFish, setFilterFish] = useState([]); // 필터::어종
      const [filterSort, setFilterSort] = useState(null); // 필터::정렬
      const [filterOption, setFilterOption] = useState({}); // 필터::옵션
      const selDateModal = useRef(null);
      const selAreaModal = useRef(null);
      const selFishModal = useRef(null);
      const selSortModal = useRef(null);
      const selOptionModal = useRef(null);

      return (
        <>
          {/** Navgation */}
          <Navigation title={"바다낚시"} visibleSearchIcon={true} />

          {/** Filter */}
          <div className="filterWrap">
            <div className="slideList">
              <ul className="listWrap">
                <li className="item">
                  <a
                    onClick={() => setFilterLive(!filterLive)}
                    className={
                      "filterLive" + (filterLive ? " ".concat("active") : "")
                    }
                  >
                    <span className="sr-only">라이브</span>
                  </a>
                </li>
                <li className="item">
                  <a
                    className={
                      "filterSel" +
                      (filterDate !== null ? " ".concat("active") : "")
                    }
                    data-toggle="modal"
                    data-target="#selDateModal"
                  >
                    날짜
                  </a>
                  {filterDate !== null && (
                    <a
                      onClick={() => {
                        selDateModal.current?.onInit();
                        setFilterDate(null);
                      }}
                    >
                      <span className="close"></span>
                    </a>
                  )}
                </li>
                <li className="item">
                  <a
                    className={
                      "filterSel" +
                      (filterArea.length > 0 ? " ".concat("active") : "")
                    }
                    data-toggle="modal"
                    data-target="#selAreaModal"
                  >
                    지역
                    {filterArea.length > 0 && <>({filterArea.length})</>}
                  </a>
                  {filterArea.length > 0 && (
                    <a
                      onClick={() => {
                        selAreaModal.current?.onInit();
                        setFilterArea([]);
                      }}
                    >
                      <span className="close"></span>
                    </a>
                  )}
                </li>
                <li className="item">
                  <a
                    className={
                      "filterSel" +
                      (filterFish.length > 0 ? " ".concat("active") : "")
                    }
                    data-toggle="modal"
                    data-target="#selFishModal"
                  >
                    어종{filterFish.length > 0 && <>({filterFish.length})</>}
                  </a>
                  {filterFish.length > 0 && (
                    <a
                      onClick={() => {
                        selFishModal.current?.onInit();
                        setFilterFish([]);
                      }}
                    >
                      <span className="close"></span>
                    </a>
                  )}
                </li>
                <li className="item">
                  <a
                    className={
                      "filterSel" +
                      (filterSort !== null ? " ".concat("active") : "")
                    }
                    data-toggle="modal"
                    data-target="#selSortModal"
                  >
                    정렬
                  </a>
                  {filterSort !== null && (
                    <a
                      onClick={() => {
                        selSortModal.current?.onInit();
                        setFilterSort(null);
                      }}
                    >
                      <span className="close"></span>
                    </a>
                  )}
                </li>
                <li className="item">
                  <a
                    className={
                      "filterSel" +
                      (Object.keys(filterOption).length > 0
                        ? " ".concat("active")
                        : "")
                    }
                    data-toggle="modal"
                    data-target="#selOptionModal"
                  >
                    옵션
                  </a>
                  {Object.keys(filterOption).length > 0 && (
                    <a
                      onClick={() => {
                        selOptionModal.current?.onInit();
                        setFilterOption({});
                      }}
                    >
                      <span className="close"></span>
                    </a>
                  )}
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
            {/** Content > 일반 */}
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
            ref={selDateModal}
            id={"selDateModal"}
            onSelected={(date) => setFilterDate(date)}
          />
          <SelectAreaModal
            ref={selAreaModal}
            id={"selAreaModal"}
            onSelected={(data) => setFilterArea(data)}
          />
          <SelectFishModal
            ref={selFishModal}
            id={"selFishModal"}
            onSelected={(data) => setFilterFish(data)}
          />
          <SelectSortModal
            ref={selSortModal}
            id={"selSortModal"}
            onSelected={(data) => setFilterSort(data)}
          />
          <SelectOptionModal
            ref={selOptionModal}
            id={"selOptionModal"}
            onSelected={(data) => setFilterOption(data)}
          />
        </>
      );
    }
  )
);
