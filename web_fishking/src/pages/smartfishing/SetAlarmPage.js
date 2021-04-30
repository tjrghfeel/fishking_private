import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "DataStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          list: [],
          alarm: true,
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        this.loadPageData();
      }
      loadPageData = async () => {
        const { APIStore } = this.props;
        const resolve = await APIStore._get(`/v2/api/fishing/alarm`);
        this.setState({ alarm: resolve.alarm });
      };
      setAlarm = async (alerted) => {
        const { APIStore } = this.props;
        const resolve = await APIStore._put(
          `/v2/api/fishing/alarm/update`, {
            alarm: alerted
          }
        );
        this.loadPageData();
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            {/** Navigation */}
            <NavigationLayout title={"알림 설정"} showBackIcon={true} />
            <div className="container nopadding mt-3" key={0}>
              <div className="row no-gutters mt-3 mb-2">
                <div className="col-9">
                  <strong>예약</strong>
                  <br/>
                  <small className="grey">
                    예약 관련 알람입니다.
                  </small>
                </div>
                <div className="col-3">
                  <nav>
                    <div
                      className="nav nav-tabs btn-set"
                      id="nav-tab"
                      role="tablist"
                      onClick={() => this.setAlarm(!this.state.alarm)}
                    >
                      <a
                        className={`nav-link ${
                          this.state.alarm ? "active" : ""
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
                          this.state.alarm ? "" : "active"
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
