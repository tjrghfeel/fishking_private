/* global Chart */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
import PageStore from "../../stores/PageStore";
const {
  LAYOUT: { NavigationLayout, SmartFishingMainTab },
  VIEW: { SmartfishingDashboardListItemView },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "ModalStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.chart1 = React.createRef(null);
        this.chart2 = React.createRef(null);
        this.chart3 = React.createRef(null);
        this.state = {
          count: null,
          bookRunning: [],
          bookConfirm: [],
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
        // >>>>> 결제현황
        let resolve = await APIStore._get(
          `/v2/api/smartfishing/dashboard/count`
        );
        this.setState({ count: resolve });
        this.drawChart(this.chart1.current, [
          { text: "승인필요", value: resolve["percentRunning"] },
          { text: "예약완료", value: resolve["percentConfirm"] },
        ]);
        this.drawChart(this.chart2.current, [
          { text: "대기예약", value: resolve["percentWait"] },
          { text: "예약확정", value: resolve["percentFix"] },
        ]);
        this.drawChart(this.chart3.current, [
          { text: "예약취소", value: resolve["percentCancel"] },
          { text: "출조완료", value: resolve["percentComplete"] },
        ]);
        // >>>>> 승인필요
        resolve = await APIStore._get(
          `/v2/api/smartfishing/dashboard/bookRunning`
        );
        this.setState({ bookRunning: resolve });
        // >>>>> 예약완료
        resolve = await APIStore._get(
          `/v2/api/smartfishing/dashboard/bookConfirm`
        );
        this.setState({ bookConfirm: resolve });
      };
      drawChart = (target, chartData = { text: "", value: "" }) => {
        const labels = [];
        const data = [];
        for (let item of chartData) {
          labels.push(item["text"]);
          data.push(item["value"]);
        }
        new Chart(target, {
          type: "pie",
          data: {
            hover: false,
            labels,
            label: {
              display: false,
            },
            datasets: [
              {
                data,
                borderAlign: "center",
                backgroundColor: [
                  "rgba(255, 99, 132, 0.2)",
                  "rgba(54, 162, 235, 0.2)",
                ],
              },
            ],
          },
          options: {
            legend: {
              display: false,
            },
          },
        });
      };
      onClickItem1 = async (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        PageStore.push(`/reservation/detail?orderId=${item.id}`);
      };
      onClickApproveItem1 = async (item) => {
        const { ModalStore, APIStore } = this.props;
        ModalStore.openModal("Confirm", {
          title: "예약승인",
          body: (
            <React.Fragment>
              예약승인 하시겠습니까?
              <br />
              예약승인시 예약완료 상태로 변경됩니다.
            </React.Fragment>
          ),
          onOk: async () => {
            const resolve = await APIStore._post(
              `/v2/api/order/confirm?orderId=${item.id}`
            );
            if (resolve && resolve.success) {
              this.loadPageData();
            }
          },
        });
      };
      onClickItem2 = async (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        PageStore.push(`/reservation/detail?orderId=${item.id}`);
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout
              title={
                <span className="navbar-title">
                  <img
                    src="/assets/smartfishing/img/svg/navbar-logo-smartfishing.svg"
                    alt="스마트출조"
                  />
                </span>
              }
              showBackIcon={true}
            />
            <SmartFishingMainTab activeIndex={0} />

            <div className="container nopadding mt-2">
              <h5>결제현황</h5>
              <div className="card-round-box-grey text-center">
                <div className="row mt-3 mb-3">
                  <div className="col-4">
                    <strong>예약</strong>
                    <p className="mt-2 mb-2">
                      <canvas ref={this.chart1} width={100} height={100} />
                    </p>
                    <p>
                      <small className="grey">승인필요</small> :{" "}
                      <strong className="large orange">
                        {Intl.NumberFormat().format(
                          this.state.count?.countRunning || 0
                        )}
                      </strong>
                    </p>
                    <p>
                      <small className="grey">예약완료</small> :{" "}
                      <strong className="large text-primary">
                        {Intl.NumberFormat().format(
                          this.state.count?.countConfirm || 0
                        )}
                      </strong>
                    </p>
                  </div>
                  <div className="col-4">
                    <strong>진행</strong>
                    <p className="mt-2 mb-2">
                      <canvas ref={this.chart2} width={100} height={100} />
                    </p>
                    <p>
                      <small className="grey">대기예약</small> :{" "}
                      <strong className="large orange">
                        {Intl.NumberFormat().format(
                          this.state.count?.countWait || 0
                        )}
                      </strong>
                    </p>
                    <p>
                      <small className="grey">예약확정</small> :{" "}
                      <strong className="large text-primary">
                        {Intl.NumberFormat().format(
                          this.state.count?.countFix || 0
                        )}
                      </strong>
                    </p>
                  </div>
                  <div className="col-4">
                    <strong>최종</strong>
                    <p className="mt-2 mb-2">
                      <canvas ref={this.chart3} width={100} height={100} />
                    </p>
                    <p>
                      <small className="grey">예약취소</small> :{" "}
                      <strong className="large orange">
                        {Intl.NumberFormat().format(
                          this.state.count?.countCancel || 0
                        )}
                      </strong>
                    </p>
                    <p>
                      <small className="grey">출조완료</small> :{" "}
                      <strong className="large text-primary">
                        {Intl.NumberFormat().format(
                          this.state.count?.countComplete || 0
                        )}
                      </strong>
                    </p>
                  </div>
                </div>
              </div>
            </div>

            {this.state.bookRunning?.length > 0 && (
              <React.Fragment>
                <p className="space mt-2"></p>
                <div className="container nopadding mt-2">
                  <h5 className="mb-1">
                    승인필요
                    <span
                      onClick={() =>
                        PageStore.push(`/reservation?qStatus=bookRunning`)
                      }
                      style={{ float: "right" }}
                    >
                      더보기
                    </span>
                  </h5>
                </div>
                {this.state.bookRunning.map((data, index) => (
                  <SmartfishingDashboardListItemView
                    key={index}
                    data={data}
                    onClick={this.onClickItem1}
                    onClickApprove={this.onClickApproveItem1}
                  />
                ))}
              </React.Fragment>
            )}

            {this.state.bookConfirm?.length > 0 && (
              <React.Fragment>
                <p className="space mt-2"></p>
                <div className="container nopadding mt-2">
                  <h5 className="mb-1">
                    예약완료
                    <span
                      onClick={() =>
                        PageStore.push(`/reservation?qStatus=bookConfirm`)
                      }
                      style={{ float: "right" }}
                    >
                      더보기
                    </span>
                  </h5>
                </div>
                {this.state.bookConfirm.map((data, index) => (
                  <SmartfishingDashboardListItemView
                    key={index}
                    data={data}
                    onClick={this.onClickItem2}
                  />
                ))}
              </React.Fragment>
            )}
          </React.Fragment>
        );
      }
    }
  )
);
