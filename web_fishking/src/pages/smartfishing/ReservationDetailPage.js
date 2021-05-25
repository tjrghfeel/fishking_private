import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
import PageStore from "../../stores/PageStore";
const {
  LAYOUT: { NavigationLayout },
  VIEW: { ShipType3PositionView, ShipType5PositionView, ShipType9PositionView },
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
        this.state = {};
        this.arr_status = [
          "대기자 예약",
          "예약 진행중",
          "예약 완료",
          "예약 확정",
          "출조 완료",
          "예약 취소",
        ];
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        this.loadPageData();
      }
      loadPageData = async () => {
        const { PageStore, APIStore } = this.props;
        const { orderId } = PageStore.getQueryParams();
        const resolve = await APIStore._get(`/v2/api/orders/detail/${orderId}`);
        this.setState({ ...resolve });
      };
      requestApprove = async () => {
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
              `/v2/api/order/confirm?orderId=${this.state.id}`
            );
            if (resolve && resolve.success) {
              this.loadPageData();
            }
          },
        });
      };
      requestCancel = async () => {
        const { APIStore } = this.props;
        const resolve = await APIStore._post(
          `/v2/api/order/cancel?orderId=${this.state.id}`
        );
        if (resolve && resolve.success) {
          this.loadPageData();
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <NavigationLayout title={"예약 상세정보"} showBackIcon={true} />

            <div className="container nopadding mt-3">
              <div className="card-round-box pt-1 pb-3 text-center">
                <h5 className="mb-2">
                  <span className="text-primary">
                    <img
                      src="/assets/smartfishing/img/svg/icon-dday.svg"
                      alt=""
                      className="vam"
                    />
                    &nbsp;예약번호 : {this.state.orderNumber}
                  </span>
                </h5>
                <div className="pay-bg">
                  <ol className="pay-step">
                    {this.arr_status.slice(0, 5).map((text, index) => (
                      <React.Fragment>
                        {this.state.status === "예약 취소" &&
                          text === "출조 완료" && (
                            <li key={index} className="active">
                              {this.arr_status[5]}
                            </li>
                          )}
                        {(this.state.status !== "예약 취소" ||
                          text !== "출조 완료") && (
                          <li
                            key={index}
                            className={
                              this.state.status == text ? "active" : ""
                            }
                          >
                            {text}
                          </li>
                        )}
                      </React.Fragment>
                    ))}
                  </ol>
                </div>
              </div>
            </div>
            <p className="space mt-2"></p>

            {/** 상품정보 */}
            <div className="container nopadding">
              <div className="card">
                <h6>상품정보</h6>
                <hr className="full mt-1 mb-2" />
                <div className="row no-gutters">
                  <div className="col-6">
                    <strong>{this.state.name}</strong>
                    <br />
                    <small className="grey">
                      출항시간
                      <br />
                      상품가격
                      <br />
                      수량
                    </small>
                  </div>
                  <div className="col-6 text-right">
                    <br />
                    {this.state.fishingStartTime} ~ <br />
                    {Intl.NumberFormat().format(this.state.amount || 0)}원
                    <br />
                    {Intl.NumberFormat().format(
                      this.state.rideList?.length || 0
                    )}
                    개
                  </div>
                </div>
              </div>
            </div>
            <p className="space mt-2"></p>

            {/** 예약자정보 */}
            <div className="container nopadding">
              <div className="card">
                <h6>예약자 정보</h6>
                <hr className="full mt-1 mb-2" />
                <div className="row no-gutters">
                  <div className="col-6">
                    <small className="grey">
                      예약자명
                      <br />
                      연락처
                    </small>
                  </div>
                  <div className="col-6 text-right">
                    {this.state.reserveName}
                    <br />
                    {this.state.reservePhone}
                  </div>
                </div>
              </div>
            </div>
            <p className="space mt-2"></p>

            {/** 승선자정보 */}
            <div className="container nopadding">
              <div className="card">
                <h6>승선자 정보</h6>
                {this.state.rideList?.map((data, index) => (
                  <React.Fragment key={index}>
                    <hr className="full mt-1 mb-2" />
                    <div className="row no-gutters">
                      <div className="col-6">
                        <small className="grey">
                          승선자명
                          <br />
                          연락처
                          <br />
                          생년월일
                        </small>
                      </div>
                      <div className="col-6 text-right">
                        {data["name"]}
                        <br />
                        {data["phone"]}
                        <br />
                        {data["birthday"]}
                      </div>
                    </div>
                  </React.Fragment>
                ))}
              </div>
            </div>
            <p className="space mt-2"></p>

            {/** 결제정보 */}
            <div className="container nopadding">
              <div className="card">
                <h6>결제 정보</h6>
                <hr className="full mt-1 mb-2" />
                <div className="row no-gutters">
                  <div className="col-6">
                    <small className="grey">
                      결제일
                      <br />
                      결재수단
                      <br />총 주문 금액
                      <br />
                      쿠폰 할인 금액
                    </small>
                  </div>
                  <div className="col-6 text-right">
                    {this.state.orderDate?.substr(0, 10)}
                    <br />
                    {this.state.payMethod}
                    <br />
                    {Intl.NumberFormat().format(this.state.payTotalAmount || 0)}
                    원
                    <br />
                    <strong className="red">
                      -
                      {Intl.NumberFormat().format(
                        this.state.discountAmount || 0
                      )}
                      원
                    </strong>
                  </div>
                </div>
                <hr className="full mt-1 mb-2" />
                <div className="row no-gutters">
                  <div className="col-6">
                    <small>결제 금액</small>
                  </div>
                  <div className="col-6 text-right">
                    <strong className="large">
                      {Intl.NumberFormat().format(this.state.amount || 0)}원
                    </strong>
                  </div>
                </div>
              </div>
            </div>
            <p className="space mt-2"></p>

            {/** 선상위치 정보 */}
            <div className="container nopadding">
              <div className="card">
                <h6>선상위치 정보</h6>
                <hr className="full mt-1 mb-2" />
                {this.state.shipType == 3 && (
                  <ShipType3PositionView
                    data={{ used: this.state.reservePositions, total: [] }}
                    editable={false}
                  />
                )}
                {this.state.shipType == 5 && (
                  <ShipType5PositionView
                    data={{ used: this.state.reservePositions, total: [] }}
                    editable={false}
                  />
                )}
                {this.state.shipType == 9 && (
                  <ShipType9PositionView
                    data={{ used: this.state.reservePositions, total: [] }}
                    editable={false}
                  />
                )}
              </div>
            </div>
            <p className="space mt-2"></p>

            {/** 취소정보 */}
            {this.state.status === "예약 취소" && (
              <React.Fragment>
                <div className="container nopadding">
                  <div className="card">
                    <h6>취소 정보</h6>
                    <hr className="full mt-1 mb-2" />
                    <div className="row no-gutters">
                      <div className="col-6">
                        <small className="grey">취소 신청일</small>
                      </div>
                      <div className="col-6 text-right">
                        {this.state.cancelDate}
                      </div>
                    </div>
                    <hr className="full mt-1 mb-2" />
                    <div className="row no-gutters">
                      <div className="col-6">
                        <small>환불금액</small>
                      </div>
                      <div className="col-6 text-right">
                        <strong className="large red">
                          -
                          {Intl.NumberFormat().format(
                            this.state.refundAmount || 0
                          )}
                          원
                        </strong>
                      </div>
                    </div>
                  </div>
                </div>
                <p className="space mt-2"></p>
              </React.Fragment>
            )}

            {/** 하단버튼 */}
            <div className="fixed-bottom">
              <div className="row no-gutters">
                {this.state.status === "예약 진행중" && (
                  <div className="col-6">
                    <a
                      onClick={this.requestApprove}
                      className="btn btn-primary btn-lg btn-block"
                      data-toggle="modal"
                      data-target="#confirmModal"
                    >
                      예약승인
                    </a>
                  </div>
                )}
                {this.state.status !== "출조 완료" &&
                  this.state.status !== "예약 취소" && (
                    <div
                      className={
                        this.state.status === "예약 진행중" ? "col-6" : "col-12"
                      }
                    >
                      <a
                        className="btn btn-secondary btn-lg btn-block"
                        data-toggle="modal"
                        data-target="#cancelModal"
                      >
                        예약취소
                      </a>
                    </div>
                  )}
              </div>
            </div>

            {/** 결제취소 모달 */}
            <div
              className="modal fade"
              id="cancelModal"
              tabIndex="-1"
              aria-labelledby="cancelModalLabel"
              aria-hidden="true"
            >
              <div className="modal-dialog modal-sm modal-dialog-centered">
                <div className="modal-content">
                  <div className="modal-header">
                    <h5
                      className="modal-title text-center"
                      id="cancelModalLabel"
                    >
                      결제 취소
                    </h5>
                  </div>
                  <div className="modal-body">
                    <h6 className="text-center red">
                      <img
                        src="./assets/img/svg/icon-alert.svg"
                        alt=""
                        className="vam"
                      />
                      &nbsp;결제한 모든 상품이 취소처리 됩니다.
                    </h6>
                    <hr />
                    <ul className="list">
                      <li>
                        결제 시 사용하신 쿠폰은 취소 확정 시 취소 및 환불 규정에
                        따라 소멸 처리되며 재발급이 불가합니다.
                      </li>
                    </ul>
                    <hr />
                  </div>
                  <div className="modal-footer-btm">
                    <div className="row no-gutters">
                      <div className="col-6">
                        <a
                          onClick={this.requestCancel}
                          className="btn btn-primary btn-lg btn-block"
                          data-dismiss="modal"
                        >
                          확인
                        </a>
                      </div>
                      <div className="col-6">
                        <a
                          className="btn btn-third btn-lg btn-block"
                          data-dismiss="modal"
                        >
                          닫기
                        </a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
