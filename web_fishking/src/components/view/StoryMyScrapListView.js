import React from "react";
import { inject, observer } from "mobx-react";
import StoryScrapListItemView from "./StoryScrapListItemView";

export default inject(
  "PageStore",
  "APIStore",
  "DataStore"
)(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const { PageStore } = this.props;
        const restored = PageStore.restoreState({
          page: 0,
          list: [],
          isEnd: false,
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
      loadPageData = async (page = 0) => {
        const { APIStore, PageStore } = this.props;

        if (page > 0 && PageStore.state.isEnd) return;

        PageStore.setState({ page });
        const { content = [], pageable: { pageSize = 0 } = {} } =
          (await APIStore._get("/v2/api/myFishingDiaryScrap/" + page)) || {};

        // console.log(JSON.stringify(content));
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
        PageStore.push(`/story/diary/detail/${item.id}`);
      };

      onClickLike = async (item) => {
        const { APIStore, DataStore, PageStore } = this.props;
        const takeType =
          item.fishingDiaryType === "조행일지" ? "fishingDiary" : "fishingBlog";
        let resolve = false;
        if (item.isLikeTo) {
          resolve = await APIStore._delete(`/v2/api/loveto`, {
            takeType,
            linkId: item.id,
          });
        } else {
          resolve = await APIStore._post(`/v2/api/loveto`, {
            takeType,
            linkId: item.id,
          });
        }
        if (resolve) {
          const list = DataStore.updateItemOfArrayByKey(
            PageStore.state.list,
            "id",
            item.id,
            {
              isLikeTo: !item.isLikeTo,
              likeCount: item.isLikeTo
                ? item.likeCount - 1
                : item.likeCount + 1,
            }
          );
          PageStore.setState({ list });
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore, addPathname = "" } = this.props;
        return (
          <React.Fragment>
            {PageStore.state.list &&
              PageStore.state.list.map((data, index) => (
                <StoryScrapListItemView
                  key={index}
                  data={data}
                  onClick={this.onClick}
                  onClickLike={this.onClickLike}
                  onClickProfile={(item) =>
                    PageStore.push(`/member/profile/${item.memberId}`)
                  }
                  onClickComment={(item) => {
                    PageStore.push(`/story/diary/comment/${item.id}`);
                  }}
                />
              ))}
            {(!PageStore.state.list || PageStore.state.list.length === 0) && (
              <div className="container nopadding mt-3 mb-0 text-center">
                <p className="mt-5 mb-3">
                  <img
                    src="/assets/cust/img/svg/icon-scrap-no.svg"
                    alt=""
                    className="icon-lg"
                  />
                </p>
                <h6>내가 스크랩한 게시글이 없습니다.</h6>
                <p className="mt-3">
                  <small className="grey">
                    낚시인들의 소중한 경험을 저장해 보세요.
                  </small>
                </p>
                <p className="mt-5">
                  <a
                    onClick={() => PageStore.push(addPathname)}
                    className="btn btn-primary btn-round"
                  >
                    어복스토리 보러 가기
                  </a>
                </p>
              </div>
            )}
          </React.Fragment>
        );
      }
    }
  )
);
