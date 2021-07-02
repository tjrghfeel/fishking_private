import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
import APIStore from "../../../stores/APIStore";
const {
  LAYOUT: { NavigationLayout, TideTab },
  MODAL: { SelectTideAreaModal },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "DataStore",
  "ModalStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.selTideModal = React.createRef(null);
        let observerId = localStorage.getItem("myMenuTideObserverCode");
        this.state = {
          tabActive: 0,
          date: new Date( new Date().setDate( new Date().getDate() + 1)),
          location: (observerId!== null)? {observerId:observerId} : null,
          arr_150: [], // 오늘의 물때정보 > 알림 목록
          arr_158: [], // 날짜별 물때정보 > 알림 목록
          arr_159: [], // 날짜별 물때정보 > 알림 목록
          arr_160: [], // 날짜별 물때정보 > 알림 목록
          sav_150: [],
          sav_158: [],
          sav_159: [],
          sav_160: [],
          selected: [],
          tide: [],
          day: [],
          time: [],
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const {
          match: {
            params: { dayType = "daily" },
          },
          DataStore,
        } = this.props;
        this.selTideModal.current.load(dayType);
        if (dayType === "today") {
          const arr_150 = await DataStore.getCodes("150", 2);
          await this.setState({ tabActive: 0, sav_150: arr_150 });
        } else {
          const arr_158 = await DataStore.getCodes("158");
          const arr_159 = await DataStore.getCodes("159");
          const arr_160 = await DataStore.getCodes("160");
          await this.setState({
            tabActive: 1,
            sav_158: arr_158,
            sav_159: arr_159,
            sav_160: arr_160,
          });
        }
        if(this.state.location !== null){this.onSelectedTideArea(false);}

      }
      loadPageData = async (dayType) => {};
      onSelectedTideArea = async (item) => {
        await this.setState({
          arr_150: [],
          arr_158: [],
          arr_159: [],
          arr_160: [],
        });

        let observerId = null;
        if (item) {observerId = item.observerId;}
        else {observerId = this.state.location?.observerId;}

        localStorage.setItem("myMenuTideObserverCode", observerId);

        if (observerId === null) return;
        if (this.state.tabActive === 0) {
          // 오늘의 -
          const resolve = await APIStore._get(`/v2/api/todayTide`, {
            observerId,
          });
          this.setState({ location: resolve });
        } else {
          // 날짜별 -
          const resolve = await APIStore._get(`/v2/api/tideByDate`, {
            observerId,
            date: this.state.date.format("-"),
          });
          this.setState({
            location: resolve,
            tide: resolve.alertTideList,
            day: resolve.alertDayList,
            time: resolve.alertTimeList,
          });
        }
        await this.setState({
          arr_150: this.state.sav_150,
          arr_158: this.state.sav_158,
          arr_159: this.state.sav_159,
          arr_160: this.state.sav_160,
        });
      };
      onChangeAlarm = async (checked, type, item) => {
        const { DataStore } = this.props;
        if (type === "150") {
          if (checked) {
            this.setState({
              selected: this.state.selected.concat(item["code"]),
            });
          } else {
            this.setState({
              selected: DataStore.removeItemOfArrayByItem(
                this.state.selected,
                item["code"]
              ),
            });
          }
        } else if (type === "158") {
          if (checked) {
            this.setState({ tide: this.state.tide.concat(item["code"]) });
          } else {
            this.setState({
              tide: DataStore.removeItemOfArrayByItem(
                this.state.tide,
                item["code"]
              ),
            });
          }
        } else if (type === "159") {
          if (checked) {
            this.setState({ day: this.state.day.concat(item["code"]) });
          } else {
            await this.setState({
              day: DataStore.removeItemOfArrayByItem(
                this.state.day,
                item["code"]
              ),
            });
          }
        } else if (type === "160") {
          if (checked) {
            this.setState({ time: this.state.time.concat(item["code"]) });
          } else {
            this.setState({
              time: DataStore.removeItemOfArrayByItem(
                this.state.time,
                item["code"]
              ),
            });
          }
        }
      };
      save = async () => {
        if (this.state.location === null) return;
        const { APIStore, ModalStore } = this.props;
        if (this.state.tabActive === 0) {
          // > 오늘의 -
          // if (this.state.selected.length === 0) {
          //   ModalStore.openModal("Alert", {
          //     body: "알람설정 시점을 선택 하십시오",
          //   })
          //   return;
          // }
          const resolve = await APIStore._post(`/v2/api/addTideLevelAlert`, {
            observerId: this.state.location.observerId,
            alertTime: this.state.selected,
          });
          if (resolve) this.onSelectedTideArea();
        } else {
          // > 날짜별 -
          if (!((this.state.tide.length === 0 && this.state.day.length === 0 && this.state.time.length === 0) ||
              (this.state.tide.length>0 && this.state.day.length>0 && this.state.time.length>0)
          )) {
            ModalStore.openModal("Alert", {
              body: "알람설정 시점을 선택 하십시오",
            })
            return;
          }
          const resolve = await APIStore._post(`/v2/api/addTideAlert`, {
            observerId: this.state.location.observerId,
            tide: this.state.tide,
            day: this.state.day,
            time: this.state.time,
          });
          if (resolve) this.onSelectedTideArea();
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <SelectTideAreaModal
              ref={this.selTideModal}
              id={"selTideModal"}
              type={"daily"}
              onSelected={(data) => this.onSelectedTideArea(data)}
            />

            <NavigationLayout title={"물때와 날씨"} showBackIcon={true} />
            <TideTab activeIndex={this.state.tabActive} />

            {/** 데이터 */}
            <div className="container nopadding mt-3">
              <div className="row no-gutters align-items-center pt-1">
                <div className="col-9 pl-2">
                  <img
                    src="/assets/cust/img/svg/icon-map.svg"
                    alt=""
                    className="vam"
                  />{" "}
                  <strong className="large">
                    {this.state.location?.observerName}
                  </strong>
                  <a
                    className="btn btn-third btn-xs vam mb-1 ml-2  btn-round-grey btn-xs-round"
                    data-toggle="modal"
                    data-target="#selTideModal"
                  >
                    위치선택
                  </a>
                  {!this.state.location && (
                    <span className="red" style={{fontSize: '12px'}}> ← 위치를 선택해 주세요 </span>
                  )}
                </div>
                <div className="col-3 text-right pr-2">
                  <a className="active">
                    <span
                      className={
                        "icon icon-alarm " +
                        (this.state.location?.isAlerted ? "on" : "")
                      }
                    ></span>
                  </a>
                </div>
              </div>
            </div>

            {/** 날짜 */}
            <div className="container nopadding mt-2">
              <div className="card-round-box card-round-box-sm pt-0 pb-0">
                <h5 className="text-center">
                  {this.state.tabActive === 1 && (
                    <a
                      className="cal-arrow-left float-left"
                      onClick={async () => {
                        const date = new Date( new Date().setDate( new Date().getDate() + 1));
                        let subdate = new Date(this.state.date);
                        subdate.setDate( subdate.getDate() - 1);
                        subdate.setHours(0,0,0,0,);
                        date.setHours(0,0,0,0);
                        if(subdate.getTime() < date.getTime()){return}
                        this.state.date.setDate(this.state.date.getDate() - 1);
                        await this.setState({ date : this.state.date });
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
                  {this.state.tabActive === 1 && (
                    <a
                      className="cal-arrow-right float-right"
                      onClick={async () => {
                        // const date = new Date();
                        const date = this.state.date;
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

            {this.state.location !== null && (
              <React.Fragment>
                {/** 물때 */}
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
                            물때 <strong className="point">{this.state.location?.tide}</strong>
                          </span>
                        </div>
                        {this.state.location?.weather && (
                          <div className="tide-info-sm">
                            <figure>
                              <img
                                src={this.state.location.weather[1]}
                                alt=""
                              />
                            </figure>
                            <span className="large">
                              {this.state.location.weather[0]}
                            </span>
                          </div>
                        )}
                      </div>
                    </div>
                    <div className="col-8 text-center border-left">
                      <div className="text-center pt-2 pb-1">
                        {this.state.location?.tideList && (
                          <div className="tide-graph">
                            <figure>
                              {(this.state.location?.tideList && this.state.location?.tideList[0] &&
                                  this.state.location?.tideList[0]['peak'] === 'high')?
                                  (<img
                                      src="/assets/cust/img/svg/tine-line1.svg"
                                      alt=""
                                  />)
                                  :
                                  (<img
                                      src="/assets/cust/img/svg/tine-line1.svg"
                                      alt=""
                                      style={{transform:'scaleX(-1)'}}
                                  />)
                              }
                            </figure>
                            <div className="tide-graph-time-col">
                              {this.state.location?.tideList &&
                                this.state.location?.tideList
                                  .slice(0, 4)
                                  .map((data, index) => (
                                    <div key={index} className="col">
                                      {data == null && ""}
                                      {data !== null &&
                                        data.dateTime.substr(11, 5)}
                                    </div>
                                  ))}
                            </div>
                            <div className="tide-graph-data-col">
                              {this.state.location?.tideList &&
                                this.state.location?.tideList
                                  .slice(0, 4)
                                  .map((data, index) => {
                                    if (index % 2 === 0) {
                                      return (
                                        <div className={(data['peak']==='high')? 'col up':'col down'}>
                                          {data === null && ""}
                                          {data !== null && data["level"]}
                                        </div>
                                      );
                                    } else {
                                      return (
                                        <div className={(data['peak']==='high')? 'col up':'col down'}>
                                          {data === null && ""}
                                          {data !== null && data["level"]}
                                        </div>
                                      );
                                    }
                                  })}
                            </div>
                          </div>
                        )}
                      </div>
                    </div>
                  </div>
                </div>

                <div className="container nopadding">
                  {/** 알람 설정 :: 오늘의 물때정보 :: START */}
                  {this.state.tabActive === 0 && (
                    <div className="row">
                      <div className="col-xs-12 col-sm-12">
                        <h6 className="modal-title-sub">알람 설정</h6>
                        {this.state.arr_150.map((row, index) => (
                          <div className="row" key={index}>
                            {row.map((data, index2) => {
                              if (data["id"] === null) return;
                              else
                                return (
                                  <div className="col" key={index2}>
                                    <label className="control checkbox">
                                      <input
                                        type="checkbox"
                                        className="add-contrast"
                                        data-role="collar"
                                        defaultChecked={(
                                          this.state.location
                                            .tidalAlertTimeList || []
                                        ).includes(data["code"])}
                                        onChange={(e) =>
                                          this.onChangeAlarm(
                                            e.target.checked,
                                            "150",
                                            data
                                          )
                                        }
                                      />
                                      <span className="control-indicator"></span>
                                      <span className="control-text">
                                        {data["codeName"]}
                                      </span>
                                    </label>
                                  </div>
                                );
                            })}
                          </div>
                        ))}
                      </div>
                    </div>
                  )}
                  {/** 알람 설정 :: 오늘의 물때정보 :: END */}
                  {/** 알람 설정 :: 날짜별 물때정보 :: START */}
                  {this.state.tabActive === 1 && (
                    <React.Fragment>
                      <div className="row">
                        <div className="col-12">
                          <h6 className="modal-title-sub">알람 설정</h6>
                        </div>
                      </div>
                      <div className="row">
                        <div className="col-4">
                          {this.state.arr_158.map((data, index) => (
                            <React.Fragment>
                              <label className="control checkbox">
                                <input
                                  type="checkbox"
                                  className="add-contrast"
                                  data-role="collar"
                                  defaultChecked={(
                                    this.state.location.alertTideList || []
                                  ).includes(data["code"])}
                                  onChange={(e) =>
                                    this.onChangeAlarm(
                                      e.target.checked,
                                      "158",
                                      data
                                    )
                                  }
                                />
                                <span className="control-indicator"></span>
                                <span className="control-text">
                                  {data["codeName"]}
                                </span>
                              </label>
                              <br />
                            </React.Fragment>
                          ))}
                        </div>
                        <div className="col-4">
                          {this.state.arr_159.map((data, index) => (
                            <React.Fragment>
                              <label className="control checkbox">
                                <input
                                  type="checkbox"
                                  className="add-contrast"
                                  data-role="collar"
                                  defaultChecked={(
                                    this.state.location.alertDayList || []
                                  ).includes(data["code"])}
                                  onChange={(e) =>
                                    this.onChangeAlarm(
                                      e.target.checked,
                                      "159",
                                      data
                                    )
                                  }
                                />
                                <span className="control-indicator"></span>
                                <span className="control-text">
                                  {data["codeName"]}
                                </span>
                              </label>
                              <br />
                            </React.Fragment>
                          ))}
                        </div>
                        <div className="col-4">
                          {this.state.arr_160.map((data, index) => (
                            <React.Fragment>
                              <label className="control checkbox">
                                <input
                                  type="checkbox"
                                  className="add-contrast"
                                  data-role="collar"
                                  defaultChecked={(
                                    this.state.location.alertTimeList || []
                                  ).includes(data["code"])}
                                  onChange={(e) =>
                                    this.onChangeAlarm(
                                      e.target.checked,
                                      "160",
                                      data
                                    )
                                  }
                                />
                                <span className="control-indicator"></span>
                                <span className="control-text">
                                  {data["codeName"]}
                                </span>
                              </label>
                              <br />
                            </React.Fragment>
                          ))}
                        </div>
                      </div>
                    </React.Fragment>
                  )}
                </div>
              </React.Fragment>
            )}
            {this.state.location && (
              <div className="fixed-bottom">
                <div className="row no-gutters">
                  <div className="col-12">
                    <a
                      className="btn btn-primary btn-lg btn-block btn-btm"
                      onClick={this.save}
                    >
                      적용하기
                    </a>
                  </div>
                </div>
              </div>)}
          </React.Fragment>
        );
      }
    }
  )
);
