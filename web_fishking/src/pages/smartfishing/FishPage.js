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
            <NavigationLayout title={"조황관리"} showBackIcon={true} />
            <SmartFishingMainTab activeIndex={2} />

            <div className="filterlinewrap container nopadding">
              <ul className="nav nav-tabs nav-filter">
                <li>
                  <div className="input-group keyword">
                    <select className="custom-select">
                      <option>제목</option>
                      <option>내용</option>
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
                    선상명
                  </label>
                  <select className="form-control" id="selPay">
                    <option>전체</option>
                    <option>어복황제3호</option>
                    <option>어복호</option>
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

            <div className="container nopadding">
              <div className="row-story-col ">
                <div className="col">
                  <a>
                    <figure>
                      <img
                        src="/assets/smartfishing/img/sample/boat1.jpg"
                        alt=""
                      />
                    </figure>
                  </a>
                </div>
                <div className="col">
                  <a>
                    <h5>어복황제3호</h5>
                    <p>
                      <img
                        src="/assets/smartfishing/img/svg/icon-map-grey.svg"
                        alt=""
                        className="vam"
                      />
                      <small className="grey">경북 포항시 남구</small>
                    </p>
                  </a>
                </div>
                <div className="col">
                  <small className="grey">17분전</small>
                </div>
              </div>
              <div className="row-story">
                <a>
                  <h6>전남 진도 어복황제3호 8월 17일 오전시간 배조황!!</h6>
                  <p>
                    태풍바비가 오기전에 수온이 25도 였는데, 태풍지나가면서
                    수온이 27.5도…남쪽에서 난류도 같이…{" "}
                    <a className="grey">더보기</a>
                  </p>
                </a>
                <p className="clearfix-sm"></p>
                <div
                  id="carousel-sub"
                  className="carousel slide"
                  data-ride="carousel"
                >
                  <div className="carouselwrap w-full">
                    <ol className="carousel-indicators">
                      <li
                        data-target="#carousel-sub"
                        data-slide-to="0"
                        className="active"
                      ></li>
                      <li data-target="#carousel-sub" data-slide-to="1"></li>
                      <li data-target="#carousel-sub" data-slide-to="2"></li>
                    </ol>
                    <div className="carousel-inner">
                      <div className="carousel-item active">
                        <img
                          src="/assets/smartfishing/img/sample/photo1.jpg"
                          className="d-block w-100"
                          alt=""
                        />
                      </div>
                      <div className="carousel-item">
                        <img
                          src="/assets/smartfishing/img/sample/photo2.jpg"
                          className="d-block w-100"
                          alt=""
                        />
                      </div>
                      <div className="carousel-item">
                        <img
                          src="/assets/smartfishing/img/sample/photo3.jpg"
                          className="d-block w-100"
                          alt=""
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div className="row no-gutters">
                <div className="col-6">
                  <small>좋아요 1개</small>
                </div>
                <div className="col-6 text-right">
                  <small>댓글 0 &nbsp;&nbsp; 스크랩 0</small>
                </div>
              </div>
              <hr />
              <nav className="nav nav-pills nav-comment nav-justified">
                <a className="nav-link active">
                  <span className="icon icon-good"></span>좋아요
                </a>
                <a className="nav-link">
                  <span className="icon icon-comment"></span>댓글쓰기
                </a>
                <a className="nav-link">
                  <span className="icon icon-scrap"></span>스크랩
                </a>
              </nav>
              <p className="space"></p>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
