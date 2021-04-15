import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
import { Calendar } from "react-multi-date-picker";
const {
  LAYOUT: { NavigationLayout },
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
        this.shipId = React.createRef(null);
        this.fishingStartTime = React.createRef(null);
        this.fishingEndTime = React.createRef(null);
        this.isUseTrue = React.createRef(null);
        this.isUseFalse = React.createRef(null);
        this.calendar = React.createRef(null);
        this.positionSelectTrue = React.createRef(null);
        this.positionSelectFalse = React.createRef(null);
        this.reserveTypeAuto = React.createRef(null);
        this.reserveTypeApproval = React.createRef(null);
        this.extraRunSelectTrue = React.createRef(null);
        this.extraRunSelectFalse = React.createRef(null);
        this.state = {
          id: null,

          shipId: null, // 선박
          name: null, // 상품명
          amount: 0, // 상품가격
          minPersonnel: 0, // 정원 - 최소인원
          maxPersonnel: 0, // 정원 - 최대인원
          fishingStartTime: null, // 운항시간 - 시작
          fishingEndTime: null, // 운항시간 - 종료
          isUse: true, // 상태
          species: [], // 주요어종
          fishingDates: [], // 조업일 리스트
          positionSelect: true, // 예약시 위치선정
          reserveType: "auto", // 예약확정방법
          extraRun: true, // 추가운행여부
          extraPersonnel: 0, // 추가운행최소인원수
          extraShipNumber: 0, // 최대선박수

          arr_ship: [],
          arr_species: [],
          minDate: null,
          arr_dates: [],
        };
        this.arr_hour = [];
        for (let i = 0; i < 24; i++) {
          this.arr_hour.push(i);
        }
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const { PageStore, APIStore, DataStore } = this.props;

        // 최소선택 날짜 (오늘 + 3일)
        let minDate = new Date();
        minDate.setDate(minDate.getDate() + 3);
        // 선박명-업체명
        const resolve = await APIStore._get(`/v2/api/goods/ships`);
        this.setState({ arr_ship: resolve });
        // 주요어종
        const arr80 = await DataStore.getCodes("80", 3);
        const arr161 = await DataStore.getCodes("161", 3);
        const arr162 = await DataStore.getCodes("162", 3);
        this.setState({
          minDate,
          arr_ship: resolve,
          arr_species: [...arr80, ...arr161, ...arr162],
        });

        const { id } = PageStore.getQueryParams();
        if (id) {
          this.setState({ id });
          this.loadPageData(id);
        }
      }
      loadPageData = async (id) => {
        const { APIStore } = this.props;
        const resolve = await APIStore._get(`/v2/api/goods/detail/${id}`);
        let {
          shipId,
          name,
          amount,
          minPersonnel,
          maxPersonnel,
          fishingStartTime,
          fishingEndTime,
          isUse,
          species,
          fishingDates,
          positionSelect,
          reserveType,
          extraRun,
        } = resolve;

        let arr_dates = [];
        for (let item of fishingDates) {
          const year = new Number(item.substr(0, 4));
          const month = new Number(item.substr(5, 2)) - 1;
          const date = new Number(item.substr(8, 2));
          const newDate = new Date(year, month, date);
          arr_dates.push(newDate);
        }

        this.shipId.current.value = shipId;
        this.fishingStartTime.current.value = fishingStartTime;
        this.fishingEndTime.current.value = fishingEndTime;
        if (isUse) this.isUseTrue.current.checked = true;
        else this.isUseFalse.current.checked = true;
        for (let item of species) {
          document.querySelector(
            `[name='check-species'][data-value='${item}']`
          ).checked = true;
        }
        if (positionSelect) {
          this.positionSelectTrue.current.click();
        } else {
          this.positionSelectFalse.current.click();
        }
        if (reserveType === "auto") this.reserveTypeAuto.current.checked = true;
        else this.reserveTypeApproval.current.checked = true;
        if (extraRun) {
          this.extraRunSelectTrue.current.click();
        } else {
          this.extraRunSelectFalse.current.click();
        }
        this.setState({ ...resolve, arr_dates });
      };
      selectAllSpecies = () => {
        const eles = document.querySelectorAll('[name="check-species"]');
        for (let ele of eles) ele.checked = true;
        const species = [];
        for (let row of this.state.arr_species) {
          for (let item of row) {
            if (item["code"] === null) continue;
            species.push(item["code"]);
          }
        }
        this.setState({ species });
      };
      selectSpecies = (code, checked) => {
        const { DataStore } = this.props;
        if (checked) {
          this.setState({ species: this.state.species.concat(code) });
        } else {
          this.setState({
            species: DataStore.removeItemOfArrayByItem(
              this.state.species,
              code
            ),
          });
        }
      };
      onSubmit = async () => {
        const { APIStore, ModalStore, PageStore } = this.props;
        const {
          id,
          shipId,
          name,
          amount,
          minPersonnel,
          maxPersonnel,
          fishingStartTime,
          fishingEndTime,
          isUse,
          species,
          fishingDates,
          positionSelect,
          reserveType,
          extraRun,
          extraPersonnel,
          extraShipNumber,
        } = this.state;
        if (shipId === null || shipId === "") {
          ModalStore.openModal("Alert", {
            body: "선상명-업체명 을 선택해주세요.",
          });
          return;
        }
        if (name === null || name === "") {
          ModalStore.openModal("Alert", {
            body: "상품명을 입력해주세요.",
          });
          return;
        }
        if (new Number(minPersonnel) > new Number(maxPersonnel)) {
          ModalStore.openModal("Alert", {
            body: "정원을 확인해주세요.",
          });
          return;
        }
        if (
          fishingStartTime === null ||
          fishingEndTime === null ||
          fishingStartTime.replace(":", "") > fishingEndTime.replace(":", "")
        ) {
          ModalStore.openModal("Alert", {
            body: "운항시간을 확인해주세요.",
          });
          return;
        }
        if (species.length === 0) {
          ModalStore.openModal("Alert", {
            body: "주요어종을 선택해주세요.",
          });
          return;
        }
        if (fishingDates.length === 0) {
          ModalStore.openModal("Alert", {
            body: "조업일을 선택해주세요.",
          });
          return;
        }

        const params = {
          shipId,
          name,
          amount,
          minPersonnel,
          maxPersonnel,
          fishingStartTime,
          fishingEndTime,
          isUse,
          species,
          fishingDates,
          positionSelect,
          reserveType,
          extraRun,
          extraPersonnel,
          extraShipNumber,
        };
        let resolve = null;
        if (id !== null) {
          // 수정
          resolve = await APIStore._put(`/v2/api/goods/update/${id}`, params);
        } else {
          // 등록
          resolve = await APIStore._post(`/v2/api/goods/add`, params);
        }

        if (resolve && resolve["result"] === "success") {
          ModalStore.openModal("Alert", {
            body: "저장되었습니다.",
            onOk: () => {
              PageStore.push(`/goods`);
            },
          });
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout title={"상품등록"} showBackIcon={true} />

            <div className="container nopadding mt-3">
              <p className="text-right">
                <strong className="required"></strong> 필수입력
              </p>
              <form>
                <div className="form-group">
                  <label htmlFor="InputCName">
                    선박명 <strong className="required"></strong>
                  </label>
                  <select
                    ref={this.shipId}
                    className="form-control"
                    onChange={(e) =>
                      this.setState({
                        shipId: e.target.selectedOptions[0].value,
                      })
                    }
                  >
                    <option value={""}>선상명을 선택하세요</option>
                    {this.state.arr_ship?.map((data, index) => (
                      <option value={data["id"]}>{data["shipName"]}</option>
                    ))}
                  </select>
                </div>
                <div className="form-group">
                  <label htmlFor="InputGName">
                    상품명 <strong className="required"></strong>
                  </label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="상품명을 입력하세요"
                    value={this.state.name}
                    onChange={(e) => this.setState({ name: e.target.value })}
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="InputGPrice">
                    상품가격 <small>(숫자만 입력)</small>{" "}
                    <strong className="required"></strong>
                  </label>
                  <input
                    type="number"
                    className="form-control"
                    placeholder="상품가격을 입력하세요"
                    value={Math.abs(this.state.amount)}
                    onChange={(e) => this.setState({ amount: e.target.value })}
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="InputGPrice">
                    정원 <small>(숫자만 입력)</small>{" "}
                    <strong className="required"></strong>
                  </label>
                  <div className="input-group mb-3">
                    <input
                      type="number"
                      className="form-control"
                      placeholder="최소"
                      value={this.state.minPersonnel}
                      onChange={(e) =>
                        this.setState({ minPersonnel: e.target.value })
                      }
                    />
                    <div className="input-group-prepend">
                      <span className="input-group-text">~</span>
                    </div>
                    <input
                      type="number"
                      className="form-control"
                      placeholder="최대"
                      value={this.state.maxPersonnel}
                      onChange={(e) =>
                        this.setState({ maxPersonnel: e.target.value })
                      }
                    />
                    <div className="input-group-append">
                      <span className="input-group-text">명</span>
                    </div>
                  </div>
                </div>
                <div className="form-group">
                  <label htmlFor="InputGPrice">
                    운항 시간 <strong className="required"></strong>
                  </label>
                  <div className="input-group mb-3">
                    <select
                      ref={this.fishingStartTime}
                      className="form-control"
                      onChange={(e) =>
                        this.setState({
                          fishingStartTime: e.target.selectedOptions[0].value,
                        })
                      }
                    >
                      <option value={""}>시작시간</option>
                      {this.arr_hour.map((data, index) => (
                        <option
                          value={
                            data < 10
                              ? "0".concat(data).concat("00")
                              : "".concat(data).concat("00")
                          }
                        >
                          {data < 12 ? "오전" : "오후"}
                          {data}시
                        </option>
                      ))}
                    </select>
                    <div className="input-group-prepend">
                      <span className="input-group-text">부터</span>
                    </div>
                    <select
                      ref={this.fishingEndTime}
                      className="form-control"
                      onChange={(e) =>
                        this.setState({
                          fishingEndTime: e.target.selectedOptions[0].value,
                        })
                      }
                    >
                      <option>종료시간</option>
                      {this.arr_hour.map((data, index) => (
                        <option
                          value={
                            data < 10
                              ? "0".concat(data).concat("00")
                              : "".concat(data).concat("00")
                          }
                        >
                          {data < 12 ? "오전" : "오후"}
                          {data}시
                        </option>
                      ))}
                    </select>
                    <div className="input-group-append">
                      <span className="input-group-text">까지</span>
                    </div>
                  </div>
                </div>
                <div className="space mb-4"></div>
                <div className="form-group">
                  <label htmlFor="InputGPrice" className="d-block">
                    상태 <strong className="required"></strong>
                  </label>
                  <label className="control radio">
                    <input
                      ref={this.isUseTrue}
                      name={"isUse"}
                      type="radio"
                      className="add-contrast"
                      data-role="collar"
                      defaultChecked={this.state.isUse}
                      onClick={(e) => {
                        if (e.target.checked) this.setState({ isUse: true });
                      }}
                    />
                    <span className="control-indicator"></span>
                    <span className="control-text">판매</span>
                  </label>{" "}
                  &nbsp;&nbsp;&nbsp;&nbsp;
                  <label className="control radio">
                    <input
                      ref={this.isUseFalse}
                      name={"isUse"}
                      type="radio"
                      className="add-contrast"
                      data-role="collar"
                      defaultChecked={!this.state.isUse}
                      onClick={(e) => {
                        if (e.target.checked) this.setState({ isUse: false });
                      }}
                    />
                    <span className="control-indicator"></span>
                    <span className="control-text">대기</span>
                  </label>
                </div>
                <div className="space mt-0 mb-4"></div>
                <div className="form-group">
                  <a
                    className="btn btn btn-round-grey btn-xs-round float-right"
                    onClick={this.selectAllSpecies}
                  >
                    전체선택
                  </a>
                  <label htmlFor="InputGPrice" className="d-block">
                    주요어종 <strong className="required"></strong>
                  </label>
                  {this.state.arr_species.map((data, index) => (
                    <div className="row" key={index}>
                      {data.map((item, index2) => (
                        <div className="col" key={index2}>
                          {item["code"] !== null && (
                            <label className="control checkbox">
                              <input
                                name={"check-species"}
                                type="checkbox"
                                className="add-contrast"
                                data-role="collar"
                                data-value={item["code"]}
                                onClick={(e) =>
                                  this.selectSpecies(
                                    item["code"],
                                    e.target.checked
                                  )
                                }
                              />
                              <span className="control-indicator"></span>
                              <span className="control-text">
                                {item["codeName"]}
                              </span>
                            </label>
                          )}
                        </div>
                      ))}
                    </div>
                  ))}
                </div>
                <div className="space mb-4"></div>
                <div className="form-group">
                  <label htmlFor="InputGPrice" className="d-block">
                    조업일 선택 <strong className="required"></strong>
                  </label>
                  <Calendar
                    style={{ width: "100%" }}
                    numberOfMonths={2}
                    multiple
                    value={this.state.arr_dates}
                    minDate={this.state.minDate}
                    onChange={async (dates) => {
                      const fishingDates = [];
                      for (let item of dates) {
                        const year = item["year"];
                        let month = item["month"]["number"];
                        if (month < 10) month = `0${month}`;
                        let day = item["day"];
                        if (day < 10) day = `0${day}`;
                        fishingDates.push(`${year}-${month}-${day}`);
                      }
                      await this.setState({ fishingDates, arr_dates: dates });
                    }}
                  />
                </div>
                <div className="space mb-4"></div>
                <div className="form-group">
                  <div className="row no-gutters align-items-center">
                    <div className="col">
                      <label className="d-block">
                        예약시 위치 선정 <strong className="required"></strong>
                      </label>
                    </div>
                    <div className="col setwrap">
                      <nav>
                        <div
                          className="nav nav-tabs btn-set"
                          id="nav-tab"
                          role="tablist"
                        >
                          <a
                            ref={this.positionSelectTrue}
                            className="nav-link active btn btn-on"
                            id="nav-home-tab"
                            data-toggle="tab"
                            role="tab"
                            aria-controls="nav-on"
                            aria-selected="true"
                            onClick={() =>
                              this.setState({ positionSelect: true })
                            }
                          >
                            ON
                          </a>
                          <a
                            ref={this.positionSelectFalse}
                            className="nav-link btn btn-off"
                            id="nav-profile-tab"
                            data-toggle="tab"
                            role="tab"
                            aria-controls="nav-off"
                            aria-selected="false"
                            onClick={() =>
                              this.setState({ positionSelect: false })
                            }
                          >
                            OFF
                          </a>
                        </div>
                      </nav>
                    </div>
                  </div>
                </div>
                <div className="space mt-0 mb-4"></div>
                <div className="form-group">
                  <label htmlFor="InputGPrice" className="d-block">
                    예약 확정 방법 <strong className="required"></strong>
                  </label>
                  <label className="control radio">
                    <input
                      ref={this.reserveTypeAuto}
                      name={"reserveType"}
                      type="radio"
                      className="add-contrast"
                      data-role="collar"
                      defaultChecked={this.state.reserveType === "auto"}
                      onChange={(e) => {
                        if (e.target.checked)
                          this.setState({ reserveType: "auto" });
                      }}
                    />
                    <span className="control-indicator"></span>
                    <span className="control-text">자동 확정</span>
                  </label>{" "}
                  &nbsp;&nbsp;&nbsp;&nbsp;
                  <label className="control radio">
                    <input
                      ref={this.reserveTypeApproval}
                      name={"reserveType"}
                      type="radio"
                      className="add-contrast"
                      data-role="collar"
                      onChange={(e) => {
                        if (e.target.checked)
                          this.setState({ reserveType: "approval" });
                      }}
                    />
                    <span className="control-indicator"></span>
                    <span className="control-text">선장 수동 확정</span>
                  </label>
                </div>
                <div className="space mt-0 mb-4"></div>
                <div className="form-group">
                  <div className="row no-gutters align-items-center mb-3">
                    <div className="col">
                      <label className="d-block">
                        추가운행 <strong className="required"></strong>
                      </label>
                    </div>
                    <div className="col setwrap">
                      <nav>
                        <div
                          className="nav nav-tabs btn-set"
                          id="nav-tab"
                          role="tablist"
                        >
                          <a
                            ref={this.extraRunSelectTrue}
                            className="nav-link active btn btn-on"
                            id="nav-home-tab"
                            data-toggle="tab"
                            role="tab"
                            aria-controls="nav-on"
                            aria-selected="true"
                            onClick={() => this.setState({ extraRun: true })}
                          >
                            ON
                          </a>
                          <a
                            ref={this.extraRunSelectFalse}
                            className="nav-link btn btn-off"
                            id="nav-profile-tab"
                            data-toggle="tab"
                            role="tab"
                            aria-controls="nav-off"
                            aria-selected="false"
                            onClick={() => this.setState({ extraRun: false })}
                          >
                            OFF
                          </a>
                        </div>
                      </nav>
                    </div>
                  </div>
                  {this.state.extraRun && (
                    <React.Fragment>
                      <div className="input-group mb-3">
                        <div className="input-group-prepend">
                          <span className="input-group-text">
                            선박당 대기자가
                          </span>
                        </div>
                        <div className="input-group-append">
                          <span className="input-group-text">
                            <input
                              type="number"
                              className="form-control"
                              placeholder=""
                              value={this.state.extraPersonnel}
                              onChange={(e) =>
                                this.setState({
                                  extraPersonnel: e.target.value,
                                })
                              }
                            />
                            명 이상 발생할 경우{" "}
                            <input
                              type="number"
                              className="form-control"
                              placeholder=""
                              value={this.state.extraShipNumber}
                              onChange={(e) =>
                                this.setState({
                                  extraShipNumber: e.target.value,
                                })
                              }
                            />{" "}
                            대까지 추가 운행을 진행하며
                          </span>
                        </div>
                      </div>
                      <div className="input-group mb-3">
                        <div className="input-group-prepend">
                          <span className="input-group-text">출조</span>
                        </div>
                        <div className="input-group-append">
                          <span className="input-group-text">
                            2일 전 까지 인원이 차지 않는 경우 해당 선박 운행이
                            취소됩니다.{" "}
                          </span>
                        </div>
                      </div>
                    </React.Fragment>
                  )}
                </div>
              </form>
              <p className="clearfix">
                <br />
                <br />
              </p>
            </div>
            <div className="fixed-bottom">
              <div className="row no-gutters">
                <div className="col-12">
                  <a
                    onClick={this.onSubmit}
                    className="btn btn-primary btn-lg btn-block"
                  >
                    확인
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
