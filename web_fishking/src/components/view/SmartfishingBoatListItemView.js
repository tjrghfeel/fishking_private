import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: {
        fishingType,
        profileImage,
        shipName,
        address,
        createDate,
        hasCamera,
      },
      data,
      onClick,
    }) => {
      return (
        <div className="container nopadding mt-3">
          <a onClick={() => (onClick ? onClick(data) : null)}>
            <div className="card card-sm">
              <div className="row no-gutters">
                <div className="cardimgWrap">
                  <img src={profileImage} className="img-fluid" alt="" />
                </div>
                <div className="cardInfoWrap">
                  <div className="card-body pt-0">
                    <div className="row no-gutters d-flex align-items-center">
                      <div className="col-9">
                        <h6>
                          <span className="tag">{fishingType}</span> {shipName}
                        </h6>
                        <p>
                          {address}
                          <br />
                          등록일: {createDate?.substr(0, 10)}
                        </p>
                      </div>
                      <div className="col-3 text-right">
                        <small className="grey">녹화영상:</small>{" "}
                        <strong className={"large" + (hasCamera ? " red" : "")}>
                          {hasCamera ? "유" : "무"}
                        </strong>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </a>
          <hr className="full mt-2 mb-3" />
        </div>
      );
    }
  )
);
