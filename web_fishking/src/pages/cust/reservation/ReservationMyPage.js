/* global $ */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
import DataStore from "../../../stores/DataStore";
const {
  LAYOUT: { NavigationLayout, MainTab },
  VIEW: { ReservationMyListItemView },
  MODAL: { ConfirmReservationCancelModal, SelectReservationCancelReasonModal },
} = Components;

export default inject(
  "PageStore",
  "DataStore",
  "APIStore",
  "ModalStore",
  "NativeStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          cancelOrderId: null,
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const { PageStore } = this.props;
        const restored = PageStore.restoreState({
          page: 0,
          list: [],
          isEnd: false,
          sort: "",
          options: [],
        });
        PageStore.setScrollEvent(() => {
          this.loadPageData(PageStore.state.page + 1, PageStore.state.sort);
        });
        const options = await DataStore.getEnums("orderStatus");
        PageStore.setState({ options });
        if (!restored) this.loadPageData();
      }
      componentWillUnmount() {
        const { PageStore } = this.props;
        PageStore.removeScrollEvent();
      }
      loadPageData = async (page = 0, sort = "") => {
        const { APIStore, PageStore } = this.props;

        if (page > 0 && PageStore.state.isEnd) return;

        PageStore.setState({ page, sort });
        const { content = [], pageable: { pageSize = 0 } = {} } =
          (await APIStore._get("/v2/api/myOrdersList/" + page, { sort })) || {};

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
      };

      onClick = (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        PageStore.push(`/reservation/my/detail/${item.id}`);
      };
      onClickMap = (item) => {
        const { NativeStore } = this.props;
        NativeStore.openMap({
          address: item.sigungu.replace(/[\n]/g, "").replace(/[\r]/g, ""),
        });
      };
      onClickReservation = (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        PageStore.push(
          `/company/${item.fishingType === "선상" ? "boat" : "rock"}/detail/${
            item.shipId
          }`
        );
      };
      onClickReview = (item) => {
        const { PageStore } = this.props;
        PageStore.storeState();
        PageStore.push(`/reservation/review/add/${item.id}`);
      };
      onClickCancel = (item) => {
        this.setState({ cancelOrderId: item.id });
        $("#cancelModal").modal("show");
      };
      requestCancel = async () => {
        const { APIStore, ModalStore, DataStore, PageStore } = this.props;
        $("#reasonModal").modal("hide");
        const resolve = await APIStore._post(`/v2/api/cancel`, {
          orderId: this.state.cancelOrderId,
        });
        if (resolve && resolve["status"] === "success") {
          ModalStore.openModal("Alert", { body: "취소되었습니다." });
          const list = DataStore.updateItemOfArrayByKey(
            PageStore.state.list,
            "id",
            this.state.cancelOrderId,
            { ordersStatus: "취소 완료" }
          );
          PageStore.setState({ list });
        } else {
          ModalStore.openModal("Alert", { body: resolve["message"] });
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout
              title={"예약내역"}
              showBackIcon={true}
              backPathname={`/main/my`}
              showSearchIcon={true}
            />

            {/** Filter */}
            <div className="filterWrap">
              <div className="slideList">
                <form className="form-inline">
                  <select
                    className="form-control"
                    onChange={(e) => this.loadPageData(0, e.target.value)}
                  >
                    <option value="" selected>
                      전체
                    </option>
                    {PageStore.state.options &&
                      PageStore.state.options.map((data, index) => (
                        <option key={index} value={data.key}>
                          {data.value}
                        </option>
                      ))}
                  </select>
                </form>
              </div>
            </div>

            {/** 안내 */}
            <div className="container nopadding mt-2">
              <div className="card-round-box-grey">
                <nav className="nav nav-pills nav-sel nav-my nav-arrow nav-col-3">
                  <a className="nav-link" href="#none">
                    <figure>
                      <img src="/assets/cust/img/svg/reserv1.svg" alt="" />
                    </figure>
                    <span>결제완료</span>
                  </a>
                  <a className="nav-link" href="#none">
                    <figure>
                      <img src="/assets/cust/img/svg/reserv2.svg" alt="" />
                    </figure>
                    <span>알림발송</span>
                  </a>
                  <a className="nav-link" href="#none">
                    <figure>
                      <img src="/assets/cust/img/svg/reserv3.svg" alt="" />
                    </figure>
                    <span>예약확정</span>
                  </a>
                </nav>
                <hr className="mt-0 mb-3" />
                <p className="text-center">
                  <small className="grey">
                    예약대기 중에 예약 취소 시{" "}
                    <span className="red">취소 및 환불규정</span>에 따라
                    적용됩니다.
                  </small>
                </p>
              </div>
            </div>

            {/** list */}
            {PageStore.state.list &&
              PageStore.state.list.map((data, index) => (
                <ReservationMyListItemView
                  key={index}
                  data={data}
                  onClick={this.onClick}
                  onClickMap={this.onClickMap}
                  onClickReservation={this.onClickReservation}
                  onClickReview={this.onClickReview}
                  onClickCancel={this.onClickCancel}
                />
              ))}
            <p className="clearfix">
              <br />
            </p>

            <MainTab activeIndex={4} />

            {/** 취소/환불규정 모달 */}
            <ConfirmReservationCancelModal
              id={"cancelModal"}
              onClick={() => {
                $("#cancelModal").modal("hide");
                $("#reasonModal").modal("show");
              }}
            />
            <SelectReservationCancelReasonModal
              id={"reasonModal"}
              onClick={this.requestCancel}
            />
          </React.Fragment>
        );
      }
    }
  )
);
