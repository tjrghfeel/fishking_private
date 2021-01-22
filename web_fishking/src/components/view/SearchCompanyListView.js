import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";

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
        return (
          <React.Fragment>
            {/** 검색 */}
            <div className="container nopadding mt-3 mb-0">
              <form className="form-search">
                <a href="search-all.html">
                  <img
                    src="/assets/img/svg/form-search.svg"
                    alt=""
                    className="icon-search"
                  />
                </a>
                <input
                  className="form-control mr-sm-2"
                  type="search"
                  placeholder="업체명 또는 키워드로 검색하세요."
                  aria-label="Search"
                />
                <a href="search.html">
                  <img src="/assets/img/svg/navbar-search.svg" alt="Search" />
                </a>
              </form>
            </div>
            {/** Filter */}
            <div className="container nopadding mt-3 mb-0">
              <div className="row no-gutters d-flex align-items-center">
                <div className="col-6">
                  <p className="mt-2 pl-2">
                    ‘쭈꾸미’ 검색결과{" "}
                    <strong className="text-primary">71건</strong>
                  </p>
                </div>
                <div className="col-6 text-right">
                  <div className="custom-control custom-radio custom-control-inline">
                    <input
                      type="radio"
                      id="customRadioInline1"
                      name="customRadioInline1"
                      className="custom-control-input"
                    />
                    <label
                      className="custom-control-label"
                      htmlFor="customRadioInline1"
                    >
                      거리순
                    </label>
                  </div>
                  <div className="custom-control custom-radio custom-control-inline">
                    <input
                      type="radio"
                      id="customRadioInline2"
                      name="customRadioInline1"
                      className="custom-control-input"
                    />
                    <label
                      className="custom-control-label"
                      htmlFor="customRadioInline2"
                    >
                      명칭순
                    </label>
                  </div>
                </div>
              </div>
            </div>
            {/** 업체 */}
            <div className="container nopadding mt-3 mb-0">
              <a href="boat-detail.html">
                <div className="card card-sm">
                  <div className="row no-gutters">
                    <div className="cardimgWrap">
                      <img
                        src="/assets/img/sample/boat1.jpg"
                        className="img-fluid"
                        alt=""
                      />
                      <span className="play">
                        <img src="/assets/img/svg/live-play.svg" alt="" />
                      </span>
                      <span className="play-time">20:17</span>
                    </div>
                    <div className="cardInfoWrap">
                      <div className="card-body">
                        <h6>어복황제3호</h6>
                        <p>
                          <strong className="text-primary">쭈꾸미, 우럭</strong>{" "}
                          <img
                            src="/assets/img/fish/fish_icon_02.svg"
                            alt=""
                            className="fish-cate"
                          />
                          13
                          <br />
                          <span className="grey">선상&nbsp;|</span>&nbsp;전남
                          진도군 27km
                          <br />
                        </p>
                        <div className="card-price">
                          <small className="orange">실시간예약</small>
                          <h5>
                            <strong>40,000</strong>
                            <small>원~</small>
                          </h5>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </a>
              <hr />
              <a href="boat-detail.html">
                <div className="card card-sm">
                  <div className="row no-gutters">
                    <div className="cardimgWrap">
                      <img
                        src="/assets/img/sample/boat2.jpg"
                        className="img-fluid"
                        alt=""
                      />
                      <span className="play">
                        <img src="/assets/img/svg/live-play.svg" alt="" />
                      </span>
                      <span className="play-live">LIVE</span>
                    </div>
                    <div className="cardInfoWrap">
                      <div className="card-body">
                        <h6>어복황제3호</h6>
                        <p>
                          <strong className="text-primary">쭈꾸미, 우럭</strong>{" "}
                          <img
                            src="/assets/img/fish/fish_icon_01.svg"
                            alt=""
                            className="fish-cate"
                          />
                          13
                          <br />
                          <span className="grey">선상&nbsp;|</span>&nbsp;전남
                          진도군 27km
                        </p>
                        <div className="card-price">
                          <small className="orange">실시간예약</small>
                          <h5>
                            <strong>40,000</strong>
                            <small>원~</small>
                          </h5>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </a>
              <hr />
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
