/* global daum */
import React, { useRef, useState, useCallback, useEffect } from "react";
import { inject, observer } from "mobx-react";
import SelectCompanyAndLocationModal from "./SelectCompanyAndLocationModal";
import NativeStore from "../../stores/NativeStore";

export default inject(
  "DOMStore",
  "NativeStore"
)(
  observer(({ DOMStore, NativeStore, id, onSelected }) => {
    const [tab, setTab] = useState(1);

    const [mapInstance, setMapInstance] = useState(null);
    const [isLoadMap, setIsLoadMap] = useState(false);
    const onChangeTab = useCallback(
      (index) => {
        setTab(index);

        if (index === 2) {
          let map = null;
          if (mapInstance === null) {
            const container = document.querySelector("#map");
            const options = {
              center: new daum.maps.LatLng(36.252932, 127.724734),
              level: 7,
            };
            map = new daum.maps.Map(container, options);
            setMapInstance(map);
          }
          setTimeout(() => {
            (mapInstance || map).relayout();
          }, 0);

          NativeStore.getCurrentPosition();
        }
      },
      [
        setTab,
        isLoadMap,
        setIsLoadMap,
        mapInstance,
        setMapInstance,
        NativeStore,
      ]
    );

    const inputSearchKeyword = useRef(null);
    const [page, setPage] = useState(0);
    const [isPending, setIsPending] = useState(false);
    const [isEnd, setIsEnd] = useState(false);
    const [searchKeyword, setSearchKeyword] = useState("");
    const [searchFilter, setSearchFilter] = useState("distance");
    const [searchResult, setSearchResult] = useState([]);
    const searchByKeyword = useCallback(
      async (page = 0, sort = "distance") => {
        const keyword = inputSearchKeyword.current?.value;
        if (keyword === "") return;

        if (page > 0 && (isPending || isEnd)) return;
        else if (page === 0) setIsEnd(false);

        setIsPending(true);
        setPage(page);
        setSearchFilter(sort);
        setSearchKeyword(keyword);

        // TODO : 업체 검색 요청
      },
      [isPending, setIsPending, isEnd, setIsEnd, setPage]
    );
    const select = useCallback((item) => {
      // TODO : 선택 데이터 가공
      onSelected(item);
    });
    return (
      // TODO : 코드목록 'sortType' 에 '명칭순' 데이터가 필요합니다.
      <div
        className="modal fade modal-full"
        id={id}
        tabIndex="-1"
        aria-labelledby={id.concat("Label")}
        aria-hidden="true"
      >
        <div className="modal-dialog">
          <div className="modal-content">
            <div className="modal-header bg-primary d-flex justify-content-center">
              <a data-dismiss="modal" className="nav-left">
                <img src="/assets/img/svg/navbar-back.svg" alt="뒤로가기" />
              </a>
              <h5 className="modal-title" id={id.concat("Label")}>
                업체&위치등록
              </h5>
            </div>
            <div className="modal-body">
              {/** 탭메뉴 */}
              <nav className="nav nav-pills nav-menu nav-justified">
                <a
                  className={"nav-link" + (tab === 1 ? " active" : "")}
                  onClick={() => onChangeTab(1)}
                >
                  업체
                </a>
                <a
                  className={"nav-link" + (tab === 2 ? " active" : "")}
                  onClick={() => onChangeTab(2)}
                >
                  내 위치 선택
                </a>
              </nav>

              {/** 내 위치 선택*/}
              <div
                className="container nopadding"
                style={{
                  display: tab === 1 ? "none" : null,
                }}
              >
                <div className="mapwrap-sm">
                  <div className="info-btm-round pt-2">
                    <div className="row no-gutters d-flex align-items-center pt-0">
                      <div className="col-7 text-center">
                        전라남도 진도군 임회면 굴포리 144-7
                      </div>
                      <div className="col-4 text-right">
                        <a className="btn btn-primary btn-sm-nav-tab">
                          내 위치 선택하기
                        </a>
                      </div>
                    </div>
                  </div>
                  <div id="map">
                    <a
                      className="pointer img-sm"
                      style={{ top: "310px", left: "225px" }}
                    >
                      <img src="/assets/img/svg/marker-boat-white.svg" />
                    </a>
                  </div>
                </div>
              </div>

              {/** 업체 > 컨텐츠 */}
              {tab === 1 && (
                <React.Fragment>
                  {/** 검색 */}
                  <div className="container nopadding mt-3 mb-0">
                    <form className="form-search">
                      <a onClick={searchByKeyword}>
                        <img
                          src="/assets/img/svg/form-search.svg"
                          alt=""
                          className="icon-search"
                        />
                      </a>
                      <input
                        ref={inputSearchKeyword}
                        className="form-control mr-sm-2"
                        type="search"
                        placeholder="업체명 또는 키워드로 검색하세요."
                        aria-label="Search"
                        onKeyDown={(e) => {
                          if (e.keyCode === 13) {
                            searchByKeyword();
                            e.preventDefault();
                          }
                        }}
                      />
                    </form>
                  </div>

                  {/** Filter */}
                  <div className="container nopadding mt-3 mb-0">
                    <div className="row no-gutters d-flex align-items-center">
                      <div className="col-6">
                        <p className="mt-2 pl-2">
                          {searchKeyword !== "" && (
                            <React.Fragment>
                              ‘{searchKeyword}’ 검색결과{" "}
                              <strong className="text-primary">71건</strong>
                            </React.Fragment>
                          )}
                        </p>
                      </div>
                      <div className="col-6 text-right">
                        <div className="custom-control custom-radio custom-control-inline">
                          <input
                            type="radio"
                            id="customRadioInline1"
                            name="customRadioInline1"
                            className="custom-control-input"
                            defaultChecked={searchFilter === "distance"}
                            onClick={() => searchByKeyword(0, "distance")}
                          />
                          <label
                            className="custom-control-label"
                            htmlFor="customRadioInline1"
                          >
                            거리순
                          </label>
                        </div>
                        <div className="custom-control custom-radio custom-control-inline">
                          <input
                            type="radio"
                            id="customRadioInline2"
                            name="customRadioInline1"
                            className="custom-control-input"
                            defaultChecked={searchFilter === "distance2"}
                            onClick={() => searchByKeyword(0, "distance2")}
                          />
                          <label
                            className="custom-control-label"
                            htmlFor="customRadioInline2"
                          >
                            명칭순
                          </label>
                        </div>
                      </div>
                    </div>
                  </div>

                  {/** 업체 리스트 */}
                  <div className="container nopadding mt-3 mb-0">
                    {searchResult.length > 0 &&
                      searchResult.map((data, index) => (
                        <SelectCompanyAndLocationModal
                          key={index}
                          data={data}
                          onClick={(data) => select(data)}
                        />
                      ))}
                  </div>
                </React.Fragment>
              )}
            </div>
          </div>
        </div>
      </div>
    );
  })
);
