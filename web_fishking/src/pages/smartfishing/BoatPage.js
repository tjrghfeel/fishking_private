import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, SmartFishingMainTab },
} = Components;

export default inject("PageStore")(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <NavigationLayout title={"선상관리"} showBackIcon={true} />
            <SmartFishingMainTab activeIndex={4} />

            <div className="filterlinewrap container nopadding">
              <ul className="nav nav-tabs nav-filter">
                <li>
                  <div className="input-group keyword">
                    <select className="custom-select">
                      <option>업체명</option>
                      <option>선상명</option>
                      <option>선주</option>
                    </select>
                    <input
                      type="text"
                      className="form-control"
                      placeholder="검색어 입력"
                      value=""
                    />
                  </div>
                </li>
                <li>
                  <label htmlFor="selPay" className="sr-only">
                    구분선택
                  </label>
                  <select className="form-control" id="selPay">
                    <option>전체</option>
                    <option>선상</option>
                    <option>갯바위</option>
                  </select>
                </li>
                <li className="full">
                  <p>
                    <a className="btn btn-primary btn-sm">검색</a>
                    <a className="btn btn-grey btn-sm">초기화</a>
                  </p>
                </li>
              </ul>
            </div>
            <p className="clearfix"></p>

            <a>
              <div className="card card-sm">
                <div className="row no-gutters">
                  <div className="cardimgWrap">
                    <img
                      src="/assets/smartfishing/img/sample/boat3.jpg"
                      className="img-fluid"
                      alt=""
                    />
                    <span className="play">
                      <img
                        src="/assets/smartfishing/img/svg/live-play.svg"
                        alt=""
                      />
                    </span>
                    <span className="play-time">20:17</span>
                  </div>
                  <div className="cardInfoWrap">
                    <div className="card-body">
                      <h6>어복황제3호</h6>
                      <p>
                        <strong className="text-primary">쭈꾸미, 우럭</strong>{" "}
                        <img
                          src="/assets/smartfishing/img/fish/fish_icon_02.svg"
                          alt=""
                          className="fish-cate"
                        />
                        13
                        <br />
                        전남 진도군 27km
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
          </React.Fragment>
        );
      }
    }
  )
);
