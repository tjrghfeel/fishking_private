import React from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router-dom";
import Calendar from "react-calendar";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout },
  VIEW: { ReservationGoodsListItemView },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "ModalStore"
)(
  observer(
    withRouter(
      class extends React.Component {
        constructor(props) {
          super(props);
          this.state = {
            minDate: null,
            selectedDate: null,
            selected: null,
            goods: null,
          };
        }
        /********** ********** ********** ********** **********/
        /** function */
        /********** ********** ********** ********** **********/
        async componentDidMount() {
          const minDate = new Date();
          minDate.setDate(minDate.getDate() + 2);
          await this.setState({ minDate, selectedDate: minDate });
          this.onChangeDate(minDate);
        }

        onChangeDate = async (selected) => {
          const radios = document.querySelectorAll('input[type="radio"]');
          for (let radio of radios) {
            radio.checked = false;
          }
          await this.setState({ selectedDate: selected });

          const {
            APIStore,
            match: {
              params: { shipId },
            },
          } = this.props;
          // 상품 목록
          try {
            const goods = await APIStore._get(`/v2/api/ship/${shipId}/goods`, {
              date: this.state.selectedDate.format("-"),
            });
            await this.setState({ goods, selected: null });
          } catch (err) {
            await this.setState({ goods: [], selected: null });
          }
          // # 물때정보
          try {
            const tideTime = await APIStore._get(`/v2/api/tideTime`, {
              date: selected.format("-"),
              shipId,
            });
            await this.setState({
              tideTime: tideTime.tideTime,
              weather: tideTime.weather,
            });
          } catch (err) {
            await this.setState({
              tideTime: null,
              weather: null,
            });
          }
          if (this.state.goods && this.state.goods.length > 0) {
            const resolve = await APIStore._get("/v2/api/tidalPeak", {
              code: this.state.goods[0]["observerCode"],
              date: selected.format("-"),
            });
            let startIndex = -1;
            for (let i = 0; i < resolve.length; i++) {
              if (resolve[i]["peak"] === "high") startIndex = i;
              if (startIndex != -1) break;
            }
            const tide = resolve.slice(startIndex, startIndex + 4);
            while (tide.length < 4) {
              tide.push(null);
            }

            await this.setState({ tide });
          }
        };

        onChange = (checked, item) => {
          if (checked) {
            this.setState({ selected: item });
          }
        };

        onSubmit = () => {
          const { ModalStore } = this.props;
          if (
            this.state.selected === null ||
            this.state.selected.length === 0
          ) {
            ModalStore.openModal("Alert", { body: "상품을 선택해주세요." });
            return;
          }

          const { PageStore } = this.props;
          PageStore.push(
            `/reservation/goods/payment/${
              this.state.selected.id
            }/${this.state.selectedDate.format()}`
          );
        };

        /********** ********** ********** ********** **********/
        /** render */
        /********** ********** ********** ********** **********/
        render() {
          return (
            <React.Fragment>
              <NavigationLayout title={"예약정보"} showBackIcon={true} />

              {/** Filter */}
              <div
                style={{
                  display: "flex",
                  justifyContent: "center",
                  alignItems: "center",
                }}
              >
                <Calendar
                  value={this.state.selectedDate}
                  onChange={this.onChangeDate}
                  minDate={this.state.minDate}
                />
              </div>

              {/** 날짜 */}
              <div className="container nopadding mt-1">
                <div className="card-round-box card-round-box-sm pt-0 pb-0">
                  <h5 className="text-center">
                    <span className="text-primary">
                      {this.state.selectedDate?.toString()}
                    </span>
                  </h5>
                </div>
              </div>

              {(this.state.goods === null || this.state.goods.length === 0) && (
                <div
                  style={{
                    textAlign: "center",
                    marginTop: "1rem",
                    fontWeight: "bold",
                  }}
                >
                  해당일 예약 가능한 상품이 없습니다.
                </div>
              )}

              {this.state.goods !== null && this.state.goods.length > 0 && (
                <React.Fragment>
                  {/** 물때 */}
                  <div className="container nopadding mt-0 mb-0">
                    <div className="row">
                      <div className="col-4 text-center">
                        <div className="text-center">
                          <div className="tide-info pt-2">
                            <figure style={{ textAlign: "center" }}>
                              {this.state.weather && (
                                <React.Fragment>
                                  <img src={this.state.weather[1]} alt="" />
                                  {this.state.weather[0]}
                                </React.Fragment>
                              )}
                            </figure>
                            <span className="large">
                              물때
                              <br />
                              <strong className="point">
                                {this.state.tideTime}
                              </strong>
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
                              {this.state.tide &&
                                this.state.tide.map((data, index) => (
                                  <div key={index} className="col">
                                    {data == null && ""}
                                    {data !== null &&
                                      data.dateTime.substr(11, 5)}
                                  </div>
                                ))}
                            </div>
                            <div className="tide-graph-data-col">
                              {this.state.tide &&
                                this.state.tide.map((data, index) => {
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
                    <p className="space mt-0 mb-1"></p>
                  </div>
                </React.Fragment>
              )}

              <div className="col-12 text-center bg-white m-2">
                <>
                  운행을 위한 취소 인원(정원)이 달성되지 않을 경우<br/>
                  운행이 취소될 수 있습니다.
                </>
              </div>
              <p className="space mt-0 mb-1"></p>

              {/** 리스트 */}
              {this.state.goods &&
                this.state.goods.map((data, index) => (
                  <ReservationGoodsListItemView
                    key={index}
                    data={data}
                    onChange={this.onChange}
                  />
                ))}

              {/** 하단버튼 */}
              <div className="fixed-bottom">
                <div className="row no-gutters">
                  <div className="col-12">
                    <a
                      onClick={this.onSubmit}
                      className="btn btn-primary btn-lg btn-block"
                    >
                      상품 바로 결제
                    </a>
                  </div>
                </div>
              </div>
            </React.Fragment>
          );
        }
      }
    )
  )
);
