/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";
import CarouselList01 from "./CarouselList01";

export default inject()(
  observer(({ list }) => {
    return (
      <>
        {list &&
          list.map((data, index) => {
            const {
              profileImage,
              membername,
              location,
              regDate,
              title,
              contents,
              images,
              likeCount = 0,
              commentCount = 0,
              scrapCount = 0,
              isLike = false,
              isScrap = false,
            } = data;
            return (
              <div key={index} className="container nopadding">
                <div className="row-story-col ">
                  <div className="col">
                    <a>
                      <figure>
                        <img src={profileImage} alt="" />
                      </figure>
                    </a>
                  </div>
                  <div className="col">
                    <a>
                      <h5>{membername}</h5>
                      <p>
                        <img
                          src="/assets/img/svg/icon-map-grey.svg"
                          alt=""
                          className="vam"
                        />
                        <small className="grey">{location}</small>
                      </p>
                    </a>
                  </div>
                  <div className="col">
                    <small className="grey">{regDate}</small>
                  </div>
                </div>
                <div className="row-story">
                  <a href="story-detail.html">
                    <h6>{title}</h6>
                    <p>
                      {contents}… <span className="grey">더보기</span>
                    </p>
                  </a>
                  <p className="clearfix-sm"></p>

                  <CarouselList01
                    carouselType={"ListItem03"}
                    list={images}
                    isWrapped={true}
                  />
                </div>
                <div className="row no-gutters">
                  <div className="col-6">
                    <small>
                      좋아요 {Intl.NumberFormat().format(likeCount)}개
                    </small>
                  </div>
                  <div className="col-6 text-right">
                    <small>
                      댓글 {Intl.NumberFormat().format(commentCount)}{" "}
                      &nbsp;&nbsp; 스크랩{" "}
                      {Intl.NumberFormat().format(scrapCount)}
                    </small>
                  </div>
                </div>
                <hr />
                <nav className="nav nav-pills nav-comment nav-justified">
                  <a
                    className={
                      "nav-link" + (isLike ? " ".concat("active") : "")
                    }
                  >
                    <span className="icon icon-good"></span>좋아요
                  </a>
                  <a className="nav-link">
                    <span className="icon icon-comment"></span>댓글쓰기
                  </a>
                  <a
                    className={
                      "nav-link" + (isScrap ? " ".concat("active") : "")
                    }
                  >
                    <span className="icon icon-scrap"></span>스크랩
                  </a>
                </nav>
                <p className="space"></p>
              </div>
            );
          })}
      </>
    );
  })
);
