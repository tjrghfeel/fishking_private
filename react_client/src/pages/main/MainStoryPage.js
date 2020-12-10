import React, { useRef, useState } from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";
import StoryTabs from "../../components/layout/StoryTabs";
import ScrollList02 from "../../components/list/ScrollList02";
import SelectAreaModal from "../../components/modal/SelectAreaModal";
import SelectFishModal from "../../components/modal/SelectFishModal";
import SelectStorySortModal from "../../components/modal/SelectStorySortModal";

export default inject()(
  observer(() => {
    const [filterArea, setFilterArea] = useState([]); // 필터::지역
    const [filterFish, setFilterFish] = useState([]); // 필터::어종
    const [filterSort, setFilterSort] = useState(null); // 필터::정렬
    const selAreaModal = useRef(null);
    const selFishModal = useRef(null);
    const selSortModal = useRef(null);
    return (
      <>
        {/** Navigation */}
        <Navigation tit={"어복스토리"} visibleSearchIcon={true} />

        {/** 탭메뉴 */}
        <StoryTabs />

        {/** Filter */}
        <div className="filterWrap">
          <div className="slideList">
            <ul className="listWrap">
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
            </ul>
          </div>
        </div>

        {/** List */}
        <ScrollList02
          list={[
            {
              profileImage: "/assets/img/sample/boat1.jpg",
              membername: "어복황제3호",
              location: "경북 포항시 남구",
              regDate: "17분전",
              title: "전남 진도 어복황제3호 8월  17일 오전시간 배조황!!",
              contents:
                "태풍바비가 오기전에 수온이 25도 였는데, 태풍지나가면서 수온이 27.5도…남쪽에서 난류도 같이",
              images: [
                { imgSrc: "/assets/img/sample/photo5.jpg" },
                { imgSrc: "/assets/img/sample/photo3.jpg" },
                { imgSrc: "/assets/img/sample/photo6.jpg" },
              ],
              likeCount: 1,
              commentCount: 10,
              scrapCount: 20,
              isLike: true,
              isScrap: false,
            },
          ]}
        />

        {/** 모달 팝업 */}
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
        <SelectStorySortModal
          ref={selSortModal}
          id={"selSortModal"}
          onSelected={(data) => setFilterSort(data)}
        />
      </>
    );
  })
);
