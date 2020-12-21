import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layouts/Navigation";

export default inject()(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      onChangeUseWifi = async (is) => {
        // TODO : [PUB-OK/API-NO] 설정 > 동영상 : 동영상 스트리밍 설정 변경
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <>
            {/** Navigation */}
            <Navigation title={"동영상 스트리밍 설정"} showBack={true} />

            {/** 입력 */}
            <div className="container nopadding mt-3">
              <div className="row no-gutters mt-3 mb-2">
                <div className="col">
                  <strong>Wi-Fi 에서만 동영상 스트리밍</strong>
                  <br />
                  <small className="grey">
                    Wi-Fi 에서만 동영상이 재생됩니다.
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
                        href="#nav-on"
                        role="tab"
                        aria-controls="nav-on"
                        aria-selected="true"
                        onChange={() => this.onChangeUseWifi(true)}
                      >
                        ON
                      </a>
                      <a
                        className="nav-link btn btn-off"
                        id="nav-profile-tab"
                        data-toggle="tab"
                        href="#nav-off"
                        role="tab"
                        aria-controls="nav-off"
                        aria-selected="false"
                        onChange={() => this.onChangeUseWifi(false)}
                      >
                        OFF
                      </a>
                    </div>
                  </nav>
                </div>
              </div>
              <p className="space"></p>
            </div>
          </>
        );
      }
    }
  )
);
