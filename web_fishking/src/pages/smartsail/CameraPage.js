import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, SmartSailMainTab },
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
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout
              title={"상품관리"}
              customButton={
                <a className="fixed-top-right new">
                  <strong>N</strong>
                  <img
                    src="/assets/smartsail/img/svg/icon-alarm.svg"
                    alt="알림내역"
                  />
                  <span className="sr-only">알림내역</span>
                </a>
              }
              showBackIcon={true}
            />
            <SmartSailMainTab activeIndex={2} />

            <div className="filterlinewrap container nopadding">
              <ul className="nav nav-tabs nav-filter">
                <li>
                  <div className="input-group keyword">
                    <select className="custom-select">
                      <option>선상명</option>
                      <option>업체명</option>
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
                    녹화영상
                  </label>
                  <select className="form-control" id="selPay">
                    <option>녹화영상전체</option>
                    <option>유</option>
                    <option>무</option>
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
              <a onClick={() => PageStore.push(`/camera/add`)}>
                <div className="card card-sm">
                  <div className="row no-gutters">
                    <div className="cardimgWrap">
                      <img
                        src="/assets/smartsail/img/sample/boat1.jpg"
                        className="img-fluid"
                        alt=""
                      />
                    </div>
                    <div className="cardInfoWrap">
                      <div className="card-body pt-0">
                        <div className="row no-gutters d-flex align-items-center">
                          <div className="col-9">
                            <h6>어복황제3호</h6>
                            <p>
                              엔진실 – 카메라 이름
                              <br />
                              <span className="grey">
                                ▷ 707 &nbsp;&nbsp;&nbsp; ♡ 125
                              </span>
                            </p>
                          </div>
                          <div className="col-3 text-right">
                            <small className="grey">녹화영상:</small>{" "}
                            <strong className="large red">유</strong>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </a>
              <hr className="full mt-2 mb-3" />
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
