/* eslint-disable jsx-a11y/anchor-is-valid */
import React, { useEffect } from "react";
import Navigation from "../../components/layouts/Navigation";
import { inject, observer } from "mobx-react";

const BoatPage = inject("ModalStore")(
  observer(({ ModalStore }) => {
    useEffect(() => {
      console.log("effect");
    });
    /** 날짜 팝업 열기 */
    const openSelectDate = () => {
      ModalStore.open({
        id: "selDateModal",
        onSelect: (selectedDate) => {
          console.log(selectedDate);
        },
      });
    };
    return (
      <>
        <Navigation title={"바다낚시"} hasSearch={true} />

        {/** 필터 */}
        <div className="filterWrap">
          <div className="slideList">
            <ul className="listWrap">
              <li className="item">
                <a href="#none" className="filterLive">
                  <span className="sr-only">라이브</span>
                </a>
              </li>
              <li className="item">
                <a onClick={() => openSelectDate()} className="filterSel">
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
      </>
    );
  })
);

export default BoatPage;
