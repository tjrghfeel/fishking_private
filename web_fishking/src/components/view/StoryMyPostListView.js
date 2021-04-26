import React from "react";
import { inject, observer } from "mobx-react";
import StoryPostListItemView from "./StoryPostListItemView";

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          loaded: false
        }
      }
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
          (await APIStore._get("/v2/api/myFishingPostList/" + page)) || {};

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
        this.setState({ loaded: true })
      };

      onClick = (item) => {
        const { onClick } = this.props;
        if (onClick) {
          onClick(item);
        }
      };
      onClickProfile = (item) => {
        const { onClickProfile } = this.props;
        if (onClickProfile) {
          onClickProfile(item);
        }
      };
      onClickComment = (item) => {
        const { onClickComment, PageStore } = this.props;
        if (onClickComment) {
          PageStore.storeState();
          onClickComment(item);
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            {PageStore.state.list &&
              PageStore.state.list.map((data, index) => (
                <StoryPostListItemView
                  key={index}
                  data={data}
                  showCommentIcon={true}
                  onClick={this.onClick}
                  onClickProfile={this.onClickProfile}
                  onClickComment={this.onClickComment}
                />
              ))}
            {((!PageStore.state.list || PageStore.state.list.length === 0) && this.state.loaded) && (
              <div className="container nopadding mt-3 mb-0 text-center">
                <p className="mt-5 mb-3">
                  <img
                    src="/assets/cust/img/svg/icon-comment-no.svg"
                    alt=""
                    className="icon-lg"
                  />
                </p>
                <h6>내가 작성한 글이 없습니다.</h6>
                {/*<p className="mt-3">*/}
                {/*  <small className="grey">*/}
                {/*    댓글로 낚시인들과 소통해 보세요.*/}
                {/*  </small>*/}
                {/*</p>*/}
                {/*<p className="mt-5">*/}
                {/*  <a*/}
                {/*    onClick={() => PageStore.push(addPathname)}*/}
                {/*    className="btn btn-primary btn-round"*/}
                {/*  >*/}
                {/*    어복스토리 보러 가기*/}
                {/*  </a>*/}
                {/*</p>*/}
              </div>
            )}
          </React.Fragment>
        );
      }
    }
  )
);
