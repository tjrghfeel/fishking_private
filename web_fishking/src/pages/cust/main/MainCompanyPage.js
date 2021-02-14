import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { MainTab, NavigationLayout },
  VIEW: {
    FilterListView,
    CompanyPremiumListItemView,
    CompanyListItemView,
    FABView,
  },
  MODAL: {
    SelectDateModal,
    SelectAreaModal,
    SelectFishModal,
    SelectCompanySortModal,
    SelectCompanyOptionModal,
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
        const {
          PageStore,
          match: {
            params: { fishingType },
          },
        } = this.props;
        let type = "";
        if (fishingType == "boat") type = "ship";
        else if (fishingType == "rock") type = "seaRocks";
        const restored = PageStore.restoreState({
          isPending: false,
          isEnd: false,
          list: [],
          fishingType: type,
          page: 0,
          size: 20,
          hasRealTimeVideo: "",
          fishingDate: null,
          sido: null,
          species: null,
          orderBy: "popular",
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
      onClick = async (item) => {
        const {
          PageStore,
          match: {
            params: { fishingType },
          },
        } = this.props;
        PageStore.storeState();
        PageStore.push(`/company/${fishingType}/detail/${item.id}`);
      };
      onClickFAB = async (text) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        if (text === "통합가이드") {
          PageStore.push(`/guide/main`);
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const {
          PageStore,
          match: {
            params: { fishingType },
          },
        } = this.props;
        return (
          <React.Fragment>
            <SelectDateModal
              id={"selDateModal"}
              onSelected={(selected) => {
                PageStore.setState({ fishingDate: selected.format() });
                this.setState({ filterDateActive: true });
                this.loadPageData(0);
              }}
            />
            <SelectAreaModal
              id={"selAreaModal"}
              onSelected={(selected) => {
                if (selected == null) {
                  PageStore.setState({ sido: null });
                  this.setState({ filterAreaActive: false });
                } else {
                  PageStore.setState({ sido: selected.code });
                  this.setState({ filterAreaActive: true });
                }
                this.loadPageData(0);
              }}
            />
            <SelectFishModal
              id={"selFishModal"}
              onSelected={(selected) => {
                if (selected == null || selected.length == 0) {
                  PageStore.setState({ species: null });
                  this.setState({ filterFishActive: false });
                } else {
                  const species = [];
                  for (let item of selected) {
                    species.push(item.code);
                  }
                  PageStore.setState({ species });
                  this.setState({ filterFishActive: true });
                }
                this.loadPageData(0);
              }}
            />
            <SelectCompanySortModal
              id={"selSortModal"}
              onSelected={(selected) => {
                PageStore.setState({ orderBy: selected.value });
                this.loadPageData(0);
              }}
            />
            <SelectCompanyOptionModal
              id={"selOptionModal"}
              onSelected={(selectedService, selectedFacility) => {
                if (
                  (selectedService !== null && selectedService.length > 0) ||
                  (selectedFacility !== null && selectedFacility.length > 0)
                ) {
                  this.setState({ filterOptionActive: true });
                } else {
                  this.setState({ filterOptionActive: false });
                }

                if (selectedService === null || selectedService.length === 0) {
                  PageStore.setState({ sevices: null });
                } else {
                  const services = [];
                  for (let item of selectedService) {
                    services.push(item.code);
                  }
                  PageStore.setState({ services });
                }
                if (
                  selectedFacility === null ||
                  selectedFacility.length === 0
                ) {
                  PageStore.setState({ facilities: null });
                } else {
                  const facilities = [];
                  for (let item of selectedFacility) {
                    facilities.push(item.code);
                  }
                  PageStore.setState({ facilities });
                }
                this.loadPageData(0);
              }}
            />

            <NavigationLayout title={"바다낚시"} showSearchIcon={true} />

            <FilterListView
              list={[
                {
                  isLiveButton: true,
                  isActive: this.state.filterLiveActive,
                  onClick: (selected) => {
                    this.setState({ filterLiveActive: !selected });
                    PageStore.setState({
                      hasRealTimeVideo: selected ? "" : "1",
                    });
                    this.loadPageData(0);
                  },
                },
                {
                  text: this.state.filterDateText,
                  isActive: this.state.filterDateActive,
                  modalTarget: "selDateModal",
                  onClickClear: () => {
                    this.setState({ filterDateActive: false });
                    PageStore.setState({ fishingDate: null });
                    this.loadPageData(0);
                  },
                },
                {
                  text: this.state.filterAreaText,
                  isActive: this.state.filterAreaActive,
                  modalTarget: "selAreaModal",
                  onClickClear: () => {
                    this.setState({ filterAreaActive: false });
                    PageStore.setState({ sido: null });
                    this.loadPageData(0);
                  },
                },
                {
                  text: this.state.filterFishText,
                  isActive: this.state.filterFishActive,
                  modalTarget: "selFishModal",
                  onClickClear: () => {
                    this.setState({ filterFishActive: false });
                    PageStore.setState({ species: null });
                    this.loadPageData(0);
                  },
                },
                {
                  text: this.state.filterSortText,
                  isActive: this.state.filterSortActive,
                  modalTarget: "selSortModal",
                  onClickClear: () => {
                    PageStore.setState({ orderBy: "popular" });
                    this.loadPageData(0);
                  },
                },
                {
                  text: this.state.filterOptionText,
                  isActive: this.state.filterOptionActive,
                  modalTarget: "selOptionModal",
                  onClickClear: () => {
                    this.setState({ filterOptionActive: false });
                    PageStore.setState({ services: null, facilities: null });
                    this.loadPageData(0);
                  },
                },
              ]}
            />

            {/** Content */}
            <div className="container nopadding">
              {/*<CompanyPremiumListItemView />*/}
              <p className="clearfix"></p>
              {/*<h6 className="text-secondary mb-3">일반</h6>*/}
              {PageStore.state.list &&
                PageStore.state.list.map((data, index) => (
                  <CompanyListItemView
                    key={index}
                    data={data}
                    onClick={this.onClick}
                  />
                ))}
            </div>

            {/** Toggle Menu */}
            <FABView
              list={[
                {
                  text: "통합가이드",
                  icon: "/assets/cust/img/svg/allmenu-guide.svg",
                },
                {
                  text: "지도보기",
                  icon: "/assets/cust/img/svg/allmenu-map.svg",
                },
                {
                  text: "예약검색",
                  icon: "/assets/cust/img/svg/allmenu-reserv.svg",
                },
              ]}
              onClick={this.onClickFAB}
            />

            <MainTab activeIndex={fishingType == "boat" ? 1 : 2} />
          </React.Fragment>
        );
      }
    }
  )
);
