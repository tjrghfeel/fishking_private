import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, SmartFishingMainTab },
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
        return (
          <React.Fragment>
            <NavigationLayout title={"정산관리"} showBackIcon={true} />
            <SmartFishingMainTab activeIndex={5} />

            <div className="filterlinewrap container nopadding">
              <ul className="nav nav-tabs nav-filter">
                <li>
                  <div className="input-group keyword">
                    <select className="custom-select">
                      <option>선상명</option>
                      <option>업체명</option>
                    </select>
                    <input
                      type="text"
                      className="form-control"
                      placeholder="검색어 입력"
                      value=""
                    />
                  </div>
                </li>
                <li>
                  <label htmlFor="selPay" className="sr-only">
                    녹화영상
                  </label>
                  <select className="form-control" id="selPay">
                    <option>정산상태</option>
                    <option>정산완료</option>
                    <option>정산대기</option>
                  </select>
                </li>
                <li className="full">
                  <p>
                    <a className="btn btn-primary btn-sm">검색</a>
                    <a className="btn btn-grey btn-sm">초기화</a>
                  </p>
                </li>
              </ul>
            </div>
            <p className="clearfix"></p>

            <div className="mainAdWrap text-center">
              <h5 className="mb-2">
                <span className="text-primary">
                  <img
                    src="/assets/smartfishing/img/svg/icon-paid.svg"
                    alt=""
                    className="vam"
                  />
                  &nbsp;정산 예정 금액 <small>(2020-11-25)</small>
                </span>
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
                    <div className="row no-gutters">
                      <div className="col-12">
                        <div className="card-round-box pt-3 pb-2 mt-1 ml-3 mr-3">
                          <h6>어복황제7호</h6>
                          <h1>
                            1,700,000<small className="grey">원</small>
                          </h1>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div className="carousel-item">
                    <div className="row no-gutters">
                      <div className="col-12">
                        <div className="card-round-box pt-3 pb-2 mt-1 ml-3 mr-3">
                          <h6>어복호</h6>
                          <h1>
                            2,700,000<small className="grey">원</small>
                          </h1>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div className="carousel-item">
                    <div className="row no-gutters">
                      <div className="col-12">
                        <div className="card-round-box pt-3 pb-2 mt-1 ml-3 mr-3">
                          <h6>어복황제3호</h6>
                          <h1>
                            7,700,000<small className="grey">원</small>
                          </h1>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <p className="space mt-2"></p>

            <h5 className="text-center">2020-10-25</h5>
            <div className="container nopadding">
              <a>
                <div className="card card-sm">
                  <div className="row no-gutters d-flex align-items-center">
                    <div className="col-3">
                      <strong>어복황제7호</strong>
                    </div>
                    <div className="col-6">
                      정산금액: <strong>2,700,000원</strong>
                      <br />
                      예약금액: 3,200,000원
                      <br />
                      취소금액: <strong className="red">-500,000</strong>원
                    </div>
                    <div className="col-3 text-right">
                      <span className="status relative status2">정산대기</span>
                    </div>
                  </div>
                </div>
              </a>
              <hr className="full mt-3 mb-3" />
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
