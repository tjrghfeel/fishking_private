import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: {
        profileImage,
        nickName,
        fishingDate,
        goodsFishSpecies,
        meridiem,
        distance,
        fishingTideTime,
        tasteByReview = 0,
        serviceByReview = 0,
        cleanByReview = 0,
        content,
        fileList = [],
      },
    }) => {
      return (
        <div className="container nopadding">
          <div className="row-story-col">
            <div className="col">
              <a href="profile-user.html">
                <figure>
                  <img src={profileImage} alt="" />
                </figure>
              </a>
            </div>
            <div className="col">
              <a href="profile-user.html">
                <h5>{nickName}</h5>
                <p>
                  <small className="grey">
                    {fishingDate.substr(0, 4)}년 {fishingDate.substr(4, 2)}월{" "}
                    {fishingDate.substr(6, 2)}일 이용
                  </small>
                </p>
              </a>
            </div>
            <div className="col"></div>
          </div>
          <div className="row-story">
            <p>
              <span className="tag">
                {goodsFishSpecies}({meridiem === "0" ? "오전" : "오후"})
              </span>{" "}
              <span className="grey">
                {" "}
                {distance && <React.Fragment>{distance}m</React.Fragment>}
                {fishingTideTime}
              </span>
            </p>
            <p>
              <div
                className="rateit float-left"
                data-rateit-value="4.2"
                data-rateit-ispreset="true"
                data-rateit-readonly="true"
                data-rateit-starwidth="16"
                data-rateit-starheight="16"
              ></div>
              <span className="grey">손맛</span>{" "}
              <img src="/cust/assets/img/star1.png" alt="" className="vam" />{" "}
              {tasteByReview.toFixed(1)}
              &nbsp;&nbsp;
              <span className="grey">서비스</span>{" "}
              <img src="/cust/assets/img/star1.png" alt="" className="vam" />{" "}
              {serviceByReview.toFixed(1)}
              &nbsp;&nbsp;
              <span className="grey">청결도</span>{" "}
              <img src="/cust/assets/img/star1.png" alt="" className="vam" />{" "}
              {cleanByReview.toFixed(1)}
            </p>
            <hr className="clearfix" />
            <p>{content}</p>
            <p className="clearfix-sm"></p>

            {fileList.length > 0 && (
              <div
                id="carousel-sub"
                className="carousel slide"
                data-ride="carousel"
              >
                <div className="carouselwrap w-full">
                  <ol className="carousel-indicators">
                    {fileList.map((data, index) => (
                      <li
                        key={index}
                        className={index === 0 ? "active" : ""}
                        data-target="#carousel-sub"
                        data-slide-to={index}
                      ></li>
                    ))}
                  </ol>
                  <div className="carousel-inner">
                    {fileList.map((data, index) => (
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
          <p className="space"></p>
        </div>
      );
    }
  )
);
