/* global $ */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout },
  VIEW: { SearchAdListItemView },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.keyword = React.createRef(null);
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
        const resolve = await APIStore._get("/v2/api/search/keywords");
        this.setState(resolve);

        PageStore.reloadSwipe();
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout title={"검색"} showBackIcon={true} />

            <nav className="nav nav-pills nav-menu nav-justified">
              <a
                className="nav-link active"
                onClick={() => PageStore.push(`/search/all`)}
              >
                전체
              </a>
              <a
                className="nav-link"
                onClick={() => PageStore.push(`/search/reserve`)}
              >
                예약
              </a>
            </nav>

            {/** 검색 */}
            <div className="container nopadding mt-3 mb-0">
              <form className="form-search">
                <a>
                  <img
                    src="/assets/img/svg/form-search.svg"
                    alt=""
                    className="icon-search"
                  />
                </a>
                <input
                  ref={this.keyword}
                  className="form-control mr-sm-2"
                  type="search"
                  placeholder="어떤 낚시를 찾고 있나요?"
                  aria-label="Search"
                />
                <a
                  onClick={() => {
                    if (this.keyword.current?.value == "") return;
                    PageStore.push(
                      `/search/keyword/all?keyword=${this.keyword.current?.value}`
                    );
                  }}
                >
                  <img src="/assets/img/svg/navbar-search.svg" alt="Search" />
                </a>
              </form>
            </div>

            {/** 인기검색어 */}
            <div className="container nopadding mt-2 mb-0">
              <h5>
                인기 검색어{" "}
                <small className="red">
                  <strong>HOT</strong>
                </small>
              </h5>
              <ul className="list-search">
                {this.state.keywords &&
                  this.state.keywords.length > 0 &&
                  this.state.keywords.slice(0, 5).map((data, index) => (
                    <li key={index + 1}>
                      <a
                        onClick={() => {
                          PageStore.push(
                            `/search/keyword/all?keyword=${data["keyword"]}`
                          );
                        }}
                      >
                        <strong>{Intl.NumberFormat().format(index + 1)}</strong>{" "}
                        {data["keyword"]}
                        {data["isNew"] && (
                          <span className="new float-right">NEW</span>
                        )}
                      </a>
                    </li>
                  ))}
              </ul>
              {this.state.keywords && this.state.keywords.length > 5 && (
                <React.Fragment>
                  <div className="toggle-content">
                    <ul className="list-search">
                      {this.state.keywords.splice(5, 10).map((data, index) => (
                        <li key={index}>
                          <a>
                            <strong>
                              {Intl.NumberFormat().format(index + 6)}
                            </strong>{" "}
                            {data["keyword"]}
                            {data["isNew"] && (
                              <span className="new float-right">NEW</span>
                            )}
                          </a>
                        </li>
                      ))}
                    </ul>
                  </div>
                  <div className="togglewrap">
                    <a
                      className="toggle-btn"
                      onClick={() => {
                        $(".toggle-content").slideToggle("slow");
                        $(".toggle-btn").toggleClass("active");
                        return false;
                        $(".toggle-content").toggleClass("expanded");
                      }}
                    ></a>
                  </div>
                </React.Fragment>
              )}
            </div>

            {/** 어복황제 추천 */}
            {this.state.ad && (
              <div className="container nopadding mt-2 mb-0">
                <h5>
                  어복황제 추천 <strong className="text-primary">AD</strong>
                </h5>
                <div className="slideList slideList-md">
                  <ul className="listWrap">
                    {this.state.ad.map((data, index) => (
                      <SearchAdListItemView key={index} data={data} />
                    ))}
                  </ul>
                </div>
              </div>
            )}
          </React.Fragment>
        );
      }
    }
  )
);
