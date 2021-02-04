import React from "react";
import { inject, observer } from "mobx-react";

export default inject("DataStore")(
  observer(
    ({
      data: {
        profileImage,
        nickName,
        address,
        createdDate,
        title,
        contents,
        fileList = [],
        likeCount = 0,
        commentCount = 0,
        scrapCount = 0,
        isLikeTo = false,
        isScraped = false,
      },
      data,
      showLikeIcon = false,
      showCommentIcon = false,
      showScrapIcon = false,
      onClick,
      onClickProfile,
      onClickLike,
      onClickComment,
      onClickScrap,
    }) => {
      return (
        <div className="container nopadding">
          <div className="row-story-col ">
            <div className="col">
              <a onClick={() => (onClickProfile ? onClickProfile(data) : null)}>
                <figure>
                  <img src={profileImage} alt="" />
                </figure>
              </a>
            </div>
            <div className="col">
              <a onClick={() => (onClickProfile ? onClickProfile(data) : null)}>
                <h5>{nickName}</h5>
                <p>
                  <img
                    src="/assets/cust/img/svg/icon-map-grey.svg"
                    alt=""
                    className="vam"
                  />
                  <small className="grey">{address}</small>
                </p>
              </a>
            </div>
            <div className="col">
              <small className="grey">{createdDate.latest()}</small>
            </div>
          </div>
          <div className="row-story">
            <a onClick={() => (onClick ? onClick(data) : null)}>
              <h6>{title}</h6>
              <p>
                {contents}…{" "}
                <a
                  onClick={() => (onClick ? onClick(data) : null)}
                  className="grey"
                >
                  더보기
                </a>
              </p>
            </a>
            <p className="clearfix-sm"></p>
            {fileList && fileList.length > 0 && (
              <div
                id="carousel-sub"
                className="carousel slide"
                data-ride="carousel"
                onClick={() => (onClick ? onClick(data) : null)}
              >
                <div className="carouselwrap w-full">
                  <ol className="carousel-indicators">
                    {fileList &&
                      fileList.map((data, index) => (
                        <li
                          key={index}
                          data-target="#carousel-sub"
                          data-slide-to={index}
                          className={index === 0 ? "active" : ""}
                        ></li>
                      ))}
                  </ol>
                  <div className="carousel-inner">
                    {fileList &&
                      fileList.map((data, index) => (
                        <div
                          key={index}
                          className={
                            "carousel-item" + (index === 0 ? " active" : "")
                          }
                        >
                          <img src={data} className="d-block w-100" alt="" />
                        </div>
                      ))}
                  </div>
                </div>
              </div>
            )}
          </div>
          <div className="row no-gutters">
            <div className="col-6">
              <small>좋아요 {Intl.NumberFormat().format(likeCount)}개</small>
            </div>
            <div className="col-6 text-right">
              <small>
                댓글 {Intl.NumberFormat().format(commentCount)} &nbsp;&nbsp;
                스크랩 {Intl.NumberFormat().format(scrapCount)}
              </small>
            </div>
          </div>
          <hr />
          <nav className="nav nav-pills nav-comment nav-justified">
            {showLikeIcon && (
              <a
                className={"nav-link" + (isLikeTo ? " active" : "")}
                onClick={() => (onClickLike ? onClickLike(data) : null)}
              >
                <span className="icon icon-good"></span>좋아요
              </a>
            )}
            {showCommentIcon && (
              <a
                className="nav-link"
                onClick={() => (onClickComment ? onClickComment(data) : null)}
              >
                <span className="icon icon-comment"></span>댓글쓰기
              </a>
            )}
            {showScrapIcon && (
              <a
                className={"nav-link" + (isScraped ? " active" : "")}
                onClick={() => (onClickScrap ? onClickScrap(data) : null)}
              >
                <span className="icon icon-scrap"></span>스크랩
              </a>
            )}
          </nav>
          <p className="space"></p>
        </div>
      );
    }
  )
);
