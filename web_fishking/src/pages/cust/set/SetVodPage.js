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
      onChangeOption1 = async (checked) => {};
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            {/** Navigation */}
            <NavigationLayout
              title={"동영상 스트리밍 설정"}
              showBackIcon={true}
            />

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
          </React.Fragment>
        );
      }
    }
  )
);
