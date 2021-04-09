import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, SmartFishingMainTab },
  VIEW: { SmartfishingBoatListItemView },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "ModalStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.arr_keywordType = [{ text: "선박명", value: "shipName" }];
        this.arr_cameraActive = [
          { text: "유", value: true },
          { text: "무", value: false },
        ];
        this.keywordType = React.createRef(null);
        this.keyword = React.createRef(null);
        this.cameraActive = React.createRef(null);
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const { PageStore } = this.props;
        const restored = PageStore.restoreState({
          isPending: false,
          isEnd: false,
          page: 0,
          list: [],
          keywordType: "shipName",
          keyword: "",
          cameraActive: null,
        });
        PageStore.setScrollEvent(() => {
          this.loadPageData(PageStore.state.page + 1);
        });
        if (!restored) this.loadPageData();
      }
      componentWillUnmount() {
        const { PageStore } = this.props;
        PageStore.removeScrollEvent();
      }
      initSearchData = () => {
        const { PageStore } = this.props;
        PageStore.setState({
          keywordType: "shipName",
          keyword: "",
          cameraActive: null,
        });
        this.keywordType.current.value = "shipName";
        this.keyword.current.value = "";
        this.cameraActive.current.value = "";
      };
      loadPageData = async (page = 0) => {
        const { APIStore, PageStore, ModalStore } = this.props;
        const {
          isPending,
          isEnd,
          keywordType,
          keyword,
          cameraActive,
        } = PageStore.state;

        if (APIStore.isLoading || isPending || (page > 0 && isEnd)) return;

        PageStore.setState({ isPending: true, page });

        const { content = [], pageable: { pageSize = 0 } = {} } =
          (await APIStore._get(`/v2/api/smartfishing/ship/${page}`, {
            keywordType,
            keyword,
            cameraActive,
          })) || {};

        if (page === 0) {
          PageStore.setState({ list: content });
          setTimeout(() => {
            window.scrollTo(0, 0);
          }, 100);
          if (content.length === 0) {
            ModalStore.openModal("Alert", {
              body: "조회된 데이터가 없습니다.",
            });
          }
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
      onClickAdd = async () => {
        const { PageStore } = this.props;
        PageStore.storeState();
        PageStore.push(`/boat/add`);
      };
      onClick = (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        PageStore.push(`/boat/add?id=${item.id}`);
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout title={"선박관리"} showBackIcon={true} />
            <SmartFishingMainTab activeIndex={4} />

            <div className="filterlinewrap container nopadding">
              <ul className="nav nav-tabs nav-filter">
                <li>
                  <div className="input-group keyword">
                    <select
                      ref={this.keywordType}
                      className="custom-select"
                      onChange={(e) =>
                        PageStore.setState({
                          keywordType: e.target.selectedOptions[0].value,
                        })
                      }
                    >
                      {this.arr_keywordType.map((data, index) => (
                        <option key={index} value={data["value"]}>
                          {data["text"]}
                        </option>
                      ))}
                    </select>
                    <input
                      ref={this.keyword}
                      type="text"
                      className="form-control"
                      placeholder="검색어 입력"
                      value={PageStore.state.keyword}
                      onChange={(e) =>
                        PageStore.setState({ keyword: e.target.value })
                      }
                    />
                  </div>
                </li>
                <li>
                  <label htmlFor="selPay" className="sr-only">
                    녹화영상
                  </label>
                  <select
                    ref={this.cameraActive}
                    className="form-control"
                    onChange={(e) =>
                      PageStore.setState({
                        cameraActive: e.target.selectedOptions[0].value,
                      })
                    }
                  >
                    <option value={""}>녹화영상전체</option>
                    {this.arr_cameraActive.map((data, index) => (
                      <option value={data["value"]}>{data["text"]}</option>
                    ))}
                  </select>
                </li>
                <li className="full">
                  <p>
                    <a
                      className="btn btn-primary btn-sm"
                      onClick={() => this.loadPageData(0)}
                    >
                      검색
                    </a>
                    <a
                      className="btn btn-grey btn-sm"
                      onClick={this.initSearchData}
                    >
                      초기화
                    </a>
                  </p>
                </li>
              </ul>
            </div>
            <p className="clearfix"></p>

            {PageStore.state.list?.map((data, index) => (
              <SmartfishingBoatListItemView
                key={index}
                data={data}
                onClick={this.onClick}
              />
            ))}

            <a onClick={this.onClickAdd} className="add-circle">
              <img
                src="/assets/smartfishing/img/svg/icon-write-white.svg"
                alt=""
                className="add-icon"
              />
            </a>
          </React.Fragment>
        );
      }
    }
  )
);
