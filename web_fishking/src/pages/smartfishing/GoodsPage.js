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
            <NavigationLayout title={"상품관리"} showBackIcon={true} />
            <SmartFishingMainTab activeIndex={3} />

            <div className="filterlinewrap container nopadding">
              <ul className="nav nav-tabs nav-filter">
                <li>
                  <div className="input-group keyword">
                    <select className="custom-select">
                      <option>상품명</option>
                      <option>업체명</option>
                      <option>선상명</option>
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
                    상태
                  </label>
                  <select className="form-control" id="selPay">
                    <option>상태전체</option>
                    <option>판매</option>
                    <option>판매</option>
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

            <div className="container nopadding mt-3">
              <a>
                <div className="card card-sm">
                  <div className="row no-gutters">
                    <div className="cardimgWrap">
                      <img
                        src="/assets/smartfishing/img/sample/boat1.jpg"
                        className="img-fluid"
                        alt=""
                      />
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
                      </div>
                    </div>
                  </div>
                  <hr className="full mt-2 mb-3" />
                  <div className="row no-gutters d-flex align-items-center">
                    <div className="col-6">
                      <span className="tag-orange">출발 05:00~</span>{" "}
                      <span className="tag-grey">최소7명 ~ 최대20명</span>
                      <h6>우럭 (오전)</h6>
                    </div>
                    <div className="col-5 text-right pr-4">
                      <h6>40,000원</h6>
                    </div>
                    <div className="col-1 text-right border-left">
                      <small className="grey">판매</small>
                    </div>
                  </div>
                  <hr className="full mt-2 mb-3" />
                  <div className="row no-gutters d-flex align-items-center">
                    <div className="col-6">
                      <span className="tag-orange">출발 13:00~</span>{" "}
                      <span className="tag-grey">최소7명 ~ 최대20명</span>
                      <h6>우럭 (오후)</h6>
                    </div>
                    <div className="col-5 text-right pr-4">
                      <h6>40,000원</h6>
                    </div>
                    <div className="col-1 text-right border-left">
                      <small className="grey">대기</small>
                    </div>
                  </div>
                </div>
              </a>
              <p className="space mt-3 mb-3"></p>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
