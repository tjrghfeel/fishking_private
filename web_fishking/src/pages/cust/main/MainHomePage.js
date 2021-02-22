import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { MainTab },
  VIEW: {
    MainAdListItemView,
    MainFishingDiaryListItemView,
    MainShipListItemView,
    MainLiveListItemView,
  },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
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
        const resolve = await APIStore._get("/v2/api/main");
        console.log(JSON.stringify(resolve));
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
            {/** Navigation */}
            <nav className="navbar fixed-top navbar-dark bg-primary">
              <a className="navbar-brand">
                <img
                  src="/assets/cust/img/svg/navbar-logo.svg"
                  alt="어복황제"
                />
              </a>
              <form className="form-inline">
                <input
                  className="form-control mr-sm-2"
                  type="search"
                  placeholder=""
                  aria-label="Search"
                  disabled
                />
                <a onClick={() => PageStore.push(`/search/all`)}>
                  <img
                    src="/assets/cust/img/svg/navbar-search.svg"
                    alt="Search"
                  />
                </a>
              </form>
            </nav>

            {/** Carousel */}
            <div
              id="carousel-visual"
              className="carousel slide"
              data-ride="carousel"
            >
              <ol className="carousel-indicators">
                <li
                  data-target="#carousel-visual"
                  data-slide-to="0"
                  className="active"
                ></li>
                <li data-target="#carousel-visual" data-slide-to="1"></li>
                <li data-target="#carousel-visual" data-slide-to="2"></li>
              </ol>
              <div className="carousel-inner">
                <div className="carousel-item active">
                  <img
                    src="/assets/cust/img/slide1.jpg"
                    className="d-block w-100"
                    alt=""
                  />
                </div>
                <div className="carousel-item">
                  <img
                    src="/assets/cust/img/slide1.jpg"
                    className="d-block w-100"
                    alt=""
                  />
                </div>
                <div className="carousel-item">
                  <img
                    src="/assets/cust/img/slide1.jpg"
                    className="d-block w-100"
                    alt=""
                  />
                </div>
              </div>
            </div>
            <div className="clearfix"></div>

            {/** Quick Link */}
            <div className="container">
              <nav className="nav nav-pills quick nav-justified">
                <a
                  className="nav-link"
                  onClick={() => PageStore.push(`/main/company/boat`)}
                >
                  <img
                    src="/assets/cust/img/svg/quick-boat.svg"
                    alt="바다낚시"
                  />{" "}
                  선상 실시간 예약
                </a>{" "}
                &nbsp;&nbsp;
                <a
                  className="nav-link"
                  onClick={() => PageStore.push(`/main/company/rock`)}
                >
                  <img
                    src="/assets/cust/img/svg/quick-rocks.svg"
                    alt="갯바위낚시"
                  />{" "}
                  갯바위 실시간 예약
                </a>
              </nav>
            </div>
            <div className="clearfix"></div>

            {/** Content */}
            <div className="container nopadding">
              {this.state.live && (
                <React.Fragment>
                  {/** 실시간 조황 */}
                  <h5>실시간 조황</h5>
                  <div className="slideList">
                    <ul className="listWrap">
                      {this.state.live.map((data, index) => (
                        <MainLiveListItemView key={index} data={data} />
                      ))}

                      <li className="item more">
                        <a
                          className="moreLink"
                          onClick={() => PageStore.push(`/main/story/tv`)}
                        >
                          <div className="inner">
                            <span>더보기</span>
                          </div>
                        </a>
                      </li>
                    </ul>
                  </div>
                </React.Fragment>
              )}

              {/** 출조 정보 */}
              {this.state.ship && (
                <React.Fragment>
                  <h5>출조 정보</h5>
                  <div className="slideList">
                    <ul className="listWrap">
                      {this.state.ship.map((data, index) => (
                        <MainShipListItemView key={index} data={data} />
                      ))}
                      <li className="item more">
                        <a
                          className="moreLink"
                          onClick={() => PageStore.push(`/main/company/boat`)}
                        >
                          <div className="inner">
                            <span>더보기</span>
                          </div>
                        </a>
                      </li>
                    </ul>
                  </div>
                </React.Fragment>
              )}

              {/** 광고배너 */}
              {this.state.ad && (
                <div className="mainAdWrap">
                  <h5>
                    추천 업체 &nbsp;
                    <small className="text-primary">
                      <strong>AD</strong>
                    </small>
                  </h5>
                  <div
                    id="carouselRecommend"
                    className="carousel slide"
                    data-ride="carousel"
                  >
                    <ol className="carousel-indicators">
                      {this.state.ad.map((data, index) => (
                        <li
                          data-target="#carouselRecommend"
                          data-slide-to={index}
                          className={index === 0 ? "active" : ""}
                        ></li>
                      ))}
                    </ol>
                    <div className="carousel-inner">
                      {this.state.ad.map((data, index) => (
                        <MainAdListItemView
                          key={index}
                          data={data}
                          active={index === 0}
                        />
                      ))}
                    </div>
                  </div>
                </div>
              )}

              {/** 조황일지 */}
              {this.state.fishingDiaries && (
                <React.Fragment>
                  <h5>조황 일지</h5>
                  <div className="slideList">
                    <ul className="listWrap">
                      {this.state.fishingDiaries.map((data, index) => (
                        <MainFishingDiaryListItemView key={index} data={data} />
                      ))}
                      <li className="item more">
                        <a
                          className="moreLink"
                          onClick={() => PageStore.push(`/main/story/diary`)}
                        >
                          <div className="inner">
                            <span>더보기</span>
                          </div>
                        </a>
                      </li>
                    </ul>
                  </div>
                </React.Fragment>
              )}

              {/** 지역별 조황 */}
              {this.state.direction && (
                <div className="mainMapWrap">
                  <h5>지역별 조황</h5>
                  <div className="mainMap">
                    {this.state.direction.map((data, index) => {
                      let className = "infoMap";
                      if (data["code"] === "northWest")
                        className = className.concat(" NorthWestSea");
                      else if (data["code"] === "centralWest")
                        className = className.concat(" CentralWestSea");
                      else if (data["code"] === "southWest")
                        className = className.concat(" SouthWestSea");
                      else if (data["code"] === "westSouth")
                        className = className.concat(" WestSouthSea");
                      else if (data["code"] === "centralSouth")
                        className = className.concat(" CentralSouthSea");
                      else if (data["code"] === "eastSouth")
                        className = className.concat(" EastSouthSea");
                      else if (data["code"] === "northEast")
                        className = className.concat(" NorthEastSea");
                      else if (data["code"] === "centralEast")
                        className = className.concat(" CentralEastSea");
                      else if (data["code"] === "southEast")
                        className = className.concat(" SouthEastSea");
                      else if (data["code"] === "jeju")
                        className = className.concat(" Jeju");
                      return (
                        <a key={index}>
                          <div className={className}>
                            {data["codeName"]}{" "}
                            <strong>
                              {Intl.NumberFormat().format(data["count"] || 0)}
                            </strong>
                          </div>
                        </a>
                      );
                    })}
                  </div>
                </div>
              )}

              {/** 지역별 조황*/}
              {this.state.species && (
                <React.Fragment>
                  <h5>어종별 조황</h5>
                  <div className="slideList fishList">
                    <ul className="listWrap">
                      {this.state.species.map((data, index) => (
                        <li className="item" key={index}>
                          <a
                            onClick={() =>
                              PageStore.push(
                                `/main/company/boat?species=${data.code}`
                              )
                            }
                          >
                            {/*<div className="imgWrap imgFish">*/}
                            {/*  <img*/}
                            {/*    src="/assets/cust/img/fish/fish_icon_01w.svg"*/}
                            {/*    alt=""*/}
                            {/*  />*/}
                            {/*</div>*/}
                            <div className="InfoWrap">
                              <h6>
                                {data.codeName}{" "}
                                <strong className="text-primary">
                                  {Intl.NumberFormat().format(data.count)}
                                </strong>
                              </h6>
                            </div>
                          </a>
                        </li>
                      ))}
                      <li className="item more">
                        <a
                          className="moreLink"
                          onClick={() => PageStore.push(`/main/company/boat`)}
                        >
                          <div className="inner">
                            <span>더보기</span>
                          </div>
                        </a>
                      </li>
                    </ul>
                  </div>
                </React.Fragment>
              )}
            </div>
            <p className="space"></p>

            <MainTab activeIndex={0} />
          </React.Fragment>
        );
      }
    }
  )
);
