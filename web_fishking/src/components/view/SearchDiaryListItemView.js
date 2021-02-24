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
            </div>
          </a>
          <hr />
        </React.Fragment>
      );
    }
  )
);
