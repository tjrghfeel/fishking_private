import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
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
          alerted: [],
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        this.loadPageData();
      }
      loadPageData = async () => {
        const { APIStore, DataStore } = this.props;
        const code165 = await DataStore.getCodes("165");
        this.setState({ list: code165 });
        console.log(JSON.stringify(code165));
        const resolve = await APIStore._get(`/v2/api/setting/alertSet`);
        console.log(JSON.stringify(resolve));
      };
      setAlarm = async (data, alerted) => {
        console.log(`${JSON.stringify(data)} : ${alerted}`);
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            {/** Navigation */}
            <NavigationLayout title={"알림 설정"} showBackIcon={true} />

            {this.state.list?.map((data, index) => {
              if (index === 0) {
                return (
                  <div className="container nopadding mt-3" key={index}>
                    <div className="row no-gutters mt-3 mb-2">
                      <div className="col">
                        <strong>{data["codeName"]}</strong>
                        {/*<br />*/}
                        {/*<small className="grey">*/}
                        {/*  작성한 내글에 대한 댓글 알림 입니다.*/}
                        {/*</small>*/}
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
                              onClick={() => this.setAlarm(data, true)}
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
                              onClick={() => this.setAlarm(data, false)}
                            >
                              OFF
                            </a>
                          </div>
                        </nav>
                      </div>
                    </div>
                    <p className="space"></p>
                  </div>
                );
              } else {
                return (
                  <div className="container nopadding mt-1" key={index}>
                    <div className="row no-gutters mt-3 mb-2">
                      <div className="col">
                        <strong>{data["codeName"]}</strong>
                        {/*<br />*/}
                        {/*<small className="grey">*/}
                        {/*  작성한 내글에 대한 댓글 알림 입니다.*/}
                        {/*</small>*/}
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
                              onClick={() => this.setAlarm(data, true)}
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
                              onClick={() => this.setAlarm(data, false)}
                            >
                              OFF
                            </a>
                          </div>
                        </nav>
                      </div>
                    </div>
                    <p className="space"></p>
                  </div>
                );
              }
            })}
          </React.Fragment>
        );
      }
    }
  )
);
