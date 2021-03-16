import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { MainTab, NavigationLayout, StoryTab },
  MODAL: {
    SelectDateModal,
    SelectAreaModal,
    SelectFishModal,
    SelectCompanySortModal,
  },
  VIEW: {
    FilterListItemView,
    StoryTvLiveListItemView,
    StoryTvTubeListItemView,
  },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.selAreaModal = React.createRef(null);
        this.selFishActive = React.createRef(null);
        this.state = {
          selDateActive: false,
          selAreaActive: false,
          selFishActive: false,
          selSortActive: false,
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const { PageStore, APIStore } = this.props;
        const restored = PageStore.restoreState({
          live_fishingType: "ship", // 어복라이브 검색 - 리스트타입
          live_fishingDate: null, // 어복라이브 검색 -날짜
          live_sido: null, // 어복라이브 검색 - 시도
          live_sigungu: null, // 어복라이브 검색 - 시군구
          live_species: [], // 어복라이브 검색 - 어종
          live_orderBy: "popular", // 어복라이브 검색 - 정렬
          live_list: [], // 어복라이브 - 결과 데이터
          live_latitude: null,
          live_longitude: null,

          tube_isPending: false,
          tube_list: [],
          tube_nextPageToken: null,
        });
        PageStore.setScrollEvent(() => {
          this.loadPageDataForTube(true);
        });
        if (!restored) {
          this.loadPageDataForLive();
          this.loadPageDataForTube(false);
        }
      }
      loadPageDataForLive = async () => {
        const { APIStore, PageStore } = this.props;
        const {
          live_fishingType: fishingType,
          live_fishingDate: fishingDate,
          live_sido: sido,
          live_sigungu: sigungu,
          live_species: species,
          live_orderBy: orderBy,
          live_latitude: latitude,
          live_longitude: longitude,
        } = PageStore.state;
        const live = await APIStore._get(`/v2/api/tv/lives/0`, {
          fishingType,
          fishingDate,
          sido,
          sigungu,
          species,
          orderBy,
          latitude,
          longitude,
        });
        PageStore.setState({ live_list: live.content || [] });
        PageStore.reloadSwipe();
      };
      loadPageDataForTube = async (nextPage = false) => {
        const { APIStore, PageStore } = this.props;

        if (
          (nextPage && PageStore.state.tube_nextPageToken === null) ||
          PageStore.state.tube_isPending
        )
          return;

        PageStore.setState({ tube_isPending: true });
        const {
          items: content = [],
          nextPageToken = null,
          playUrl = null,
        } = await APIStore._get(`/v2/api/tv/list`, {
          nextPageToken: nextPage ? PageStore.state.tube_nextPageToken : null,
        });

        if (!nextPage) {
          // 첫 페이지
          PageStore.setState({ tube_list: content });
          setTimeout(() => {
            window.scrollTo(0, 0);
          }, 100);
        } else {
          // 추가 페이지
          PageStore.setState({
            tube_list: PageStore.state.tube_list.concat(content),
          });
        }
        PageStore.setState({
          tube_isPending: false,
          tube_nextPageToken: nextPageToken,
        });
      };
      onClickLive = async (item) => {
        const { PageStore } = this.props;
        PageStore.push(`/story/tv/${item.id}/${item.cameraId}`);
      };
      onClickTube = async (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        PageStore.push(
          `/story/tv/${item.id}?data=${JSON.stringify(item).encrypt()}`
        );
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <SelectDateModal
              id={"selDateModal"}
              onSelected={async (selected) => {
                PageStore.setState({ live_fishingDate: selected.format("-") });
                this.loadPageDataForLive();
              }}
            />
            <SelectAreaModal
              ref={this.selAreaModal}
              id={"selAreaModal"}
              onSelected={({ selectedLv1, selectedLv2 }) => {
                if (selectedLv1 !== null) {
                  PageStore.setState({ live_sido: selectedLv1["code"] });
                } else {
                  PageStore.setState({ live_sido: null });
                }
                if (selectedLv2 !== null) {
                  PageStore.setState({ live_sigungu: selectedLv2["code"] });
                } else {
                  PageStore.setState({ live_sigungu: null });
                }
                if (selectedLv1 == null && selectedLv2 == null) {
                  this.setState({ selAreaActive: false });
                } else {
                  this.setState({ selAreaActive: true });
                }
                this.loadPageDataForLive();
              }}
            />
            <SelectFishModal
              ref={this.selFishModal}
              id={"selFishModal"}
              onSelected={(selected) => {
                let live_species = [];
                for (let item of selected) {
                  live_species.push(item["code"]);
                }
                if (live_species.length === 0) {
                  this.setState({ selFishActive: false });
                } else {
                  this.setState({ selFishActive: true });
                }
                PageStore.setState({ live_species });
                this.loadPageDataForLive();
              }}
            />
            <SelectCompanySortModal
              id={"selSortModal"}
              onSelected={(selected) => {
                PageStore.setState({ live_orderBy: selected.value });
                this.loadPageDataForLive();
              }}
            />

            <NavigationLayout title={"어복스토리"} showSearchIcon={true} />
            <StoryTab activeIndex={1} />

            {/** 어복라이브 > Filter */}
            <div className="filterWrap">
              <div className="slideList">
                <h6 className="float-left mt-2">어복 Live</h6>
                <ul className="listWrap float-right">
                  <FilterListItemView
                    data={{
                      text: "날짜",
                      isActive: this.state.selDateActive,
                      modalTarget: "selDateModal",
                      onClickClear: () => {},
                    }}
                  />
                  <FilterListItemView
                    data={{
                      text: "지역",
                      isActive: this.state.selAreaActive,
                      modalTarget: "selAreaModal",
                      onClickClear: () => {
                        this.selAreaModal.current?.onInit();
                        this.setState({ selAreaActive: false });
                        PageStore.setState({
                          live_sido: null,
                          live_sigungu: null,
                        });
                        this.loadPageDataForLive();
                      },
                    }}
                  />
                  <FilterListItemView
                    data={{
                      text: "어종",
                      isActive: this.state.selFishActive,
                      modalTarget: "selFishModal",
                      onClickClear: () => {
                        this.selFishModal.current?.onInit();
                        this.setState({ selFishActive: false });
                        PageStore.setState({
                          live_species: [],
                        });
                        this.loadPageDataForLive();
                      },
                    }}
                  />
                  <FilterListItemView
                    data={{
                      text: "정렬",
                      isActive: this.state.selSortActive,
                      modalTarget: "selSortModal",
                      onClickClear: () => {},
                    }}
                  />
                </ul>
              </div>
            </div>

            {/** 어복라이브 */}
            <div className="container nopadding mt-4">
              <div className="slideList slideTv card-md">
                <ul className="listWrap">
                  {(PageStore.state.live_list || []).length > 0 &&
                    PageStore.state.live_list.map((data, index) => (
                      <StoryTvLiveListItemView
                        key={index}
                        data={data}
                        onClick={this.onClickLive}
                      />
                    ))}
                  {(PageStore.state.live_list || []).length > 0 && (
                    <li className="item more">
                      <a
                        onClick={() => PageStore.push(`/main/company/boat`)}
                        className="moreLink"
                      >
                        <div className="inner inner-md">
                          <span>더보기</span>
                        </div>
                      </a>
                    </li>
                  )}
                </ul>
              </div>
            </div>

            {/** 어복튜브 > Filter */}
            <hr className="dark mb-0" />
            <div className="filterWrap">
              <h6 className="float-left mt-2">어복 Tube</h6>
              <div className="float-right">
                <div className="custom-control custom-radio custom-control-inline">
                  <input
                    type="radio"
                    id="customRadioInline1"
                    name="customRadioInline1"
                    className="custom-control-input"
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
                  />
                  <label
                    className="custom-control-label"
                    htmlFor="customRadioInline2"
                  >
                    인기순
                  </label>
                </div>
              </div>
            </div>

            {(PageStore.state.tube_list || []).length > 0 &&
              PageStore.state.tube_list.map((data, index) => (
                <StoryTvTubeListItemView
                  key={index}
                  data={data}
                  onClick={this.onClickTube}
                />
              ))}
            <MainTab activeIndex={3} />
          </React.Fragment>
        );
      }
    }
  )
);
