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
  "APIStore",
  "NativeStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.selDateModal = React.createRef(null);
        this.selAreaModal = React.createRef(null);
        this.selFishModal = React.createRef(null);
        this.selSortModal = React.createRef(null);
        this.selOptionModal = React.createRef(null);
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
      async componentDidMount() {
        const {
          PageStore,
          APIStore,
          NativeStore,
          match: {
            params: { fishingType },
          },
        } = this.props;
        const qp = PageStore.getQueryParams();
        let fishingDate = null;
        if ((qp.fishingDate || null) !== null) {
          fishingDate = new Date(qp.fishingDate).format("-");
        }
        let species = null;
        if ((qp.species || null) !== null) {
          species = qp.species.split("__");
          this.setState({ filterFishActive: true });
          this.selFishModal.current.onInit(species);
        }
        let hasRealTimeVideo = "";
        if ((qp.hasRealTimeVideo || null) !== null) {
          hasRealTimeVideo = qp.hasRealTimeVideo;
          if (qp.hasRealTimeVideo === "true")
            this.setState({ filterLiveActive: true });
        }
        let type = "";
        if (fishingType == "boat") type = "ship";
        else if (fishingType == "rock") type = "seaRocks";
        type = "ship";

        const { lat, lng } = await NativeStore.getCurrentPosition();
        const restored = PageStore.restoreState({
          isPending: false,
          isEnd: false,
          premium: [],
          normal: [],
          list: [],
          fishingType: type,
          page: 0,
          size: 20,
          hasRealTimeVideo,
          fishingDate: fishingDate,
          sido: null,
          sigungu: null,
          species,
          orderBy: "popular",
          facilities: null,
          genres: null,
          services: null,
          latitude: lat,
          longitude: lng,
        });
        PageStore.setScrollEvent(() => {
          this.loadPageData(PageStore.state.page + 1);
        });
        if (!restored) this.loadPageData();
        PageStore.reloadSwipe();
      }
      componentWillUnmount() {
        const { PageStore } = this.props;
        PageStore.removeScrollEvent();
      }
      loadPageData = async (page = 0) => {
        const { APIStore, PageStore } = this.props;

        if ((page > 0 && PageStore.state.isEnd) || PageStore.state.isPending)
          return;

        PageStore.setState({ page, isPending: true });

        const resolve = await APIStore._get(`/v2/api/ships/list/${page}`, {
          fishingType: PageStore.state.fishingType,
          hasRealTimeVideo: PageStore.state.hasRealTimeVideo,
          fishingDate: PageStore.state.fishingDate,
          sido: PageStore.state.sido,
          sigungu: PageStore.state.sigungu,
          species: PageStore.state.species,
          orderBy: PageStore.state.orderBy,
          facilities: PageStore.state.facilities,
          genres: PageStore.state.genres,
          services: PageStore.state.services,
          latitude: PageStore.state.latitude,
          longitude: PageStore.state.longitude,
        });
        const { ad, list } = resolve;
        const { normal = [], premium = [] } = ad || {};
        const {
          content = [],
          pageable: { pageSize = 0 },
        } = list || {};

        if (page === 0) {
          PageStore.setState({ list: content, premium, normal });
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
        PageStore.setState({ isPending: false });
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
        console.log("A");
        const {
          PageStore,
          match: {
            params: { fishingType },
          },
        } = this.props;
        PageStore.storeState();
        if (text === "통합가이드") {
          PageStore.push(`/guide/main`);
        } else if (text === "지도보기") {
          PageStore.push(`/common/mapsearch?fishingType=${fishingType}`);
        } else if (text === "예약검색") {
          PageStore.push(`/search/reserve`);
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
              ref={this.selDateModal}
              id={"selDateModal"}
              onSelected={(selected) => {
                PageStore.setState({ fishingDate: selected.format() });
                this.setState({ filterDateActive: true });
                this.loadPageData(0);
              }}
            />
            <SelectAreaModal
              ref={this.selAreaModal}
              id={"selAreaModal"}
              onSelected={({ selectedLv1, selectedLv2 }) => {
                if (selectedLv1 == null) {
                  PageStore.setState({ sido: null, sigungu: null });
                  this.setState({ filterAreaActive: false });
                } else {
                  PageStore.setState({
                    sido: selectedLv1?.code || null,
                    sigungu: selectedLv2?.code || null,
                  });
                  this.setState({ filterAreaActive: true });
                }
                this.loadPageData(0);
              }}
            />
            <SelectFishModal
              ref={this.selFishModal}
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
              ref={this.selSortModal}
              id={"selSortModal"}
              onSelected={(selected) => {
                PageStore.setState({ orderBy: selected.value });
                this.loadPageData(0);
              }}
            />
            <SelectCompanyOptionModal
              ref={this.selOptionModal}
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

            <NavigationLayout
              title={
                PageStore.state?.fishingType === "ship"
                  ? "선상낚시"
                  : PageStore.state?.fishingType === "seaRocks"
                  ? "갯바위낚시"
                  : ""
              }
              showSearchIcon={true}
            />

            <FilterListView
              list={[
                {
                  isLiveButton: true,
                  isActive: this.state.filterLiveActive,
                  onClick: (selected) => {
                    this.setState({ filterLiveActive: !selected });
                    PageStore.setState({
                      hasRealTimeVideo: selected ? "" : "true",
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
                    this.selDateModal.current?.onInit();
                    this.loadPageData(0);
                  },
                },
                {
                  text: this.state.filterAreaText,
                  isActive: this.state.filterAreaActive,
                  modalTarget: "selAreaModal",
                  onClickClear: () => {
                    this.setState({ filterAreaActive: false });
                    PageStore.setState({ sido: null, sigungu: null });
                    this.selAreaModal.current?.onInit();
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
                    this.selFishModal.current?.onInit();
                    this.loadPageData(0);
                  },
                },
                {
                  text: this.state.filterSortText,
                  isActive: this.state.filterSortActive,
                  modalTarget: "selSortModal",
                  onClickClear: () => {
                    PageStore.setState({ orderBy: "popular" });
                    this.selFishModal.current?.onInit();
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
                    this.selOptionModal.current?.onInit();
                    this.loadPageData(0);
                  },
                },
              ]}
            />

            {/** Content */}
            <div className="container nopadding">
              {PageStore.state.premium && PageStore.state.premium.length > 0 && (
                <React.Fragment>
                  <p className="clearfix"></p>
                  <h6 className="text-secondary">인기 프리미엄 AD</h6>
                  {/** 인기 프리미엄 AD */}
                  {PageStore.state.premium &&
                    PageStore.state.premium.map((data, index) => (
                      <CompanyPremiumListItemView
                        key={index}
                        data={data}
                        onClick={this.onClick}
                      />
                    ))}
                </React.Fragment>
              )}
              {PageStore.state.normal && PageStore.state.normal.length > 0 && (
                <React.Fragment>
                  <p className="clearfix"></p>
                  <h6 className="text-secondary">프리미엄 AD</h6>
                  {/** 프리미엄 AD */}
                  {PageStore.state.normal &&
                    PageStore.state.normal.map((data, index) => (
                      <CompanyPremiumListItemView
                        key={index}
                        data={data}
                        onClick={this.onClick}
                      />
                    ))}
                </React.Fragment>
              )}

              {PageStore.state.list && PageStore.state.list.length > 0 && (
                <React.Fragment>
                  <p className="clearfix"></p>
                  <h6 className="text-secondary mb-3">일반</h6>
                  {PageStore.state.list &&
                    PageStore.state.list.map((data, index) => (
                      <CompanyListItemView
                        key={index}
                        data={data}
                        onClick={this.onClick}
                      />
                    ))}
                </React.Fragment>
              )}
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

            <MainTab activeIndex={1} />
          </React.Fragment>
        );
      }
    }
  )
);
