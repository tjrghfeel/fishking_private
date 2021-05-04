import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      data: {
        shipName,
        profileImage,
        species = [],
        speciesCount = 0,
        goodsList = [],
      },
      data,
      onClick,
    }) => {
      return (
        <div className="container nopadding mt-3">
          <a>
            <div className="card card-sm">
              <div className="row no-gutters">
                <div className="cardimgWrap">
                  <img src={profileImage} className="img-fluid" alt="" />
                </div>
                <div className="cardInfoWrap">
                  <div className="card-body">
                    <h6>{shipName}</h6>
                    <p>
                      <strong className="text-primary">
                        {species.slice(0, 2).map((text, index) => (
                          <React.Fragment key={index}>
                            {(index > 0 ? ", " : "") + text}
                          </React.Fragment>
                        ))}
                      </strong>{" "}
                      <img
                        src="/assets/smartfishing/img/fish/fish_icon_18.svg"
                        alt=""
                        className="fish-cate"
                      />
                      {Intl.NumberFormat().format(speciesCount)}
                    </p>
                  </div>
                </div>
              </div>
              {goodsList?.map((data, index) => (
                <React.Fragment key={index}>
                  <hr className="full mt-2 mb-3" />
                  <div
                    className="row no-gutters d-flex align-items-center"
                    onClick={() => (onClick ? onClick(data) : null)}
                  >
                    <div className="col-6">
                      <span className="tag-orange">
                        출발 {data["fishingStartTime"]}~
                      </span>
                      <br/>
                      <span className="tag-grey">
                        최소
                        {Intl.NumberFormat().format(data["minPersonnel"] || 0)}
                        명 ~ 최대
                        {Intl.NumberFormat().format(data["maxPersonnel"] || 0)}
                        명
                      </span>
                      <br/>
                      <span className="tag-grey">
                        위치선정 {data["select"]}
                      </span>{" "}
                      <span className="tag-grey">
                        {data["confirm"]}
                      </span>
                      <br/>
                      <span className="tag-grey">
                        추가운행 {data["extra"]}
                      </span>{" "}
                      <span className="tag-grey">
                        ~{data["endDate"]}
                      </span>
                      <h6>{data["name"]}</h6>
                    </div>
                    <div className="col-5 text-right pr-4">
                      <h6>
                        {Intl.NumberFormat().format(data["amount"] || 0)}원
                      </h6>
                    </div>
                    <div className="col-1 text-right border-left">
                      <small className="grey">{data["status"]}</small>
                    </div>
                  </div>
                </React.Fragment>
              ))}
            </div>
          </a>
          <p className="space mt-3 mb-3"></p>
        </div>
      );
    }
  )
);
