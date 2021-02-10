import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { MainTab, NavigationLayout },
  VIEW: { FilterListView, CompanyPremiumListItemView, CompanyListItemView },
  MODAL: {
    SelectDateModal,
    SelectAreaModal,
    SelectFishModal,
    SelectCompanySortModal,
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
        this.state = {
          filterLiveActive: false,
          filterDateText: "날짜",
          filterAreaText: "지역",
          filterFishText: "어종",
          filterSortText: "정렬",
          filterOptionText: "옵션",
          filterDateActive: false,
          filterAreaActive: false,
          filterFishActive: false,
          filterSortActive: false,
          filterOptionActive: false,
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const { PageStore } = this.props;
        const restored = PageStore.restoreState({
          isPending: false,
          isEnd: false,
          list: [],
          fishingType: "ship",
          page: 0,
          size: 20,
          hasRealTimeVideo: null,
          fishingDate: null,
          sido: null,
          species: ["melfish", "afish"],
          orderBy: null,
          facilities: null,
          genres: null,
          services: null,
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

        console.log(
          JSON.stringify({
            fishingType: PageStore.state.fishingType,
            hasRealTimeVideo: PageStore.state.hasRealTimeVideo,
            fishingDate: PageStore.state.fishingDate,
            sido: PageStore.state.sido,
            species: PageStore.state.species,
            orderBy: PageStore.state.orderBy,
            facilities: PageStore.state.facilities,
            genres: PageStore.state.genres,
            services: PageStore.state.services,
          })
        );
        const {
          content,
          pageable: { pageSize = 0 },
        } = await APIStore._get(`/v2/api/ships/${page}`, {
          fishingType: PageStore.state.fishingType,
          hasRealTimeVideo: PageStore.state.hasRealTimeVideo,
          fishingDate: PageStore.state.fishingDate,
          sido: PageStore.state.sido,
          species: PageStore.state.species,
          orderBy: PageStore.state.orderBy,
          facilities: PageStore.state.facilities,
          genres: PageStore.state.genres,
          services: PageStore.state.services,
        });

        console.log(JSON.stringify(content));

        if (page === 0) {
          PageStore.setState({ list: content });
          setTimeout(() => {
            window.scrollTo(0, 0);
          }, 100);
        } else {
          PageStore.setState({ list: PageStore.state.list.concat(content) });
        }
        if (content.length < pageSize) {
          PageStore.setState({ isEnd: true });
        } else {
          PageStore.setState({ isEnd: false });
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <SelectDateModal
              id={"selDateModal"}
              onSelected={(selected) => console.log(selected)}
            />
            <SelectAreaModal
              id={"selAreaModal"}
              onSelected={(selected) => console.log(selected)}
            />
            <SelectFishModal
              id={"selFishModal"}
              onSelected={(selected) => console.log(selected)}
            />
            <SelectCompanySortModal
              id={"selSortModal"}
              onSelected={(selected) => console.log(selected)}
            />

            <NavigationLayout title={"바다낚시"} showSearchIcon={true} />

            <FilterListView
              list={[
                {
                  isLiveButton: true,
                  isActive: this.state.filterLiveActive,
                  onClick: (selected) => console.log(selected),
                },
                {
                  text: this.state.filterDateText,
                  isActive: this.state.filterDateActive,
                  modalTarget: "selDateModal",
                },
                {
                  text: this.state.filterAreaText,
                  isActive: this.state.filterAreaActive,
                  modalTarget: "selAreaModal",
                },
                {
                  text: this.state.filterFishText,
                  isActive: this.state.filterFishActive,
                  modalTarget: "selFishModal",
                },
                {
                  text: this.state.filterSortText,
                  isActive: this.state.filterSortActive,
                  modalTarget: "selSortModal",
                },
                {
                  text: this.state.filterOptionText,
                  isActive: this.state.filterOptionActive,
                },
              ]}
            />

            {/** Content */}
            <div className="container nopadding">
              <CompanyPremiumListItemView />
              <p className="clearfix"></p>
              <h6 className="text-secondary mb-3">일반</h6>
              <CompanyListItemView />
            </div>

            <MainTab activeIndex={1} />
          </React.Fragment>
        );
      }
    }
  )
);
