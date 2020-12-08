import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(() => {
    return (
      <a href="boat-detail.html">
        <div className="card card-lg">
          <div className="card-img">
            <img
              src="./assets/img/sample/boat1.jpg"
              className="card-img-top img-fluid"
              alt=""
            />
            <span className="play">
              <img src="./assets/img/svg/live-play-big.svg" alt="" />
            </span>
            <span className="play-live">LIVE</span>
          </div>
          <div className="card-body">
            <ul className="tag">
              <li>광어</li>
              <li>주꾸미</li>
              <li>우럭</li>
            </ul>
            <div className="card-info">
              <h5 className="card-title">어복황제1호</h5>
              <p>전남 진도군 27km</p>
              <div className="card-price">
                <small className="orange">실시간예약</small>
                <h5>
                  <strong>40,000</strong>
                  <small>원~</small>
                </h5>
              </div>
            </div>
            <hr />
            <ul className="notice">
              <li className="icon-notice">
                7~8월 어린이(13세 이하) 선비 무료!
              </li>
              <li className="icon-event">
                9~11월 쭈꾸미/우럭 할인 이벤트 진행
              </li>
            </ul>
          </div>
        </div>
      </a>
    );
  })
);
