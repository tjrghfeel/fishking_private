import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";
import Clearfix from "../../components/layout/Clearfix";

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
          <h6 className="text-secondary">인기 프리미엄 AD</h6>
        </div>
      </>
    );
  })
);
