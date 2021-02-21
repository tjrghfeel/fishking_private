import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    ({
      id = "",
      data: {
        shipImageFileUrl,
        shipName,
        fishSpecies = [],
        fishSpeciesCount = 0,
        sido = "",
        sigungu = "",
        lowPrice = 0,
      },
      data,
      onClick,
    }) => {
      return (
        <div
          className="modal show modal-full-btm"
          id={id}
          tabIndex="-1"
          aria-labelledby={id.concat("Label")}
          aria-hidden="true"
        >
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-body">
                <a data-dismiss="modal" className="float-top-right">
                  <img src="/assets/cust/img/svg/icon_close_grey.svg" alt="" />
                </a>
                <a
                  onClick={() => {
                    if (onClick) onClick(data);
                  }}
                >
                  <div className="card card-sm">
                    <div className="row no-gutters d-flex align-items-center">
                      <div className="cardimgWrap">
                        <img
                          src={shipImageFileUrl}
                          className="img-fluid"
                          alt=""
                        />
                        {/*<span className="play">*/}
                        {/*  <img src="/assets/cust/img/svg/live-play.svg" alt="" />*/}
                        {/*</span>*/}
                        {/*<span className="play-live">LIVE</span>*/}
                      </div>
                      <div className="cardInfoWrap">
                        <div className="card-body">
                          <h6>{shipName}</h6>
                          <p>
                            <strong className="text-primary">
                              {fishSpecies.length > 2 &&
                                fishSpecies
                                  .splice(0, 2)
                                  .map((data, index) => (
                                    <React.Fragment key={index}>
                                      {data.codeName.concat(" ")}
                                    </React.Fragment>
                                  ))}
                            </strong>{" "}
                            <img
                              src="/assets/cust/img/fish/fish_icon_02.svg"
                              alt=""
                              className="fish-cate"
                            />
                            {Intl.NumberFormat().format(fishSpeciesCount)}
                            <br />
                            {sido.concat(" ").concat(sigungu)}
                          </p>
                          <div className="card-price">
                            {/*<small className="orange">실시간예약</small>*/}
                            <h5>
                              <strong>
                                {Intl.NumberFormat().format(lowPrice)}
                              </strong>
                              <small>원~</small>
                            </h5>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </a>
              </div>
            </div>
          </div>
        </div>
      );
    }
  )
);
