import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout },
} = Components;

export default inject("PageStore", "NativeStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          playwifi: 'N'
        }
      }

      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        this.loadDataPage();
      }

      loadDataPage = async () => {
        const { NativeStore } = this.props;
        NativeStore.postMessage('GetPlayWifi', this.state.playwifi);
        document.addEventListener("message", event => {
          this.setState({ playwifi: event.data });
        });
        window.addEventListener("message", event => {
          this.setState({ playwifi: event.data });
        });
      }

      onChangeOption1 = async () => {
        const { NativeStore } = this.props;
        const currSetting = this.state.playwifi;
        let setting = ''
        if (currSetting === 'Y') {
          setting = 'N';
        } else {
          setting = 'Y';
        }
        this.setState({ playwifi: setting })
        NativeStore.postMessage('SetPlayWifi', setting);
      };
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
                      onClick={() => this.onChangeOption1()}
                    >
                      <a
                        className={`nav-link ${
                          this.state.playwifi === 'Y' ? "active" : ""
                        } btn btn-on`}
                        id="nav-home-tab"
                        data-toggle="tab"
                        role="tab"
                        aria-controls="nav-on"
                        aria-selected="true"
                      >
                        ON
                      </a>
                      <a
                        className={`nav-link ${
                          this.state.playwifi === 'Y' ? "" : "active"
                        } btn btn-off`}
                        id="nav-profile-tab"
                        data-toggle="tab"
                        role="tab"
                        aria-controls="nav-off"
                        aria-selected="false"
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
