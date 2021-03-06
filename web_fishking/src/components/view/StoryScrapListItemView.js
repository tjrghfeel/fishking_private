import React, { useState, useEffect } from "react";
import { inject, observer } from "mobx-react";

export default inject("DataStore")(
  observer(
    ({
      DataStore: { getEnumValueByIndex },
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
        isLikeTo,
        fishingType = 0,
      },
      data,
      onClick,
      onClickProfile,
      onClickLike,
      onClickComment,
    }) => {
      const [timeString, setTimeString] = useState("");
      const [isRealtime, setIsRealtime] = useState(false);
      const [fishingTypeName, setFishingTypeName] = useState("");
      useEffect(() => {
        (async () => {
          const createDateTimeMillis = createdDate.betweenTime();
          if (createDateTimeMillis <= 1000 * 60 * 60 * 2) {
            setIsRealtime(true);
          }
          const fishingTypeEnum = await getEnumValueByIndex(
            "fishingType",
            fishingType
          );
          if (fishingTypeEnum) {
            setFishingTypeName(fishingTypeEnum.value);
          }
        })();
      }, [
        setTimeString,
        createdDate,
        setIsRealtime,
        fishingType,
        setFishingTypeName,
      ]);
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
              <small className="grey">{timeString}</small>
            </div>
          </div>
          <div className="row-story">
            {isRealtime && (
              <React.Fragment>
                <span className="tag-orange">???????????????</span>{" "}
              </React.Fragment>
            )}
            {fishingTypeName !== "" && <span className="tag">????????????</span>}
            <a onClick={() => (onClick ? onClick(data) : null)}>
              <h6>{title}</h6>
              <p>
                {contents}???{" "}
                <a
                  onClick={() => (onClick ? onClick(data) : null)}
                  className="grey"
                >
                  ?????????
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
              <small>????????? {Intl.NumberFormat().format(likeCount)}???</small>
            </div>
            <div className="col-6 text-right">
              <small>
                ?????? {Intl.NumberFormat().format(commentCount)} &nbsp;&nbsp;
                ????????? {Intl.NumberFormat().format(scrapCount)}
              </small>
            </div>
          </div>
          <hr />
          <nav className="nav nav-pills nav-comment nav-justified">
            <a
              className={"nav-link" + (isLikeTo ? " active" : "")}
              onClick={() => (onClickLike ? onClickLike(data) : null)}
            >
              <span className="icon icon-good"></span>?????????
            </a>
            <a
              className="nav-link"
              onClick={() => (onClickComment ? onClickComment(data) : null)}
            >
              <span className="icon icon-comment"></span>????????????
            </a>
          </nav>
          <p className="space"></p>
        </div>
      );
    }
  )
);
