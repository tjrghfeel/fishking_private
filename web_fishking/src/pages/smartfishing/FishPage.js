import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, SmartFishingMainTab },
  VIEW: { StoryPostListItemView },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "ModalStore",
  "DataStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          arr_searchTarget: [
            { text: "내용", value: "content" },
            { text: "제목", value: "title" },
          ],
          arr_shipId: [],
        };
        this.searchTarget = React.createRef(null);
        this.searchKey = React.createRef(null);
        this.shipId = React.createRef(null);
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const { PageStore, APIStore } = this.props;
        const restored = PageStore.restoreState({
          isPending: false,
          isEnd: false,
          page: 0,
          list: [],
          searchTarget: "content",
          searchKey: null,
          shipId: null,
        });
        PageStore.setScrollEvent(() => {
          this.loadPageData(PageStore.state.page + 1);
        });
        let isShipListEnd = false;
        let shipPage = 0;
        const arr_shipId = [];
        while (!isShipListEnd) {
          const resolve = await APIStore._get(
            `/v2/api/smartfishing/ship/${shipPage}`
          );
          if (resolve && resolve["content"] && resolve["content"].length > 0) {
            for (let item of resolve["content"]) {
              arr_shipId.push({
                text: item["shipName"],
                value: item["id"],
              });
            }
            shipPage = shipPage + 1;
          } else {
            isShipListEnd = true;
          }
        }
        this.setState({ arr_shipId });
        if (!restored) this.loadPageData(0);
      }
      componentWillUnmount() {
        const { PageStore } = this.props;
        PageStore.removeScrollEvent();
      }
      initSearchData = () => {
        const { PageStore } = this.props;
        PageStore.setState({
          searchTarget: "content",
          searchKey: null,
          shipId: null,
        });
        this.searchTarget.current.value = "content";
        this.searchKey.current.value = "";
        this.shipId.current.value = "";
      };
      loadPageData = async (page = 0) => {
        const { APIStore, PageStore, ModalStore } = this.props;
        const {
          isPending,
          isEnd,
          searchTarget,
          searchKey,
          shipId,
        } = PageStore.state;

        if (APIStore.isLoading || isPending || (page > 0 && isEnd)) return;

        PageStore.setState({ isPending: true, page });

        const { content = [], pageable: { pageSize = 0 } = {} } =
          (await APIStore._get(
            `/v2/api/smartFishing/fishingDiary/list/${page}`,
            {
              searchTarget,
              searchKey,
              shipId,
            }
          )) || {};

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
      onClick = async (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        PageStore.push(`/cust/story/diary/detail/${item.id}`);
      };
      onClickProfile = async (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        PageStore.push(`/cust/member/profile/${item.memberId}`);
      };
      onClickLike = async (item) => {
        const { APIStore, DataStore, PageStore } = this.props;
        if (item.isLikeTo) {
          const resolve = await APIStore._delete("/v2/api/loveto", {
            linkId: item.id,
            takeType: "fishingDiary",
          });
          if (resolve) {
            const list = DataStore.updateItemOfArrayByKey(
              PageStore.state.list,
              "id",
              item.id,
              { isLikeTo: false, likeCount: item.likeCount - 1 }
            );
            PageStore.setState({ list });
          }
        } else {
          const resolve = await APIStore._post("/v2/api/loveto", {
            linkId: item.id,
            takeType: "fishingDiary",
          });
          if (resolve) {
            const list = DataStore.updateItemOfArrayByKey(
              PageStore.state.list,
              "id",
              item.id,
              { isLikeTo: true, likeCount: item.likeCount + 1 }
            );
            PageStore.setState({ list });
          }
        }
      };
      onClickComment = async (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        PageStore.push(`/cust/story/diary/comment/${item.id}`);
      };
      onClickScrap = async (item) => {
        const { APIStore, DataStore, PageStore } = this.props;
        if (item.isScraped) {
          const resolve = await APIStore._delete("/v2/api/fishingDiary/scrap", {
            fishingDiaryId: item.id,
          });
          if (resolve) {
            const list = DataStore.updateItemOfArrayByKey(
              PageStore.state.list,
              "id",
              item.id,
              { isScraped: false, scrapCount: item.scrapCount - 1 }
            );
            PageStore.setState({ list });
          }
        } else {
          const resolve = await APIStore._post("/v2/api/fishingDiary/scrap", {
            fishingDiaryId: item.id,
          });
          if (resolve) {
            const list = DataStore.updateItemOfArrayByKey(
              PageStore.state.list,
              "id",
              item.id,
              { isScraped: true, scrapCount: item.scrapCount + 1 }
            );
            PageStore.setState({ list });
          }
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout title={"조황관리"} showBackIcon={true} />
            <SmartFishingMainTab activeIndex={2} />

            <div className="filterlinewrap container nopadding">
              <ul className="nav nav-tabs nav-filter">
                <li>
                  <div className="input-group keyword">
                    <select
                      ref={this.searchTarget}
                      className="custom-select"
                      onChange={(e) =>
                        PageStore.setState({
                          searchTarget: e.target.selectedOptions[0].value,
                        })
                      }
                    >
                      {this.state.arr_searchTarget.map((data, index) => (
                        <option key={index} value={data["value"]}>
                          {data["text"]}
                        </option>
                      ))}
                    </select>
                    <input
                      ref={this.searchKey}
                      type="text"
                      className="form-control"
                      placeholder="검색어 입력"
                      value={PageStore.state.searchKey}
                      onChange={(e) =>
                        PageStore.setState({ searchKey: e.target.value })
                      }
                    />
                  </div>
                </li>
                <li>
                  <label htmlFor="selPay" className="sr-only">
                    선상명
                  </label>
                  <select
                    ref={this.shipId}
                    className="form-control"
                    onChange={(e) =>
                      PageStore.setState({
                        shipId: e.target.selectedOptions[0].value,
                      })
                    }
                  >
                    <option value={""}>전체</option>
                    {this.state.arr_shipId.map((data, index) => (
                      <option key={index} value={data["value"]}>
                        {data["text"]}
                      </option>
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
              <StoryPostListItemView
                key={index}
                data={data}
                showLikeIcon={true}
                showCommentIcon={true}
                showScrapIcon={true}
                onClick={this.onClick}
                onClickProfile={this.onClickProfile}
                onClickLike={this.onClickLike}
                onClickComment={this.onClickComment}
                onClickScrap={this.onClickScrap}
              />
            ))}

            <a
              onClick={() => PageStore.push(`/cust/story/add?iscompany=Y`)}
              className="add-circle"
            >
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
