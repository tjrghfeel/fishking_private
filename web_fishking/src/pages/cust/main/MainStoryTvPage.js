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
  VIEW: { FilterListItemView, StoryTvLiveListItemView },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
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
          playUrl: "https://www.youtube.com/watch?v=",
          nextPageToken: null,
          isPending: false,
          isEnd: false,
          page: 0,
          size: 10,
          live_list: [], // 어복 Live
          tube_list: [], // 어복 Tube
          fishingDate: null,
          sido: null,
          sigungu: null,
          species: [],
          orderBy: "popular",
          latitude: null,
          longitude: null,
        });
        PageStore.setScrollEvent(() => {
          this.loadPageData(PageStore.state.page + 1);
        });
        if (!restored) this.loadPageData();
      }

      loadPageData = async (page = 0) => {
        const { APIStore, PageStore } = this.props;

        if ((page > 0 && PageStore.state.isEnd) || APIStore.isLoading) return;

        PageStore.setState({ page, isPending: true });

        let district2List = null;
        if (
          PageStore.state.district2List &&
          PageStore.state.district2List.length > 0
        ) {
          district2List = PageStore.state.district2List.join(",");
        }
        const { nextPageToken, playUrl, items: content } = await APIStore._get(
          "/v2/api/tv/list"
        );

        PageStore.setState({ nextPageToken, playUrl });

        if (page === 0) {
          PageStore.setState({ tube_list: content });
          setTimeout(() => {
            window.scrollTo(0, 0);
          }, 100);

          // 어복 Live 추가 조회
          const live = await APIStore._get(`/v2/api/tv/lives/0`);
          console.log("live");
          console.log(JSON.stringify(live));
        } else {
          PageStore.setState({
            tube_list: PageStore.state.tube_list.concat(content),
          });
        }
        if (nextPageToken === null) {
          PageStore.setState({ isEnd: true });
        } else {
          PageStore.setState({ isEnd: false });
        }
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
              onSelected={(selected) => console.log(JSON.stringify(selected))}
            />
            <SelectAreaModal
              id={"selAreaModal"}
              onSelected={(selected) => console.log(JSON.stringify(selected))}
            />
            <SelectFishModal
              id={"selFishModal"}
              onSelected={(selected) => console.log(JSON.stringify(selected))}
            />
            <SelectCompanySortModal
              id={"selSortModal"}
              onSelected={(selected) => console.log(JSON.stringify(selected))}
            />

            <NavigationLayout title={"어복스토리"} showSearchIcon={true} />
            <StoryTab activeIndex={1} />

            {/** Filter */}
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
                      onClickClear: () => {},
                    }}
                  />
                  <FilterListItemView
                    data={{
                      text: "어종",
                      isActive: this.state.selFishActive,
                      modalTarget: "selFishModal",
                      onClickClear: () => {},
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

            {/** 인기영상 */}
            <div className="container nopadding mt-4">
              <div className="slideList slideTv card-md">
                <ul className="listWrap">
                  {(PageStore.state.tube_list || []).length > 0 &&
                    PageStore.state.tube_list.map((data, index) => (
                      <StoryTvLiveListItemView key={index} data={data} />
                    ))}
                </ul>
              </div>
            </div>
            <MainTab activeIndex={3} />
          </React.Fragment>
        );
      }
    }
  )
);
