/* global $ */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
import PageStore from "../../stores/PageStore";
const {
  LAYOUT: { NavigationLayout, SmartFishingMainTab },
  MODAL: { SelectDateModal },
  VIEW: { SmartfishingReservationListItemView },
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
        this.status = React.createRef(null);
        this.keywordType = React.createRef(null);
        this.payMethod = React.createRef(null);
        this.state = {
          modalType: 0,
          keywordType: [
            { key: "username", value: "예약자명" },
            { key: "phone", value: "연락처" },
            { key: "shipName", value: "선박명" },
            { key: "goodsName", value: "상품명" },
          ],
          payMethod: [],
          status: [],
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const { PageStore, DataStore } = this.props;

        const payMethod = await DataStore.getEnums("payMethod");
        this.setState({ payMethod });

        const status = await DataStore.getEnums("orderStatus");
        this.setState({ status });

        let { qStatus = null } = PageStore.getQueryParams();
        if (qStatus !== null) {
          this.status.current.value = qStatus;
        }

        const restored = PageStore.restoreState({
          isPending: false,
          isEnd: false,
          list: [],
          page: 0,
          startDate: null,
          endDate: null,
          keywordType: null,
          keyword: null,
          status: qStatus,
          payMethod: null,
        });
        PageStore.setScrollEvent(() => {
          this.loadPageData(PageStore.state.page + 1);
        });
        if (!restored) this.loadPageData();
      }
      initSearchData = () => {
        const { PageStore } = this.props;
        PageStore.setState({
          startDate: null,
          endDate: null,
          keywordType: null,
          keyword: null,
          status: null,
          payMethod: null,
        });
        this.status.current.value = "";
        this.keywordType.current.value = "username";
        this.payMethod.current.value = "";
      };
      loadPageData = async (page = 0) => {
        const { APIStore, PageStore } = this.props;

        if ((page > 0 && PageStore.state.isEnd) || PageStore.state.isPending)
          return;

        PageStore.setState({ page, isPending: true });

        const {
          startDate,
          endDate,
          keywordType,
          keyword,
          status,
          payMethod,
        } = PageStore.state;

        const {
          content,
          pageable: { pageSize = 0 },
        } = await APIStore._get(`/v2/api/orders/${page}`, {
          startDate,
          endDate,
          keywordType,
          keyword,
          status,
          payMethod,
        });

        if (page === 0) {
          PageStore.setState({ list: content });
          setTimeout(() => {
            window.scrollTo(0, 0);
          }, 100);
        } else {
          PageStore.setState({ list: PageStore.state.list.concat(content) });
        }
        if (content.length < pageSize) {
          PageStore.setState({ isEnd: true });
        } else {
          PageStore.setState({ isEnd: false });
        }
        PageStore.setState({ isPending: false });
      };
      onClick = async (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        PageStore.push(`/reservation/detail?orderId=${item.id}`);
      };
      onClickApprove = async (item) => {
        const { ModalStore, APIStore, DataStore } = this.props;
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
              const list = DataStore.updateItemOfArrayByKey(
                PageStore.state.list,
                "orderNumber",
                item.orderNumber,
                { status: "예약 완료" }
              );
              PageStore.setState({ list });
            }
          },
        });
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <SelectDateModal
              id={"selDateModal"}
              onSelected={(selected) => {
                if (this.state.modalType === 0) {
                  PageStore.setState({ startDate: selected.format("-") });
                } else if (this.state.modalType === 1) {
                  PageStore.setState({ endDate: selected.format("-") });
                }
              }}
            />

            <NavigationLayout title={"예약관리"} showBackIcon={true} />
            <SmartFishingMainTab activeIndex={1} />

            <div className="filterlinewrap container nopadding">
              <ul className="nav nav-tabs nav-filter">
                <li>
                  <div className="input-group">
                    <label htmlFor="StartDate" className="sr-only">
                      예약일자
                    </label>
                    <input
                      type="text"
                      className="form-control"
                      id="StartDate"
                      placeholder=""
                      disabled
                      value={PageStore.state.startDate || ""}
                    />
                    <span className="input-group-btn">
                      <a
                        className="btn btn-default"
                        data-toggle="modal"
                        data-target="#selDateModal"
                        onClick={() => this.setState({ modalType: 0 })}
                      >
                        <img
                          src="/assets/smartfishing/img/svg/input_cal.svg"
                          alt=""
                        />
                      </a>
                    </span>
                    <span className="input-group-addon">~</span>
                    <label htmlFor="EndDate" className="sr-only">
                      예약일자
                    </label>
                    <input
                      type="text"
                      className="form-control"
                      id="EndDate"
                      placeholder=""
                      disabled
                      value={PageStore.state.endDate || ""}
                    />
                    <span className="input-group-btn">
                      <a
                        className="btn btn-default"
                        data-toggle="modal"
                        data-target="#selDateModal"
                        onClick={() => this.setState({ modalType: 1 })}
                      >
                        <img
                          src="/assets/smartfishing/img/svg/input_cal.svg"
                          alt=""
                        />
                      </a>
                    </span>
                  </div>
                </li>
                <li>
                  <label htmlFor="selPayStatus" className="sr-only">
                    결제상태
                  </label>
                  <select
                    ref={this.status}
                    className="form-control"
                    onChange={(e) =>
                      PageStore.setState({
                        status: e.target.selectedOptions[0].value || null,
                      })
                    }
                  >
                    <option value={""}>결제상태 전체</option>
                    {this.state.status.map((data, index) => (
                      <option key={index} value={data["key"]}>
                        {data["value"]}
                      </option>
                    ))}
                  </select>
                </li>
                <li>
                  <div className="input-group keyword">
                    <select
                      ref={this.keywordType}
                      className="custom-select"
                      onChange={(e) =>
                        PageStore.setState({
                          keywordType: e.target.selectedOptions[0].value,
                        })
                      }
                    >
                      {this.state.keywordType.map((data, index) => (
                        <option key={index} value={data["key"]}>
                          {data["value"]}
                        </option>
                      ))}
                    </select>
                    <input
                      type="text"
                      className="form-control"
                      placeholder="검색어 입력"
                      value={PageStore.state.keyword || ""}
                      onChange={(e) =>
                        PageStore.setState({ keyword: e.target.value })
                      }
                    />
                  </div>
                </li>
                <li>
                  <label htmlFor="selPay" className="sr-only">
                    결제방법
                  </label>
                  <select
                    ref={this.payMethod}
                    className="form-control"
                    onChange={(e) =>
                      PageStore.setState({
                        payMethod: e.target.selectedOptions[0].value || null,
                      })
                    }
                  >
                    <option value={""}>결제방법 전체</option>
                    {this.state.payMethod.map((data, index) => (
                      <option key={index} value={data["key"]}>
                        {data["value"]}
                      </option>
                    ))}
                  </select>
                </li>
                <li className="full">
                  <p>
                    <a
                      className="btn btn-primary btn-sm"
                      onClick={() => this.loadPageData(0)}
                    >
                      검색
                    </a>
                    <a
                      className="btn btn-grey btn-sm"
                      onClick={this.initSearchData}
                    >
                      초기화
                    </a>
                  </p>
                </li>
              </ul>
            </div>
            <p className="clearfix"></p>

            {PageStore.state.list &&
              PageStore.state.list.map((data, index) => (
                <SmartfishingReservationListItemView
                  key={index}
                  data={data}
                  onClick={this.onClick}
                  onClickApprove={this.onClickApprove}
                />
              ))}
          </React.Fragment>
        );
      }
    }
  )
);
