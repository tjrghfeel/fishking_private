/* global $ */
import React from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router-dom";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout },
  VIEW: { CompanyReviewListItemView },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    withRouter(
      class extends React.Component {
        constructor(props) {
          super(props);
          this.state = {
            isPending: false,
            isEnd: false,
            list: [],
            page: 0,
            size: 10,
            average: 0,
            service: 0,
            taste: 0,
            clean: 0,
          };
        }
        /********** ********** ********** ********** **********/
        /** function */
        /********** ********** ********** ********** **********/
        componentDidMount() {
          const { PageStore } = this.props;
          PageStore.setScrollEvent(() => {
            this.loadPageData(this.state.page + 1);
          });
          this.loadPageData();
        }
        componentWillUnmount() {
          const { PageStore } = this.props;
          PageStore.removeScrollEvent();
        }

        loadPageData = async (page = 0) => {
          const {
            PageStore,
            APIStore,
            match: {
              params: { id: ship_id },
            },
          } = this.props;

          if ((page > 0 && this.state.isEnd) || APIStore.isLoading) return;

          this.setState({ page, isPending: true });

          const {
            reviews: {
              content,
              pageable: { pageSize = 0 },
            },
            average,
            service,
            taste,
            clean,
          } = await APIStore._get(`/v2/api/ship/${ship_id}/review/${page}`, {
            page,
            size: this.state.size,
          });
          this.setState({ average, service, taste, clean });

          if (page === 0) {
            this.setState({ list: content });
            // # 별점 스크립트 로드
            PageStore.injectScript("/assets/cust/js/jquery.rateit.min.js", {
              global: true,
            });
            setTimeout(() => {
              window.scrollTo(0, 0);
            }, 100);
          } else {
            this.setState({ list: this.state.list.concat(content) });
          }
          if (content.length < pageSize) {
            this.setState({ isEnd: true });
          } else {
            this.setState({ isEnd: false });
          }

          PageStore.applySwipe();
        };
        /********** ********** ********** ********** **********/
        /** render */
        /********** ********** ********** ********** **********/
        render() {
          const { PageStore } = this.props;
          return (
            <React.Fragment>
              <NavigationLayout
                title={"리뷰"}
                showBackIcon={true}
                // customButton={
                //   <a
                //     onClick={() => PageStore.push(`/story/add`)}
                //     className="fixed-top-right text-white"
                //   >
                //     글쓰기
                //   </a>
                // }
              />

              {/** 리뷰 */}
              <div className="container nopadding mt-4">
                <div className="row no-gutters align-items-center">
                  <div className="col-4 text-center align-self-center">
                    <h2 className="rateit-text">
                      <img src="/assets/cust/img/star-big.png" alt="profile" />
                      {(this.state.average || 0).toFixed(2)}
                    </h2>
                    <small>전체 평균 평점</small>
                  </div>
                  <div className="col-8 left-line">
                    <div className="rateit-wrap rateit-list-wrap">
                      <span className="float-left">
                        <strong className="title">손맛</strong>
                      </span>{" "}
                      &nbsp;&nbsp;
                      <div
                        className="rateit float-left"
                        data-rateit-value={(this.state.taste || 0).toFixed(1)}
                        data-rateit-ispreset="true"
                        data-rateit-readonly="true"
                        data-rateit-starwidth="16"
                        data-rateit-starheight="16"
                      ></div>
                    </div>
                    <div className="rateit-wrap rateit-list-wrap">
                      <span className="float-left">
                        <strong className="title">서비스</strong>
                      </span>{" "}
                      &nbsp;&nbsp;
                      <div
                        className="rateit float-left"
                        data-rateit-value={(this.state.service || 0).toFixed(1)}
                        data-rateit-ispreset="true"
                        data-rateit-readonly="true"
                        data-rateit-starwidth="16"
                        data-rateit-starheight="16"
                      ></div>
                    </div>
                    <div className="rateit-wrap rateit-list-wrap">
                      <span className="float-left">
                        <strong className="title">청결도</strong>
                      </span>{" "}
                      &nbsp;&nbsp;
                      <div
                        className="rateit float-left"
                        data-rateit-value={(this.state.clean || 0).toFixed(1)}
                        data-rateit-ispreset="true"
                        data-rateit-readonly="true"
                        data-rateit-starwidth="16"
                        data-rateit-starheight="16"
                      ></div>
                    </div>
                  </div>
                </div>
                <p className="space"></p>
              </div>

              {/** 리스트 */}
              {this.state.list &&
                this.state.list.map((data, index) => (
                  <CompanyReviewListItemView key={index} data={data} />
                ))}
            </React.Fragment>
          );
        }
      }
    )
  )
);
