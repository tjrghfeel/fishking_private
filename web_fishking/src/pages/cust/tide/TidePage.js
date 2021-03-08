import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout, TideTab },
  MODAL: { SelectTideAreaModal },
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
        this.selTideModal = React.createRef(null);
        this.state = {
          tabActive: 0,
          date: null,
          tide: null,
          highTideAlert: [],
          lowTideAlert: [],
          addTide: [],
          addDay: [],
          addTime: [],
        };
      }
      todayAlarmArray = [
        [
          { key: "highWater", text: "만조시 알림", highTideAlert: 0 },
          {
            key: "highWaterBefore1",
            text: "만조 1시간 전 알림",
            highTideAlert: -1,
          },
        ],
        [
          {
            key: "highWaterBefore2",
            text: "만조 2시간 전 알림",
            highTideAlert: -2,
          },
          {
            key: "highWaterAfter1",
            text: "만조 1시간 후 알림",
            highTideAlert: 1,
          },
        ],
        [
          {
            key: "highWaterAfter2",
            text: "만조 2시간 후 알림",
            highTideAlert: 2,
          },
          { key: "lowWater", text: "간조시 알림", lowTideAlert: 0 },
        ],
        [
          {
            key: "lowWaterBefore1",
            text: "간조 1시간 전 알림",
            lowTideAlert: -1,
          },
          {
            key: "lowWaterBefore2",
            text: "간조 2시간 전 알림",
            lowTideAlert: -2,
          },
        ],
        [
          {
            key: "lowWaterAfter1",
            text: "간조 1시간 후 알림",
            lowTideAlert: 1,
          },
          {
            key: "lowWaterAfter2",
            text: "간조 2시간 후 알림",
            lowTideAlert: 2,
          },
        ],
      ];
      dailyTideArray = [
        "한물",
        "두물",
        "세물",
        "네물",
        "다섯물",
        "여섯물",
        "일곱물",
        "여덟물",
        "아홉물",
        "열물",
        "열한물",
        "열두물",
        "열셋물",
        "열넷물",
        "조금",
      ];
      dailyDayArray = [
        "1일전",
        "2일전",
        "3일전",
        "4일전",
        "5일전",
        "6일전",
        "7일전",
      ];
      dailyTimeArray = [
        { text: "00시", key: "0" },
        { text: "03시", key: "3" },
        { text: "06시", key: "6" },
        { text: "09시", key: "9" },
        { text: "12시", key: "12" },
      ];
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const {
          match: {
            params: { dayType },
          },
        } = this.props;
        this.setState({
          tabActive: dayType === "today" ? 0 : 1,
          date: new Date(),
        });
        this.selTideModal.current.load(dayType);
      }
      onSelectedTideArea = async (item) => {
        const { APIStore } = this.props;
        const observerId = item?.observerId || this.state.tide.observerId;
        if (observerId === null) return;
        if (this.state.tabActive === 0) {
          const resolve = await APIStore._get(`/v2/api/todayTide`, {
            observerId,
          });
          this.setState({ tide: resolve });
        } else {
          const resolve = await APIStore._get(`/v2/api/tideByDate`, {
            observerId,
            date: this.state.date.format("-"),
          });
          this.setState({ tide: resolve });
          console.log(JSON.stringify(resolve));
        }
      };
      setAlertTide = async () => {
        const { APIStore } = this.props;
        if (this.state.tabActive === 0) {
          console.log(
            JSON.stringify({
              highTideAlert: this.state.highTideAlert,
              lowTideAlert: this.state.lowTideAlert,
              observerId: this.state.tide.observerId,
            })
          );
          const resolve = await APIStore._post(`/v2/api/addTideLevelAlert`, {
            highTideAlert: this.state.highTideAlert,
            lowTideAlert: this.state.lowTideAlert,
            observerId: this.state.tide.observerId,
          });
        } else {
          console.log(
            JSON.stringify({
              tide: this.state.addTide,
              day: this.state.addDay,
              time: this.state.addTime,
              observerId: this.state.tide.observerId,
            })
          );
          const resolve = await APIStore._post(`/v2/api/addTideAlert`, {
            tide: this.state.addTide,
            day: this.state.addDay,
            time: this.state.addTime,
            observerId: this.state.tide.observerId,
          });
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { DataStore } = this.props;
        return (
          <React.Fragment>
            <SelectTideAreaModal
              ref={this.selTideModal}
              id={"selTideModal"}
              onSelected={(data) => this.onSelectedTideArea(data)}
            />

            <NavigationLayout title={"물때와 날씨"} showBackIcon={true} />
            <TideTab activeIndex={this.state.tabActive} />

            <div className="container nopadding mt-3">
              <div className="row no-gutters align-items-center pt-1">
                <div className="col-9 pl-2">
                  <img
                    src="/assets/cust/img/svg/icon-map.svg"
                    alt=""
                    className="vam"
                  />{" "}
                  <strong className="large">
                    {this.state.tide?.observerName}
                  </strong>
                  <a
                    className="btn btn-third btn-xs vam mb-1 ml-2  btn-round-grey btn-xs-round"
                    data-toggle="modal"
                    data-target="#selTideModal"
                  >
                    위치선택
                  </a>
                </div>
                <div className="col-3 text-right pr-2">
                  <a className="active">
                    <span
                      className={"icon icon-alarm".concat(
                        this.state.tide?.isAlerted ? " on" : ""
                      )}
                    ></span>
                  </a>
                </div>
              </div>
            </div>

            <div className="container nopadding mt-2">
              <div className="card-round-box card-round-box-sm pt-0 pb-0">
                <h5 className="text-center">
                  {this.state.tabActive !== 0 && (
                    <a
                      className="cal-arrow-left float-left"
                      onClick={async () => {
                        const date = new Date();
                        date.setDate(this.state.date.getDate() - 1);
                        await this.setState({ date });
                        this.onSelectedTideArea();
                      }}
                    >
                      <img
                        src="/assets/cust/img/svg/cal-arrow-left.svg"
                        alt=""
                        className="vam mr-3"
                      />
                    </a>
                  )}
                  <span className="text-primary">
                    {this.state.date?.toString()}
                  </span>
                  {this.state.tabActive !== 0 && (
                    <a
                      className="cal-arrow-right float-right"
                      onClick={async () => {
                        const date = new Date();
                        date.setDate(this.state.date.getDate() + 1);
                        await this.setState({ date });
                        this.onSelectedTideArea();
                      }}
                    >
                      <img
                        src="/assets/cust/img/svg/cal-arrow-right.svg"
                        alt=""
                        className="vam ml-3"
                      />
                    </a>
                  )}
                </h5>
              </div>
            </div>

            {/** 물때 */}
            {this.state.tide !== null && (
              <div className="container nopadding mt-0 mb-0">
                <div className="row">
                  <div className="col-4 text-center">
                    <div className="text-center">
                      <div className="tide-info-sm pt-2">
                        <figure>
                          <img
                            src="/assets/cust/img/svg/tide12.svg"
                            alt=""
                            className="icon-sm"
                          />
                        </figure>
                        <span className="large">
                          물때 <strong className="point">5물</strong>
                        </span>
                      </div>
                      <div className="tide-info-sm">
                        <figure>
                          {this.state.tide?.weather && (
                            <img src={this.state.tide.weather[1]} alt="" />
                          )}
                        </figure>
                        <span className="large">
                          {this.state.tide?.weather &&
                            this.state.tide.weather[0]}
                        </span>
                      </div>
                    </div>
                  </div>
                  <div className="col-8 text-center border-left">
                    <div className="text-center pt-2 pb-1">
                      <div className="tide-graph">
                        <figure>
                          <img
                            src="/assets/cust/img/svg/tine-line1.svg"
                            alt=""
                          />
                        </figure>
                        <div className="tide-graph-time-col">
                          {this.state.tide.tideList &&
                            this.state.tide.tideList
                              .slice(0, 4)
                              .map((data, index) => (
                                <div key={index} className="col">
                                  {data == null && ""}
                                  {data !== null && data.dateTime.substr(11, 5)}
                                </div>
                              ))}
                        </div>
                        <div className="tide-graph-data-col">
                          {this.state.tide.tideList &&
                            this.state.tide.tideList
                              .slice(0, 4)
                              .map((data, index) => {
                                if (index % 2 === 0) {
                                  return (
                                    <div className="col up">
                                      {data === null && ""}
                                      {data !== null && data["level"]}
                                    </div>
                                  );
                                } else {
                                  return (
                                    <div className="col down">
                                      {data === null && ""}
                                      {data !== null && data["level"]}
                                    </div>
                                  );
                                }
                              })}
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            )}

            {/** 알람 설정 */}
            {this.state.tide !== null && (
              <div className="container nopadding">
                <div className="row">
                  <div className="col-xs-12 col-sm-12">
                    <h6 className="modal-title-sub">알람 설정</h6>
                    {this.state.tabActive === 0 &&
                      this.todayAlarmArray.map((row, index) => (
                        <div className="row" key={index}>
                          {row.map((col, index2) => (
                            <div className="col" key={index2}>
                              <label className="control checkbox">
                                <input
                                  type="checkbox"
                                  className="add-contrast"
                                  data-role="collar"
                                  defaultChecked={
                                    this.state.tide[col["key"]] || false
                                  }
                                  onChange={(e) => {
                                    const isHigh =
                                      col["key"].indexOf("high") !== -1;
                                    if (e.target.checked) {
                                      if (isHigh) {
                                        this.setState({
                                          highTideAlert: this.state.highTideAlert.concat(
                                            col["highTideAlert"]
                                          ),
                                        });
                                      } else {
                                        this.setState({
                                          lowTideAlert: this.state.lowTideAlert.concat(
                                            col["lowTideAlert"]
                                          ),
                                        });
                                      }
                                    } else {
                                      if (isHigh) {
                                        this.setState({
                                          highTideAlert: DataStore.removeItemOfArrayByItem(
                                            this.state.highTideAlert,
                                            col["highTideAlert"]
                                          ),
                                        });
                                      } else {
                                        this.setState({
                                          lowTideAlert: DataStore.removeItemOfArrayByItem(
                                            this.state.lowTideAlert,
                                            col["lowTideAlert"]
                                          ),
                                        });
                                      }
                                    }
                                  }}
                                />
                                <span className="control-indicator"></span>
                                <span className="control-text">
                                  {col["text"]}
                                </span>
                              </label>
                            </div>
                          ))}
                        </div>
                      ))}
                    {this.state.tabActive === 1 && (
                      <div className="row">
                        <div className="col-4">
                          {this.dailyTideArray.map((data, index) => (
                            <React.Fragment key={index}>
                              <label className="control checkbox">
                                <input
                                  type="checkbox"
                                  className="add-contrast"
                                  data-role="collar"
                                  defaultChecked={
                                    this.state.tide.alertTideList[index]
                                  }
                                  onChange={(e) => {
                                    if (e.target.checked) {
                                      this.setState({
                                        addTide: this.state.addTide.concat(
                                          index + 1
                                        ),
                                      });
                                    } else {
                                      this.setState({
                                        addTide: DataStore.removeItemOfArrayByItem(
                                          this.state.addTide,
                                          index + 1
                                        ),
                                      });
                                    }
                                  }}
                                />
                                <span className="control-indicator"></span>
                                <span className="control-text">{data}</span>
                              </label>
                              <br />
                            </React.Fragment>
                          ))}
                        </div>
                        <div className="col-4">
                          {this.dailyDayArray.map((data, index) => (
                            <React.Fragment key={index}>
                              <label className="control checkbox">
                                <input
                                  type="checkbox"
                                  className="add-contrast"
                                  data-role="collar"
                                  defaultChecked={
                                    this.state.tide.alertDayList[index]
                                  }
                                  onChange={(e) => {
                                    if (e.target.checked) {
                                      this.setState({
                                        addDay: this.state.addDay.concat(
                                          index + 1
                                        ),
                                      });
                                    } else {
                                      this.setState({
                                        addDay: DataStore.removeItemOfArrayByItem(
                                          this.state.addDay,
                                          index + 1
                                        ),
                                      });
                                    }
                                  }}
                                />
                                <span className="control-indicator"></span>
                                <span className="control-text">{data}</span>
                              </label>
                              <br />
                            </React.Fragment>
                          ))}
                        </div>
                        <div className="col-4">
                          {this.dailyTimeArray.map((data, index) => (
                            <React.Fragment key={index}>
                              <label className="control checkbox">
                                <input
                                  type="checkbox"
                                  className="add-contrast"
                                  data-role="collar"
                                  defaultChecked={
                                    this.state.tide.alertTimeList[data["key"]]
                                  }
                                  onChange={(e) => {
                                    if (e.target.checked) {
                                      this.setState({
                                        addTime: this.state.addTime.concat(
                                          data["key"]
                                        ),
                                      });
                                    } else {
                                      this.setState({
                                        addTime: DataStore.removeItemOfArrayByItem(
                                          this.state.addTime,
                                          data["key"]
                                        ),
                                      });
                                    }
                                  }}
                                />
                                <span className="control-indicator"></span>
                                <span className="control-text">
                                  {data["text"]}
                                </span>
                              </label>
                              <br />
                            </React.Fragment>
                          ))}
                        </div>
                      </div>
                    )}
                  </div>
                </div>
              </div>
            )}
            <div className="fixed-bottom">
              <div className="row no-gutters">
                <div className="col-12">
                  <a
                    onClick={this.setAlertTide}
                    className="btn btn-primary btn-lg btn-block btn-btm"
                  >
                    적용하기
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
