import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, SmartFishingMainTab },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.shipName = React.createRef(null);
        this.isCalculate = React.createRef(null);
        this.state = {
          year: null,
          month: null,
          shipName: null,
          isCalculate: null,
          pre: null, // 정산예정금액
          list: null,
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const today = new Date().format("");
        await this.setState({
          year: today.substr(0, 4),
          month: today.substr(4, 2),
        });

        const { APIStore } = this.props;
        // 정산예정금액 리스트
        const resolve = await APIStore._get(`/v2/api/calculate/now`);
        this.setState({ pre: resolve });

        this.loadPageData();
      }
      initParams = () => {
        this.shipName.current.value = "";
        this.isCalculate.current.value = 0;
        this.setState({ shipName: null, isCalculate: null });
      };
      loadPageData = async () => {
        const { APIStore } = this.props;
        // 정산 리스트
        const resolve = await APIStore._get(`/v2/api/calculate`, {
          year: this.state.year,
          month: this.state.month,
          shipName: this.state.shipName,
          isCalculate: this.state.isCalculate,
        });
        this.setState({ list: resolve });
      };
      onClick = (item) => {
        const { PageStore } = this.props;
        PageStore.push(
          `/paid/detail?year=${this.state.year}&month=${this.state.month}&shipId=${item.shipId}`
        );
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <NavigationLayout title={"정산관리"} showBackIcon={false} />
            <SmartFishingMainTab activeIndex={5} />

            <div className="filterlinewrap container nopadding">
              <ul className="nav nav-tabs nav-filter">
                <li>
                  <div className="input-group keyword">
                    <select className="custom-select">
                      <option>선상명</option>
                    </select>
                    <input
                      ref={this.shipName}
                      type="text"
                      className="form-control"
                      placeholder="검색어 입력"
                      value={this.state.shipName}
                      onChange={(e) =>
                        this.setState({ shipName: e.target.value })
                      }
                    />
                  </div>
                </li>
                <li>
                  <label className="sr-only">정산상태</label>
                  <select
                    ref={this.isCalculate}
                    className="form-control"
                    onChange={(e) => {
                      const selectedValue = e.target.selectedOptions[0].value;
                      if (selectedValue == 0)
                        this.setState({ isCalculate: null });
                      else if (selectedValue == 1)
                        this.setState({ isCalculate: true });
                      else if (selectedValue == 2)
                        this.setState({ isCalculate: false });
                    }}
                  >
                    <option value={0}>정산상태</option>
                    <option value={1}>정산완료</option>
                    <option value={2}>정산대기</option>
                  </select>
                </li>
                <li className="full">
                  <p>
                    <a
                      className="btn btn-primary btn-sm"
                      onClick={this.loadPageData}
                    >
                      검색
                    </a>
                    <a
                      className="btn btn-grey btn-sm"
                      onClick={this.initParams}
                    >
                      초기화
                    </a>
                  </p>
                </li>
              </ul>
            </div>
            <p className="clearfix"></p>

            {this.state.pre !== null && (
              <React.Fragment>
                <div className="mainAdWrap text-center">
                  <h5 className="mb-2">
                    <span className="text-primary">
                      <img
                        src="/assets/smartfishing/img/svg/icon-paid.svg"
                        alt=""
                        className="vam"
                      />
                      &nbsp;정산 예정 금액{" "}
                      <small>
                        ({this.state.pre?.year}-{this.state.pre?.month})
                      </small>
                    </span>
                  </h5>
                  <div
                    id="carouselRecommend"
                    className="carousel slide"
                    data-ride="carousel"
                  >
                    <ol className="carousel-indicators">
                      {(this.state.pre?.content || []).map((data, index) => (
                        <li
                          key={index}
                          data-target="#carouselRecommend"
                          data-slide-to={index}
                          className={index === 0 ? "active" : ""}
                        ></li>
                      ))}
                    </ol>
                    <div className="carousel-inner">
                      {(this.state.pre?.content || []).map((data, index) => (
                        <div
                          key={index}
                          className={
                            "carousel-item" + (index === 0 ? " active" : "")
                          }
                          onClick={() => this.onClick(data)}
                        >
                          <div className="row no-gutters">
                            <div className="col-12">
                              <div className="card-round-box pt-3 pb-2 mt-1 ml-3 mr-3">
                                <h6>{data["shipName"]}</h6>
                                <h1>
                                  {Intl.NumberFormat().format(data["total"])}
                                  <small className="grey">원</small>
                                </h1>
                              </div>
                            </div>
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>
                </div>
                <p className="space mt-2"></p>
              </React.Fragment>
            )}

            {this.state.list !== null && (
              <React.Fragment>
                <h5 className="text-center">
                  {this.state.list.year}-{this.state.list.month}
                </h5>
                <div className="container nopadding">
                  {(this.state.list.content || []).map((data, index) => (
                    <React.Fragment key={index}>
                      <a onClick={() => this.onClick(data)}>
                        <div className="card card-sm">
                          <div className="row no-gutters d-flex align-items-center">
                            <div className="col-3">
                              <strong>{data["shipName"]}</strong>
                            </div>
                            <div className="col-6">
                              정산금액:{" "}
                              <strong>
                                {Intl.NumberFormat().format(data["total"])}원
                              </strong>
                              <br />
                              예약금액:{" "}
                              {Intl.NumberFormat().format(data["order"])}원
                              <br />
                              취소금액:{" "}
                              <strong className="red">
                                {Intl.NumberFormat().format(data["cancel"])}
                              </strong>
                              원
                            </div>
                            {/*<div className="col-3 text-right">*/}
                            {/*  <span className="status relative status2">*/}
                            {/*    정산대기*/}
                            {/*  </span>*/}
                            {/*</div>*/}
                          </div>
                        </div>
                      </a>
                      <hr className="full mt-3 mb-3" />
                    </React.Fragment>
                  ))}
                </div>
              </React.Fragment>
            )}
          </React.Fragment>
        );
      }
    }
  )
);
