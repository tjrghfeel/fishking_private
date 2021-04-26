import React from "react";
import { inject, observer } from "mobx-react";
import StoryReviewListItemView from "./StoryReviewListItemView";

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
          (await APIStore._get("/v2/api/myReviewList/" + page)) || {};

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
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore, addPathname = "" } = this.props;
        return (
          <React.Fragment>
            {PageStore.state.list &&
              PageStore.state.list.map((data, index) => (
                <StoryReviewListItemView key={index} data={data} />
              ))}
            {((!PageStore.state.list || PageStore.state.list.length === 0) && this.state.loaded) && (
              <div className="container nopadding mt-3 mb-0 text-center">
                <p className="mt-5 mb-3">
                  <img
                    src="/assets/cust/img/svg/icon-review-no.svg"
                    alt=""
                    className="icon-lg"
                  />
                </p>
                <h6>내가 작성한 리뷰가 없습니다.</h6>
                {/*<p className="mt-3">*/}
                {/*  <small className="grey">*/}
                {/*    나에게 맞는 실시간 낚시 찾아보기*/}
                {/*  </small>*/}
                {/*</p>*/}
                {/*<p className="mt-5">*/}
                {/*  <a*/}
                {/*    onClick={() => PageStore.push(addPathname)}*/}
                {/*    className="btn btn-primary btn-round"*/}
                {/*  >*/}
                {/*    낚시 예약해 보기*/}
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
