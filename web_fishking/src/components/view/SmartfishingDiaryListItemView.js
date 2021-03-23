import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ data: {}, data, onClick }) => {
    return (
      <div className="container nopadding">
        <div className="row-story-col ">
          <div className="col">
            <a>
              <figure>
                <img src="/assets/smartfishing/img/sample/boat1.jpg" alt="" />
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
              태풍바비가 오기전에 수온이 25도 였는데, 태풍지나가면서 수온이
              27.5도…남쪽에서 난류도 같이… <a className="grey">더보기</a>
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
    );
  })
);
