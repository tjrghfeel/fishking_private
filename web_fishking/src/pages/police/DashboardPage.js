/* global Chart */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, PoliceMainTab },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {};
        this.chart = React.createRef(null);
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        this.loadPageData();
      }

      loadPageData = async () => {
        const { APIStore } = this.props;
        const resolve = await APIStore._get(`/v2/api/police/dashboard`);
        this.setState(resolve);

        // -> draw chart
        new Chart(this.chart.current, {
          type: "pie",
          data: {
            hover: false,
            labels: ["승선완료", "미완료"],
            label: { display: false },
            datasets: [
              {
                data: [
                  resolve["realRiderPercentage"],
                  resolve["waitRiderPercentage"],
                ],
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
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <NavigationLayout
              title={
                <span className="navbar-title">
                  <img
                    src="/assets/police/img/svg/navbar-logo-smartmarine.svg"
                    alt="승선확인"
                  />
                </span>
              }
              showBackIcon={true}
            />

            <div className="container nopadding mt-2">
              <h5 className="mb-1">
                선박현황 <small className="grey">({new Date().toISOString().split('T')[0]}기준)</small>
              </h5>
            </div>
            <div className="container nopadding mt-2">
              <div className="card-round-grey">
                <div className="card card-sm">
                  <div className="row no-gutters">
                    <div className="col-6">
                      <span className="grey">오늘 예정 : </span>
                    </div>
                    <div className="col-6 text-right">
                      <strong className="large">
                        {Intl.NumberFormat().format(
                          this.state.runningToday || 0
                        )}
                      </strong>
                    </div>
                  </div>
                  <hr className="mt-1 mb-2" />
                  <div className="row no-gutters mb-1">
                    <div className="col-6">
                      <span className="grey">현재 출조 : </span>
                    </div>
                    <div className="col-6 text-right">
                      <strong className="large text-primary">
                        {Intl.NumberFormat().format(this.state.runningNow || 0)}
                      </strong>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div className="container nopadding mt-2">
              <h5 className="mb-1">
                승객현황 <small className="grey">({new Date().toISOString().split('T')[0]}기준)</small>
              </h5>
            </div>
            <div className="container nopadding mt-2">
              <div className="card-round-grey">
                <div className="card card-sm">
                  <div className="row no-gutters">
                    <div className="col-6">
                      <span className="grey">금일 예약 인원 : </span>
                    </div>
                    <div className="col-6 text-right">
                      <strong className="large">
                        {Intl.NumberFormat().format(this.state.total || 0)}
                      </strong>
                    </div>
                  </div>
                  <hr className="mt-1 mb-2" />
                  <div className="row no-gutters">
                    <div className="col-6">
                      <span className="grey">금일 승선완료 인원 : </span>
                    </div>
                    <div className="col-6 text-right">
                      <strong className="large text-primary">
                        {Intl.NumberFormat().format(this.state.realRider || 0)}
                      </strong>
                    </div>
                  </div>
                  <hr className="mt-1 mb-2" />
                  <div className="row no-gutters mb-1">
                    <div className="col-6">
                      <span className="grey">승선 대기 인원 : </span>
                    </div>
                    <div className="col-6 text-right">
                      <strong className="large red">
                        {Intl.NumberFormat().format(this.state.waitRider || 0)}
                      </strong>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div className="container nopadding mt-2">
              <h5>
                승선현황 <small className="grey">({new Date().toISOString().split('T')[0]}기준)</small>
              </h5>
              <div className="card-round-box-grey text-center">
                <div className="row mt-3 mb-3 d-flex align-items-center">
                  <div className="col-6">
                    <p className="mt-2 mb-2">
                      <canvas ref={this.chart} width={100} height={100} />
                    </p>
                  </div>
                  <div className="col-6 text-left">
                    <p>
                      <a>
                        <small className="grey">승선완료</small> :{" "}
                        <strong className="large text-primary">
                          {Intl.NumberFormat().format(
                            this.state.realRiderPercentage || 0
                          )}
                        </strong>
                      </a>
                    </p>
                    <p>
                      <a>
                        <small className="grey">미완료</small> :{" "}
                        <strong className="large text-danger">
                          {Intl.NumberFormat().format(
                            this.state.waitRiderPercentage || 0
                          )}
                        </strong>
                      </a>
                    </p>
                  </div>
                </div>
              </div>
            </div>
            <p className="clearfix">
              <br />
            </p>

            <PoliceMainTab activeIndex={0} />
          </React.Fragment>
        );
      }
    }
  )
);
