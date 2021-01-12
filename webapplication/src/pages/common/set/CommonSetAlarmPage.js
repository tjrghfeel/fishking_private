import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  Common: {
    Layout: { Navigation },
  },
} = Components;

export default inject("PageStore")(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      onChangeOption1 = async (checked) => {};
      onChangeOption2 = async (checked) => {};
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            {/** Navigation */}
            <Navigation title={"알림 설정"} showBackIcon={true} />

            <div className="container nopadding mt-3">
              <div className="row no-gutters mt-3 mb-2">
                <div className="col">
                  <strong>내 글 알림</strong>
                  <br />
                  <small className="grey">
                    작성한 내글에 대한 댓글 알림 입니다.
                  </small>
                </div>
                <div className="col">
                  <nav>
                    <div
                      className="nav nav-tabs btn-set"
                      id="nav-tab"
                      role="tablist"
                    >
                      <a
                        className="nav-link active btn btn-on"
                        id="nav-home-tab"
                        data-toggle="tab"
                        role="tab"
                        aria-controls="nav-on"
                        aria-selected="true"
                        onClick={() => this.onChangeOption1(true)}
                      >
                        ON
                      </a>
                      <a
                        className="nav-link btn btn-off"
                        id="nav-profile-tab"
                        data-toggle="tab"
                        role="tab"
                        aria-controls="nav-off"
                        aria-selected="false"
                        onClick={() => this.onChangeOption1(false)}
                      >
                        OFF
                      </a>
                    </div>
                  </nav>
                </div>
              </div>
              <p className="space"></p>
            </div>

            <div className="container nopadding mt-1">
              <div className="row no-gutters mt-3 mb-2">
                <div className="col">
                  <strong>혜택 알림</strong>
                  <br />
                  <small className="grey">
                    할인, 쿠폰 등 이벤트 혜택 알림 입니다.
                  </small>
                </div>
                <div className="col">
                  <nav>
                    <div
                      className="nav nav-tabs btn-set"
                      id="nav-tab"
                      role="tablist"
                    >
                      <a
                        className="nav-link active btn btn-on"
                        id="nav-home-tab"
                        data-toggle="tab"
                        role="tab"
                        aria-controls="nav-on"
                        aria-selected="true"
                        onClick={() => this.onChangeOption2(true)}
                      >
                        ON
                      </a>
                      <a
                        className="nav-link btn btn-off"
                        id="nav-profile-tab"
                        data-toggle="tab"
                        role="tab"
                        aria-controls="nav-off"
                        aria-selected="false"
                        onClick={() => this.onChangeOption2(false)}
                      >
                        OFF
                      </a>
                    </div>
                  </nav>
                </div>
              </div>
              <p className="space"></p>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
