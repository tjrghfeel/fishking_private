import React, { useState } from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";
import SearchMainTabs from "../../components/layout/SearchMainTabs";
import SelectDateModal from "../../components/modal/SelectDateModal";

export default inject()(
  observer(() => {
    const [reserveType, setReserveType] = useState("boat");
    return (
      <>
        {/** Navigation */}
        <Navigation title={"검색"} visibleBackIcon={true} />

        {/** 탭메뉴 */}
        <SearchMainTabs />

        {/** 예약 */}
        <div className="container nopadding mt-3">
          <div className="card-round-grey">
            <span className="status status2">예약대기</span>
            <h6 className="text-center">
              <img
                src="/assets/img/svg/form-search.svg"
                alt=""
                className="vam"
              />{" "}
              낚시 예약 검색
            </h6>
            <p className="mt-3">낚시 어디로 가세요?</p>
            <div className="row no-gutters">
              <div className="col-6 padding-sm">
                <a
                  onClick={() => setReserveType("boat")}
                  className={
                    "btn btn-block btn-sm mt-1 mb-1" +
                    (reserveType === "boat"
                      ? " ".concat("btn-info")
                      : " btn-third")
                  }
                >
                  선상 예약
                </a>
              </div>
              <div className="col-6 padding-sm">
                <a
                  onClick={() => setReserveType("rock")}
                  className={
                    "btn btn-block btn-sm mt-1 mb-1" +
                    (reserveType === "rock"
                      ? " ".concat("btn-info")
                      : " btn-third")
                  }
                >
                  갯바위 예약
                </a>
              </div>
            </div>
            <p className="mt-3">언제 가시나요?</p>
            <div className="row no-gutters">
              <div className="col-12 padding-sm">
                <a
                  href="#none"
                  className="btn btn-third btn-block btn-sm mt-1 mb-1"
                  data-toggle="modal"
                  data-target="#selDateModal"
                >
                  <img
                    src="/assets/img/svg/icon-cal.svg"
                    alt=""
                    className="float-left"
                  />
                  2020년 8월 17일 (월)
                </a>
              </div>
            </div>
            <hr className="mt-2 mb-3" />
            <a
              href="boat.html"
              className="btn btn-primary btn-block btn-sm mt-1 mb-3"
            >
              예약 검색
            </a>
          </div>
        </div>

        {/** 모달 팝업 */}
        <SelectDateModal
          id={"selDateModal"}
          onSelected={(date) => console.log(date)}
        />
      </>
    );
  })
);
