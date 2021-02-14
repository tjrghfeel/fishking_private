import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: {
        imageUrlList = [null],
        title,
        content,
        profileImage,
        nickName,
        createdAt = "",
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
                  <img src={imageUrlList[0]} className="img-fluid" alt="" />
                </div>
                <div className="cardTextWrap">
                  <div className="card-body">
                    <a
                      href="#none"
                      className="ellipsis"
                      data-toggle="modal"
                      data-target="#ellipsisModal"
                    >
                      <img
                        src="/assets/cust/img/svg/icon-ellipsis.svg"
                        alt="메뉴더보기"
                      />
                    </a>
                    <h6>{title}</h6>
                    <p>{content}</p>
                    <div className="media">
                      <img
                        src={profileImage}
                        className="profile-thumb-xs align-self-center mr-1"
                        alt="profile"
                      />
                      <div className="media-body">
                        <p className="mb-0 text-secondary">
                          {nickName} | {createdAt.substr(0, 10)}
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </a>
          <hr />
        </React.Fragment>
      );
    }
  )
);
