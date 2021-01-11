import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(({ data, onClick }) => {
    return (
      <>
        <a onClick={() => (onClick ? onClick(data) : null)}>
          <div className="card card-sm">
            <div className="row no-gutters">
              <div className="cardimgWrap">
                <img
                  src="/assets/img/sample/boat1.jpg"
                  className="img-fluid"
                  alt=""
                />
                <span className="play">
                  <img src="/assets/img/svg/live-play.svg" alt="" />
                </span>
                <span className="play-time">20:17</span>
              </div>
              <div className="cardInfoWrap">
                <div className="card-body">
                  <h6>어복황제3호</h6>
                  <p>
                    <strong className="text-primary">쭈꾸미, 우럭</strong>{" "}
                    <img
                      src="/assets/img/fish/fish_icon_02.svg"
                      alt=""
                      className="fish-cate"
                    />
                    13
                    <br />
                    <span className="grey">선상&nbsp;|</span>&nbsp;전남 진도군
                    27km
                    <br />
                  </p>
                  <div className="card-price">
                    <small className="orange">실시간예약</small>
                    <h5>
                      <strong>40,000</strong>
                      <small>원~</small>
                    </h5>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </a>
        <hr />
      </>
    );
  })
);
