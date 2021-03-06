import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { SearchNavigationLayout, SearchTab },
  VIEW: { SearchCompanyListItem01View, SearchStoryListItemView },
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
        this.state = {
          keyword: null,
          tab: null,
          sort: null,
          activeIndex: 0,
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const { match } = this.props;
        this.initPageData(match.params.tab);
      }
      componentWillUnmount() {
        const { PageStore } = this.props;
        PageStore.setState({});
        PageStore.removeScrollEvent();
      }
      initPageData = async (tab = "ship") => {
        const {
          PageStore,
          NativeStore,
          // match: {
          //   params: { tab = "ship" },
          // },
        } = this.props;
        const qp = PageStore.getQueryParams();
        // const keyword = PageStore.state.keyword || qp.keyword || null;
        const keyword = qp.keyword || null;
        let activeIndex = 0;
        if (tab === "ship") activeIndex = 1;
        else if (tab === "diary") activeIndex = 2;
        else if (tab === "live") activeIndex = 3;
        else if (tab === "blog") activeIndex = 4;
        this.setState({ keyword, tab, activeIndex });
        let order = "";
        if (tab === "ship" || tab === "live") order = "distance";
        else order = "createDate";
        let type = qp.type || null;
        const { lat, lng } = await NativeStore.getCurrentPosition();
        const restored = PageStore.restoreState({
          isPending: false,
          isEnd: false,
          totalElements: 0,
          list: [],
          keyword,
          page: 0,
          order,
          type,
          latitude: lat,
          longitude: lng,
        });
        PageStore.setScrollEvent(() => {
          this.loadPageData(PageStore.state.page + 1);
        });
        if (!restored) this.loadPageData();
      };
      loadPageData = async (page = 0) => {
        const { APIStore, PageStore } = this.props;

        if ((page > 0 && PageStore.state.isEnd) || APIStore.isLoading) return;

        await PageStore.setState({ page, isPending: true });
        const resolve =
          (await APIStore._get(`/v2/api/search/${this.state.tab}/${page}`, {
            keyword: PageStore.state.keyword,
            order: PageStore.state.order,
            type: PageStore.state.type,
            latitude: PageStore.state.latitude,
            longitude: PageStore.state.longitude,
          })) || {};

        const {
          content = [],
          pageable: { pageSize = 0 } = {},
          totalElements = 0,
        } = resolve[this.state.tab] || {};

        PageStore.setState({ totalElements });

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

      onClick = (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        if (this.state.tab === "ship") {
          PageStore.push(`/company/boat/detail/${item.id}`);
        } else if (this.state.tab === "live") {
          PageStore.push(`/story/tv/detail/${item.id}`);
        } else if (this.state.tab === "diary") {
          PageStore.push(`/story/diary/detail/${item.id}`);
        } else if (this.state.tab === "blog") {
          PageStore.push(`/story/user/detail/${item.id}`);
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <SearchNavigationLayout
              text={PageStore.state.keyword}
              onSubmit={(text) => {
                PageStore.setState({ keyword: text });
                this.initPageData(this.state.tab);
              }}
            />
            <SearchTab
              activeIndex={this.state.activeIndex}
              keyword={PageStore.state.keyword}
              onClick={(type) => this.initPageData(type)}
            />

            {/** Filter */}
            <div className="filterWrap">
              <div className="row no-gutters d-flex align-items-center">
                <div
                  className={
                    this.state.tab === "ship" || this.state.tab === "live"
                      ? "col-6"
                      : "col-4"
                  }
                >
                  <p className="mt-2 pl-2">
                    ???{PageStore.state.keyword}??? ????????????{" "}
                    <strong className="text-primary">
                      {Intl.NumberFormat().format(
                        PageStore.state.totalElements || 0
                      )}
                      ???
                    </strong>
                  </p>
                </div>
                <div
                  className={
                    this.state.tab === "ship" || this.state.tab === "live"
                      ? "col-6 text-right"
                      : "col-8 text-right"
                  }
                >
                  {(this.state.tab === "ship" || this.state.tab === "live") && (
                    <React.Fragment>
                      <div className="custom-control custom-radio custom-control-inline">
                        <input
                          type="radio"
                          id="customRadioInline1"
                          name="customRadioInline1"
                          className="custom-control-input"
                          defaultChecked={true}
                          onChange={(e) => {
                            if (e.target.checked) {
                              PageStore.setState({ order: "distance" });
                              this.loadPageData(0, "distance");
                            }
                          }}
                        />
                        <label
                          className="custom-control-label"
                          htmlFor="customRadioInline1"
                        >
                          ?????????
                        </label>
                      </div>
                      <div className="custom-control custom-radio custom-control-inline">
                        <input
                          type="radio"
                          id="customRadioInline2"
                          name="customRadioInline1"
                          className="custom-control-input"
                          onChange={(e) => {
                            if (e.target.checked) {
                              PageStore.setState({ order: "name" });
                              this.loadPageData(0, "name");
                            }
                          }}
                        />
                        <label
                          className="custom-control-label"
                          htmlFor="customRadioInline2"
                        >
                          ?????????
                        </label>
                      </div>
                    </React.Fragment>
                  )}
                  {(this.state.tab === "diary" ||
                    this.state.tab === "blog") && (
                    <React.Fragment>
                      <div className="custom-control custom-radio custom-control-inline">
                        <input
                          type="radio"
                          id="customRadioInline1"
                          name="customRadioInline1"
                          className="custom-control-input"
                          defaultChecked={true}
                          onChange={(e) => {
                            if (e.target.checked) {
                              PageStore.setState({ order: "createDate" });
                              this.loadPageData(0, "createDate");
                            }
                          }}
                        />
                        <label
                          className="custom-control-label"
                          htmlFor="customRadioInline1"
                        >
                          ?????????
                        </label>
                      </div>
                      <div className="custom-control custom-radio custom-control-inline">
                        <input
                          type="radio"
                          id="customRadioInline2"
                          name="customRadioInline1"
                          className="custom-control-input"
                          onChange={(e) => {
                            if (e.target.checked) {
                              PageStore.setState({ order: "loves" });
                              this.loadPageData(0, "loves");
                            }
                          }}
                        />
                        <label
                          className="custom-control-label"
                          htmlFor="customRadioInline2"
                        >
                          ????????????
                        </label>
                      </div>
                      <div className="custom-control custom-radio custom-control-inline">
                        <input
                          type="radio"
                          id="customRadioInline3"
                          name="customRadioInline1"
                          className="custom-control-input"
                          onChange={(e) => {
                            if (e.target.checked) {
                              PageStore.setState({ order: "comments" });
                              this.loadPageData(0, "comments");
                            }
                          }}
                        />
                        <label
                          className="custom-control-label"
                          htmlFor="customRadioInline3"
                        >
                          ?????????
                        </label>
                      </div>
                    </React.Fragment>
                  )}
                </div>
              </div>
            </div>

            {/** ?????? ?????? ??????TV */}
            <div className="container nopadding mt-3 mb-0">
              <h5>
                {this.state.tab === "ship"
                  ? "??????"
                  : this.state.tab === "diary"
                  ? "????????????"
                  : this.state.tab === "live"
                  ? "??????TV"
                  : "?????????"}
              </h5>
              {PageStore.state.list &&
                PageStore.state.list.map((data, index) => {
                  if (this.state.tab === "ship" || this.state.tab === "live") {
                    return (
                      <SearchCompanyListItem01View
                        key={index}
                        data={data}
                        onClick={this.onClick}
                      />
                    );
                  } else {
                    return (
                      <SearchStoryListItemView
                        key={index}
                        data={data}
                        onClick={this.onClick}
                      />
                    );
                  }
                })}
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
