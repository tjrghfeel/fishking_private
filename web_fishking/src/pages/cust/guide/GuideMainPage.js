import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout },
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
            <NavigationLayout title={"통합가이드"} showBackIcon={true} />

            {/** 리스트 */}
            <div className="container nopadding bg-grey">
              {/*<div className="row no-gutters mt-4">*/}
              {/*  <div className="col-6">*/}
              {/*    <a onClick={() => PageStore.push(`/guide/time`)}>*/}
              {/*      <div className="card-rect">*/}
              {/*        <h6>*/}
              {/*          <img*/}
              {/*            src="/assets/cust/img/svg/icon-guide-time.svg"*/}
              {/*            alt=""*/}
              {/*            className="icon-md"*/}
              {/*          />*/}
              {/*        </h6>*/}
              {/*        <h6>시간·체험</h6>*/}
              {/*        <hr />*/}
              {/*        <small>*/}
              {/*          자세히 보기&nbsp;*/}
              {/*          <img*/}
              {/*            src="/assets/cust/img/svg/icon-arrow.svg"*/}
              {/*            alt=""*/}
              {/*            className="vam"*/}
              {/*          />*/}
              {/*        </small>*/}
              {/*      </div>*/}
              {/*    </a>*/}
              {/*  </div>*/}
              {/*  <div className="col-6">*/}
              {/*    <a onClick={() => PageStore.push(`/guide/all`)}>*/}
              {/*      <div className="card-rect">*/}
              {/*        <h6>*/}
              {/*          <img*/}
              {/*            src="/assets/cust/img/svg/icon-guide-time-all.svg"*/}
              {/*            alt=""*/}
              {/*            className="icon-md"*/}
              {/*          />*/}
              {/*        </h6>*/}
              {/*        <h6>종일·생활</h6>*/}
              {/*        <hr />*/}
              {/*        <small>*/}
              {/*          자세히 보기&nbsp;*/}
              {/*          <img*/}
              {/*            src="/assets/cust/img/svg/icon-arrow.svg"*/}
              {/*            alt=""*/}
              {/*            className="vam"*/}
              {/*          />*/}
              {/*        </small>*/}
              {/*      </div>*/}
              {/*    </a>*/}
              {/*  </div>*/}
              {/*</div>*/}
              <div className="row no-gutters">
                <div className="col-6">
                  <a onClick={() => PageStore.push(`/guide/boat`)}>
                    <div className="card-rect">
                      <h6>
                        <img
                          src="/assets/cust/img/svg/icon-guide-boat.svg"
                          alt=""
                          className="icon-md"
                        />
                      </h6>
                      <h6>선상 낚시</h6>
                      <hr />
                      <small>
                        자세히 보기&nbsp;
                        <img
                          src="/assets/cust/img/svg/icon-arrow.svg"
                          alt=""
                          className="vam"
                        />
                      </small>
                    </div>
                  </a>
                </div>
                <div className="col-6">
                  <a onClick={() => PageStore.push(`/guide/rock`)}>
                    <div className="card-rect">
                      <h6>
                        <img
                          src="/assets/cust/img/svg/icon-guide-rock.svg"
                          alt=""
                          className="icon-md"
                        />
                      </h6>
                      <h6>갯바위 낚시</h6>
                      <hr />
                      <small>
                        자세히 보기&nbsp;
                        <img
                          src="/assets/cust/img/svg/icon-arrow.svg"
                          alt=""
                          className="vam"
                        />
                      </small>
                    </div>
                  </a>
                </div>
              </div>
              <div className="row no-gutters">
                <div className="col-6">
                  <a onClick={() => PageStore.push(`/guide/smart`)}>
                    <div className="card-rect">
                      <h6>
                        <img
                          src="/assets/cust/img/svg/icon-guide-smart.svg"
                          alt=""
                          className="icon-md"
                        />
                      </h6>
                      <h6>스마트 인검</h6>
                      <hr />
                      <small>
                        자세히 보기&nbsp;
                        <img
                          src="/assets/cust/img/svg/icon-arrow.svg"
                          alt=""
                          className="vam"
                        />
                      </small>
                    </div>
                  </a>
                </div>
                <div className="col-6">
                  <a onClick={() => PageStore.push(`/guide/live`)}>
                    <div className="card-rect">
                      <h6>
                        <img
                          src="/assets/cust/img/svg/icon-guide-live.svg"
                          alt=""
                          className="icon-md"
                        />
                      </h6>
                      <h6>실시간 조황</h6>
                      <hr />
                      <small>
                        자세히 보기&nbsp;
                        <img
                          src="/assets/cust/img/svg/icon-arrow.svg"
                          alt=""
                          className="vam"
                        />
                      </small>
                    </div>
                  </a>
                </div>
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
