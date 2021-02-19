import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { MainTab },
} = Components;

export default inject("PageStore")(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/

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
                <a className="nav-link">
                  <img
                    src="/assets/cust/img/svg/quick-boat.svg"
                    alt="바다낚시"
                  />{" "}
                  선상 실시간 예약
                </a>{" "}
                &nbsp;&nbsp;
                <a className="nav-link">
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
              {/** 실시간 조황 */}
              <h5>실시간 조황</h5>
              <div className="slideList">
                <ul className="listWrap">
                  <li className="item">
                    <a>
                      <div className="imgWrap">
                        <img
                          src="/assets/cust/img/sample/live1.jpg"
                          className="img-fluid"
                          alt=""
                        />
                        <span className="play">
                          <img
                            src="/assets/cust/img/svg/live-play.svg"
                            alt=""
                          />
                        </span>
                        <span className="play-live">LIVE</span>
                      </div>
                      <div className="InfoWrap">
                        <h6>챔피온 1호</h6>
                        <p>
                          <strong className="text-primary">돌문어</strong>전남
                          여수시
                        </p>
                      </div>
                    </a>
                  </li>

                  <li className="item more">
                    <a className="moreLink">
                      <div className="inner">
                        <span>더보기</span>
                      </div>
                    </a>
                  </li>
                </ul>
              </div>

              {/** 출조 정보 */}
              <h5>출조 정보</h5>
              <div className="slideList">
                <ul className="listWrap">
                  <li className="item">
                    <a>
                      <div className="imgWrap">
                        <img
                          src="/assets/cust/img/sample/boat1.jpg"
                          className="img-fluid"
                          alt=""
                        />
                        <span className="play">
                          <img
                            src="/assets/cust/img/svg/live-play.svg"
                            alt=""
                          />
                        </span>
                        <span className="play-time">20:17</span>
                      </div>
                      <div className="InfoWrap">
                        <h6>챔피온 1호</h6>
                        <p>
                          <strong className="text-primary">돌문어</strong>전남
                          여수시
                        </p>
                        <h6>
                          70,000<small>원</small>
                        </h6>
                      </div>
                    </a>
                  </li>
                  <li className="item more">
                    <a className="moreLink">
                      <div className="inner">
                        <span>더보기</span>
                      </div>
                    </a>
                  </li>
                </ul>
              </div>

              {/** 광고배너 */}
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
                    <li
                      data-target="#carouselRecommend"
                      data-slide-to="0"
                      className="active"
                    ></li>
                    <li data-target="#carouselRecommend" data-slide-to="1"></li>
                    <li data-target="#carouselRecommend" data-slide-to="2"></li>
                  </ol>
                  <div className="carousel-inner">
                    <div className="carousel-item active">
                      <div className="card">
                        <div className="row no-gutters">
                          <div className="cardimgWrap">
                            <img
                              src="/assets/cust/img/sample/boat2.jpg"
                              className="img-fluid"
                              alt=""
                            />
                          </div>
                          <div className="cardInfoWrap">
                            <div className="card-body">
                              <h6>어복황제1호</h6>
                              <p>
                                <strong className="text-primary">쭈꾸미</strong>
                                <br />
                                전남 진도군 27km
                              </p>
                              <h6 className="btm-right">
                                70,000<small>원</small>
                              </h6>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="carousel-item">
                      <div className="card">
                        <div className="row no-gutters">
                          <div className="cardimgWrap">
                            <img
                              src="/assets/cust/img/sample/boat1.jpg"
                              className="img-fluid"
                              alt=""
                            />
                          </div>
                          <div className="cardInfoWrap">
                            <div className="card-body">
                              <h6>어복황제1호</h6>
                              <p>
                                <strong className="text-primary">쭈꾸미</strong>
                                <br />
                                전남 진도군 27km
                              </p>
                              <h6 className="btm-right">
                                70,000<small>원</small>
                              </h6>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div className="carousel-item">
                      <div className="card">
                        <div className="row no-gutters">
                          <div className="cardimgWrap">
                            <img
                              src="/assets/cust/img/sample/boat3.jpg"
                              className="img-fluid"
                              alt=""
                            />
                          </div>
                          <div className="cardInfoWrap">
                            <div className="card-body">
                              <h6>어복황제1호</h6>
                              <p>
                                <strong className="text-primary">쭈꾸미</strong>
                                <br />
                                전남 진도군 27km
                              </p>
                              <h6 className="btm-right">
                                70,000<small>원</small>
                              </h6>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              {/** 조황일지 */}
              <h5>조황 일지</h5>
              <div className="slideList">
                <ul className="listWrap">
                  <li className="item">
                    <a>
                      <div className="imgWrap">
                        <img
                          src="/assets/cust/img/sample/photo8.jpg"
                          className="img-fluid"
                          alt=""
                        />
                      </div>
                      <div className="InfoWrap">
                        <h6>이글스호 백조기 조황</h6>
                        <p>
                          <strong className="text-primary">돌문어</strong>전남
                          여수시
                        </p>
                      </div>
                    </a>
                  </li>
                  <li className="item more">
                    <a className="moreLink">
                      <div className="inner">
                        <span>더보기</span>
                      </div>
                    </a>
                  </li>
                </ul>
              </div>

              {/** 지역별 조황 */}
              <div className="mainMapWrap">
                <h5>지역별 조황</h5>
                <div className="mainMap">
                  <a>
                    <div className="infoMap NorthWestSea">
                      서해북부 <strong>7</strong>
                    </div>
                  </a>
                  <a>
                    <div className="infoMap CentralWestSea">
                      서해중부 <strong>9</strong>
                    </div>
                  </a>
                  <a>
                    <div className="infoMap SouthWestSea">
                      서해남부 <strong>12</strong>
                    </div>
                  </a>
                  <a>
                    <div className="infoMap WestSouthSea">
                      남해서부 <strong>11</strong>
                    </div>
                  </a>
                  <a>
                    <div className="infoMap CentralSouthSea">
                      남해중부 <strong>17</strong>
                    </div>
                  </a>
                  <a>
                    <div className="infoMap EastSouthSea">
                      남해동부 <strong>12</strong>
                    </div>
                  </a>
                  <a>
                    <div className="infoMap SouthEastSea">
                      동해남부 <strong>7</strong>
                    </div>
                  </a>
                  <a>
                    <div className="infoMap CentralEastSea">
                      동해중부 <strong>5</strong>
                    </div>
                  </a>
                  <a>
                    <div className="infoMap NorthEastSea">
                      동해북부 <strong>3</strong>
                    </div>
                  </a>
                  <a>
                    <div className="infoMap Jeju">
                      제주도 <strong>11</strong>
                    </div>
                  </a>
                </div>
              </div>

              {/** 지역별 조황*/}
              <h5>어종별 조황</h5>
              <div className="slideList fishList">
                <ul className="listWrap">
                  <li className="item">
                    <a>
                      <div className="imgWrap imgFish">
                        <img
                          src="/assets/cust/img/fish/fish_icon_01w.svg"
                          alt=""
                        />
                      </div>
                      <div className="InfoWrap">
                        <h6>
                          갈치 <strong className="text-primary">17</strong>
                        </h6>
                      </div>
                    </a>
                  </li>
                  <li className="item more">
                    <a className="moreLink">
                      <div className="inner">
                        <span>더보기</span>
                      </div>
                    </a>
                  </li>
                </ul>
              </div>
            </div>
            <p className="space"></p>

            <MainTab activeIndex={0} />
          </React.Fragment>
        );
      }
    }
  )
);
