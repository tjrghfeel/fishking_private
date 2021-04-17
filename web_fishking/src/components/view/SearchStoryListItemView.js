import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: {
        imageUrl,
        title,
        contents,
        profileImageUrl,
        nickName,
        createdDate,
        fishingType,
        species,
        loves = 0,
        comments = 0,
      },
      data,
      onClick,
    }) => {
      return (
        <React.Fragment>
          <a onClick={() => (onClick ? onClick(data) : null)}>
            <div className="card card-md">
              <div className="row no-gutters">
                <div className="cardphotoWrap">
                  <img src={imageUrl} className="img-fluid" alt="" />
                </div>
                <div className="cardTextWrap">
                  <div className="card-body">
                    <h6>{title}</h6>
                    <p>{contents}</p>
                    <div className="media">
                      <img
                        src={profileImageUrl}
                        className="profile-thumb-xs align-self-center mr-1"
                        alt="profile"
                      />
                      <div className="media-body">
                        <p className="mb-0 text-secondary">
                          {nickName} | {createdDate}
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <hr className="full mt-2 mb-3" />
              <div className="row no-gutters d-flex align-items-center">
                <div className="col-4">
                  <span className="tag">{fishingType}</span> {species}
                </div>
                <div className="col-8 text-right">
                  <ul className="info-num">
                    <li>
                      <img
                        src="/assets/cust/img/svg/icon-view-blue.svg"
                        alt=""
                        className="vam"
                      />{" "}
                      100
                    </li>
                    <li>
                      <img
                        src="/assets/cust/img/svg/icon-good-blue.svg"
                        alt=""
                        className="vam"
                      />{" "}
                      {Intl.NumberFormat().format(loves)}
                    </li>
                    <li>
                      <img
                        src="/assets/cust/img/svg/icon-comment-blue.svg"
                        alt=""
                        className="vam"
                      />{" "}
                      {Intl.NumberFormat().format(comments)}
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </a>
          <p className="space mt-3 mb-3"></p>
        </React.Fragment>
      );
    }
  )
);
