import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { SearchNavigationLayout, SearchTab },
  VIEW: { SearchCompanyListItem01View, SearchDiaryListItemView },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.keyword = "";
        this.state = {};
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        this.loadPageData();
      }

      loadPageData = async () => {
        const { APIStore, PageStore } = this.props;
        const qp = PageStore.getQueryParams();
        this.keyword = qp.keyword || "";

        const resolve = await APIStore._get(`/v2/api/search/all`, {
          keyword: this.keyword,
        });
        await this.setState(resolve);
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <SearchNavigationLayout text={this.keyword} />
            <SearchTab activeIndex={0} keyword={this.keyword} />

            {/** 업체 */}
            {(this.state.ship?.totalElements || 0) > 0 && (
              <div className="container nopadding mt-3 mb-0">
                <h5>업체</h5>
                {this.state.ship.content.map((data, index) => (
                  <SearchCompanyListItem01View
                    key={index}
                    data={data}
                    onClick={(item) =>
                      PageStore.push(`/company/boat/detail/${item.id}`)
                    }
                  />
                ))}
                <p className="text-right">
                  <a onClick={() => PageStore.push(`/main/company/boat`)}>
                    <small>
                      업체 더보기&nbsp;
                      <img
                        src="/assets/cust/img/svg/icon-arrow.svg"
                        alt=""
                        className="vam"
                      />
                    </small>
                  </a>
                </p>
                <p className="space"></p>
              </div>
            )}

            {/** 조황일지 */}
            {(this.state.diary?.totalElements || 0) > 0 && (
              <div className="container nopadding mt-3 mb-0">
                <h5>조황일지</h5>
                {this.state.diary.content.map((data, index) => (
                  <SearchDiaryListItemView
                    key={index}
                    data={data}
                    onClick={(item) =>
                      PageStore.push(`/story/diary/detail/${item.id}`)
                    }
                  />
                ))}
                <p className="text-right">
                  <a onClick={() => PageStore.push(`/main/story/diary`)}>
                    <small>
                      조황일지 더보기&nbsp;
                      <img
                        src="/assets/cust/img/svg/icon-arrow.svg"
                        alt=""
                        className="vam"
                      />
                    </small>
                  </a>
                </p>
                <p className="space"></p>
              </div>
            )}

            {/** 어복TV */}
            {(this.state.live?.totalElements || 0) > 0 && (
              <div className="container nopadding mt-3 mb-0">
                <h5>어복TV</h5>
                {this.state.live.content.map((data, index) => (
                  <SearchCompanyListItem01View
                    key={index}
                    data={data}
                    onClick={(item) =>
                      PageStore.push(`/story/tv/detail/${item.id}`)
                    }
                  />
                ))}
                <p className="text-right">
                  <a onClick={() => PageStore.push(`/main/story/tv`)}>
                    <small>
                      어복TV 더보기&nbsp;
                      <img
                        src="/assets/cust/img/svg/icon-arrow.svg"
                        alt=""
                        className="vam"
                      />
                    </small>
                  </a>
                </p>
                <p className="space"></p>
              </div>
            )}

            {/** 조행기 */}
            {(this.state.blog?.totalElements || 0) > 0 && (
              <div className="container nopadding mt-3 mb-0">
                <h5>조황일지</h5>
                {this.state.blog.content.map((data, index) => (
                  <SearchDiaryListItemView
                    key={index}
                    data={data}
                    onClick={(item) =>
                      PageStore.push(`/story/user/detail/${item.id}`)
                    }
                  />
                ))}
                <p className="text-right">
                  <a onClick={() => PageStore.push(`/main/story/user`)}>
                    <small>
                      조행기 더보기&nbsp;
                      <img
                        src="/assets/cust/img/svg/icon-arrow.svg"
                        alt=""
                        className="vam"
                      />
                    </small>
                  </a>
                </p>
                <p className="space"></p>
              </div>
            )}
          </React.Fragment>
        );
      }
    }
  )
);
