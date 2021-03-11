/* global $ */
import React from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router-dom";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout },
  MODAL: { ConfirmReservationCancelModal, SelectReservationCancelReasonModal },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "ModalStore",
  "NativeStore"
)(
  observer(
    withRouter(
      class extends React.Component {
        constructor(props) {
          super(props);
          this.state = {};
        }
        /********** ********** ********** ********** **********/
        /** function */
        /********** ********** ********** ********** **********/
        componentDidMount() {
          this.loadPageData();
        }

        loadPageData = async () => {
          const {
            match: {
              params: { id: ordersId },
            },
            APIStore,
          } = this.props;
          const resolve = await APIStore._get("/v2/api/OrdersDetail", {
            ordersId,
          });
          console.log(JSON.stringify(resolve));
          this.setState(resolve);

          const shipData = await APIStore._get(
            `/v2/api/ship/${resolve.shipId}`
          );
          // console.log(JSON.stringify(shipData));
          // this.setState({ shipData });

          // 예약상태
          let ordersStatusClassName = "status";
          if (resolve.orderStatus === "예약 대기")
            ordersStatusClassName += " status2";
          else if (resolve.orderStatus === "대기자 예약")
            ordersStatusClassName += " status1";
          else if (resolve.orderStatus === "예약 확정")
            ordersStatusClassName += " status4";
          else if (resolve.orderStatus === "취소 완료")
            ordersStatusClassName += " status6";
          else if (resolve.orderStatus === "출조 완료")
            ordersStatusClassName += " status5";
          else if (resolve.orderStatus === "예약 완료")
            ordersStatusClassName += " status3";
          this.setState({ ordersStatusClassName });

          // 예약일 문자열
          let reservationDateString = "";
          reservationDateString = reservationDateString
            .concat(resolve.fishingDate.substr(0, 4))
            .concat(".");
          reservationDateString = reservationDateString
            .concat(resolve.fishingDate.substr(4, 2))
            .concat(".");
          reservationDateString = reservationDateString.concat(
            resolve.fishingDate.substr(6, 2)
          );

          let week = (resolve.fishingDate || "").getWeek();
          reservationDateString = reservationDateString.concat(` (${week})`);
          this.setState({ reservationDateString });

          // 출항
          let startHour = new Number(resolve.shipStartTime.substr(0, 2));
          this.setState({ ampm: startHour < 12 ? "AM" : "PM" });
        };

        requestSNS = async (type) => {
          // TODO : 공유하기
          console.log(type);
        };
        onClickCompanyInfo = () => {
          const { PageStore } = this.props;
          PageStore.push(`/company/boat/detail/${this.state.shipId}`);
        };
        onClickFindWay = () => {
          const { NativeStore } = this.props;
          const {
            shipData: { latitude: lat, longitude: lng },
          } = this.state;
          NativeStore.openMap({ lat, lng });
        };
        onClickCopyAddress = () => {
          const { NativeStore } = this.props;
          const {
            shipData: { address },
          } = this.state;
          NativeStore.clipboardCopy(address);
        };
        onClickCancelInfo = () => {
          // # 취소/환불규정 모달
          const { ModalStore } = this.props;
          ModalStore.openModal("Alert", {
            title: "취소/환불 규정",
            bodyClass: " ",
            body: (
              <React.Fragment>
                <h6>취소규정</h6>
                <ul className="list">
                  <li>
                    해당 상품은 출조 시간 12시간 또는 24시간 전 까지 예약내역
                    하단의 [취소하기] 버튼을 통해 고객님께서 직접 취소가
                    가능하며 사용하신 쿠폰은 소멸되어 복구가 불가합니다.
                  </li>
                  <li className="red">
                    해당 상품은 출조시간 12시간 또는 24시간 이후부터 취소가
                    불가능합니다.
                  </li>
                </ul>
                <br />
                <h6>환불규정</h6>
                <ul className="list">
                  <li>
                    PG사에 따라 영업일 기준 3~5일(휴일제외) 정도 소요될 수
                    있으니 양해 부탁드립니다.
                  </li>
                </ul>
              </React.Fragment>
            ),
          });
        };
        onClickCancel1 = () => {
          // # 취소하기 동의 모달
          $("#cancelModal").modal("show");
        };
        onClickCancel2 = () => {
          if (this.check1.current.checked) {
            $("#cancelModal").modal("hide");
            $("#reasonModal").modal("show");
          }
        };
        requestCancel = async () => {
          // TODO : 취소하기
          $("#reasonModal").modal("hide");
        };
        requestReservation = () => {
          // TODO : 다시 예약하기
          console.log(JSON.stringify(this.state));
          const { PageStore } = this.props;
          PageStore.push(
            `/company/${
              this.state.fishingType === "선상" ? "boat" : "rock"
            }/detail/${this.state.shipId}`
          );
        };
        /********** ********** ********** ********** **********/
        /** render */
        /********** ********** ********** ********** **********/
        render() {
          const { ModalStore } = this.props;
          return (
            <React.Fragment>
              <NavigationLayout
                title={"예약 상세정보"}
                showBackIcon={true}
                backPathname={`/reservation/my`}
                customButton={
                  <React.Fragment>
                    <a
                      onClick={() =>
                        ModalStore.openModal("SNS", {
                          onSelect: this.requestSNS,
                        })
                      }
                      className="fixed-top-right"
                    >
                      <img
                        src="/assets/cust/img/svg/navbar-share.svg"
                        alt="공유하기"
                      />
                    </a>
                  </React.Fragment>
                }
              />

              {/** 안내 */}
              <div className="container nopadding mt-4 mb-0">
                <div className="card-round-box-grey">
                  <span className={this.state.ordersStatusClassName}>
                    {this.state.orderStatus}
                  </span>
                  <h5>
                    {this.state.shipName}
                    {"-"}
                    {this.state.goodsName}
                  </h5>
                  <p>
                    <strong>{this.state.fishingType}</strong>
                    <span className="grey">
                      {" "}
                      <small>|</small>
                      {this.state.sigungu}
                    </span>
                  </p>
                  <div className="row no-gutters">
                    <div className="col-4 padding-sm pb-0">
                      <a
                        onClick={this.onClickCompanyInfo}
                        className="btn btn-third btn-block btn-sm mt-1 mb-1"
                      >
                        <img
                          src="/assets/cust/img/svg/icon-note.svg"
                          alt=""
                          className="vam"
                        />{" "}
                        업체정보
                      </a>
                    </div>
                    <div className="col-4 padding-sm pb-0">
                      <a
                        onClick={this.onClickFindWay}
                        className="btn btn-third btn-block btn-sm mt-1 mb-1"
                      >
                        <img
                          src="/assets/cust/img/svg/icon-map.svg"
                          alt=""
                          className="vam"
                        />{" "}
                        길찾기
                      </a>
                    </div>
                    <div className="col-4 padding-sm pb-0">
                      <a
                        onClick={this.onClickCopyAddress}
                        className="btn btn-third btn-block btn-sm mt-1 mb-1"
                      >
                        <img
                          src="/assets/cust/img/svg/icon-copy.svg"
                          alt=""
                          className="vam"
                        />{" "}
                        주소복사
                      </a>
                    </div>
                  </div>
                </div>
              </div>

              <div className="container nopadding">
                <div className="card card-box">
                  <h6 className="card-header-white text-center">
                    <img
                      src="/assets/cust/img/svg/icon-alert.svg"
                      alt=""
                      className="vam"
                    />
                    &nbsp;알려드립니다.
                  </h6>
                  <div className="card-body">
                    <ul className="list mb-2">
                      <li>
                        탑승 준비를 위해 최소 출발시간 30분 이전에 도착
                        부탁드립니다.
                      </li>
                      <li>탑승을 위해 신분증은 필수 지참 부탁드립니다.</li>
                      <li>최소 출조 인원 부족 시 출조가 취소될 수 있습니다.</li>
                      <li>
                        업체의 상품정보가 수시로 변경될 수 있으며, 이로 인한
                        불이익은 어복황제가 책임지지 않습니다.
                      </li>
                    </ul>
                  </div>
                </div>
              </div>

              <div className="container nopadding mt-3">
                <div className="card-round-box pt-0 pb-0">
                  <h5 className="text-center">
                    <span className="text-primary">
                      <img
                        src="/assets/cust/img/svg/icon-dday.svg"
                        alt=""
                        className="vam"
                      />
                      &nbsp;예약일
                    </span>
                    <br /> {this.state.reservationDateString}
                  </h5>
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
                      <strong>{this.state.goodsName}</strong>
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
                      {this.state.ampm}{" "}
                      {(this.state.shipStartTime || "").substr(0, 2)}:
                      {(this.state.shipStartTime || "").substr(2, 2)} ~ <br />
                      {Intl.NumberFormat().format(this.state.goodsPrice || 0)}원
                      <br />
                      {Intl.NumberFormat().format(this.state.personnel || 0)}개
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
                        예약번호
                        <br />
                        예약자명
                        <br />
                        연락처
                      </small>
                    </div>
                    <div className="col-6 text-right">
                      {this.state.ordersNum}
                      <br />
                      {this.state.memberName}
                      <br />
                      {this.state.areaCode}
                      {this.state.localNumber}
                    </div>
                  </div>
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
                      {this.state.orderDate && (
                        <React.Fragment>
                          {this.state.orderDate.substr(0, 4)}-
                          {this.state.orderDate.substr(4, 2)}-
                          {this.state.orderDate.substr(6, 2)}
                        </React.Fragment>
                      )}
                      <br />
                      {this.state.paymentGroup}
                      <br />
                      {Intl.NumberFormat().format(this.state.totalAmount || 0)}
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
                        {Intl.NumberFormat().format(
                          this.state.paymentAmount || 0
                        )}
                        원
                      </strong>
                    </div>
                  </div>
                </div>
              </div>
              <p className="space mt-2"></p>

              {(this.state.orderStatus === "대기자 예약" ||
                this.state.orderStatus === "예약 대기" ||
                this.state.orderStatus === "예약 진행중" ||
                this.state.orderStatus === "예약 완료") && (
                <div className="container nopadding mb-2">
                  <div className="float-right">
                    <a onClick={this.onClickCancelInfo}>
                      <img src="/assets/cust/img/svg/icon-noti.svg" alt="" />
                    </a>
                  </div>
                  <p className="mt-1">
                    <small className="red">
                      해당 상품은 출항 12시간 이후부터 취소는 불가능합니다.{" "}
                    </small>
                  </p>
                  <br />
                  <p>
                    <a
                      onClick={this.onClickCancel1}
                      className="btn btn-third btn-block btn-sm mt-1 mb-1"
                    >
                      취소하기
                    </a>
                  </p>
                </div>
              )}

              {(this.state.orderStatus === "출조 완료" ||
                this.state.orderStatus === "취소 완료") && (
                <div className="fixed-bottom">
                  <div className="row no-gutters">
                    <div className="col-12">
                      <a
                        onClick={this.requestReservation}
                        className="btn btn-secondary btn-lg btn-block"
                      >
                        다시 예약하기
                      </a>
                    </div>
                  </div>
                </div>
              )}

              {/** 취소/환불규정 모달 */}
              <ConfirmReservationCancelModal
                id={"cancelModal"}
                onClick={() => {
                  $("#cancelModal").modal("hide");
                  $("#reasonModal").modal("show");
                }}
              />
              <SelectReservationCancelReasonModal id={"reasonModal"} />
            </React.Fragment>
          );
        }
      }
    )
  )
);
