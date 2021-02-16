/* global $ */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
import APIStore from "../../../stores/APIStore";
const {
  LAYOUT: { NavigationLayout },
  VIEW: { ShipType3PositionView, ShipType5PositionView, ShipType9PositionView },
  MODAL: { SelectReservationCouponModal },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          goodsId: null, // 상품id
          date: "", // 예약날짜
          reservePersonName: "", // 예약자 이름
          reservePersonPhone: "", // 예약자 전화번호
          personCount: 0, // 승선자 수
          personsName: [], // 승선자 정보
          personsPhone: [], // 승선자 정보
          personsBirthdate: [], // 승선자 정보
          positions: [], // 승선위치 정보
          payMethod: "1000000000", // 결제수단
          discountPrice: 0, // 할인가격
          couponId: 0, // 사용쿠폰 id
          paymentPrice: 0, // 쿠폰 적용 후 가격
          totalPrice: 0, // 총 가격

          step: 1, // 진행단계
          goodsName: "", // 상품명
          goodsPrice: 0, // 상품단가
          boat: {}, // 승선위치정보
          coupons: [], // 쿠폰 목록
          persons: [],
          payAgree: false,
          couponName: "쿠폰 선택하기 ",
        };
        this.reservePersonName = React.createRef(null);
        this.reservePersonPhone = React.createRef(null);
        this.personCount = React.createRef(null);
        this.ship = React.createRef(null);
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
            params: { goodsId, date },
          },
          APIStore,
        } = this.props;
        // >>>>> 상품정보
        let resolve = await APIStore._get(`/v2/api/goods/${goodsId}`);
        this.setState({
          goodsName: resolve.name,
          goodsPrice: resolve.price,
          goodsId,
          date,
        });
      };

      onSubmit = async () => {
        if (this.state.step === 1) {
          // >>>>> Step-1 :: validate
          if (this.state.reservePersonName === "") {
            this.reservePersonName.current?.classList.add("is-invalid");
            return;
          } else {
            this.reservePersonName.current?.classList.remove("is-invalid");
          }
          if (this.state.reservePersonPhone === "") {
            this.reservePersonPhone.current?.classList.add("is-invalid");
            return;
          } else {
            this.reservePersonPhone.current?.classList.remove("is-invalid");
          }
          if (this.state.personCount === 0) {
            this.personCount.current?.classList.add("is-invalid");
            return;
          } else {
            this.personCount.current?.classList.remove("is-invalid");
          }
          // >>>>> Step-2 :: prepare
          const arr = [];
          for (let i = 0; i < this.state.personCount; i++) {
            arr.push({ name: "", phone: "", birthdate: "" });
          }
          this.setState({ persons: arr, step: 2 });
        } else if (this.state.step === 2) {
          // >>>>> Step-2 :: validate
          const personsName = [];
          const personsPhone = [];
          const personsBirthdate = [];
          for (let i = 0; i < this.state.personCount; i++) {
            const name = document.querySelector(`#person-name-${i}`);
            const phone = document.querySelector(`#person-phone-${i}`);
            const birthdate = document.querySelector(`#person-birthdate-${i}`);

            if (name.value === "") {
              name.classList.add("is-invalid");
              return;
            } else {
              name.classList.remove("is-invalid");
            }
            if (phone.value === "") {
              phone.classList.add("is-invalid");
              return;
            } else {
              phone.classList.remove("is-invalid");
            }
            if (birthdate.value === "") {
              birthdate.classList.add("is-invalid");
              return;
            } else {
              birthdate.classList.remove("is-invalid");
            }

            personsName.push(name.value);
            personsPhone.push(phone.value);
            personsBirthdate.push(birthdate.value);
          }
          this.setState({
            personsName,
            personsPhone,
            personsBirthdate,
            step: 3,
          });
          // >>>>> Step-3 :: prepare
          const resolve = await APIStore._get(
            `/v2/api/goods/${this.state.goodsId}/position`,
            {
              date: this.state.date,
            }
          );
          this.setState({ boat: resolve });
          console.log("boat -> " + JSON.stringify(resolve));
        } else if (this.state.step === 3) {
          // >>>>> Step-3 :: validate
          const selected = this.ship.current?.selected;
          if (selected.length === 0) {
            alert("위치를 선택해주세요.");
            return;
          }
          const positions = [];
          for (let s of selected) {
            positions.push(new Number(s));
          }
          this.setState({ positions, step: 4 });
          // >>>>> Step-4 :: prepare
          const resolve = await APIStore._get(`/v2/api/usableCoupons`);
          this.setState({ coupons: resolve });
        } else if (this.state.step === 4) {
          // >>>>> Step-4 :: validate
          if (!this.state.payAgree) {
            alert("약관에 동의해주세요.");
            return false;
          }

          const {
            goodsId,
            date,
            reservePersonName,
            reservePersonPhone,
            personCount,
            personsName,
            personsPhone,
            personsBirthdate,
            positions,
            payMethod,
            discountPrice,
            couponId,
            paymentPrice,
            totalPrice,
            token = localStorage.getItem("@accessToken") || "",
          } = this.state;

          const params = {
            goodsId,
            date,
            reservePersonName,
            reservePersonPhone,
            personCount,
            personsName,
            personsPhone,
            personsBirthdate,
            positions,
            payMethod,
            discountPrice,
            couponId,
            paymentPrice,
            totalPrice,
            token,
          };
          console.log(JSON.stringify(params));

          // const resolve = await APIStore._post(`/v2/api/ship/reserve`, params);
          // console.log(JSON.stringify(resolve));
          $.redirect(
            `${process.env.REACT_APP_HTTP_BASE_URL}/v2/api/ship/reserve`,
            params,
            "POST"
          );
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <SelectReservationCouponModal
              id={"selResevationCouponModal"}
              data={this.state.coupons}
              price={this.state.personCount * (this.state.goodsPrice || 0)}
              onSelect={(selected, discount) => {
                if (selected !== null) {
                  this.setState({
                    couponName: selected.couponName,
                    couponId: selected.id,
                    discountPrice: discount,
                    paymentPrice:
                      this.state.goodsPrice * this.state.personCount - discount,
                    totalPrice:
                      this.state.goodsPrice * this.state.personCount - discount,
                  });
                } else {
                  this.setState({
                    couponName: "쿠폰 선택하기",
                    couponId: null,
                    discountPrice: discount,
                    totalPrice:
                      this.state.goodsPrice * this.state.personCount - discount,
                  });
                }
              }}
            />

            <NavigationLayout title={"예약하기"} showBackIcon={true} />

            {/** 정보 */}
            <div className="container nopadding mt-1">
              <h5 className="text-center">
                {this.state.goodsName}
                <br />
                <small className="red">
                  {this.state.date && (
                    <React.Fragment>
                      {this.state.date.substr(2, 2).concat(".")}
                      {this.state.date.substr(5, 2).concat(".")}
                      {this.state.date.substr(8, 2)}
                      {" ("
                        .concat(this.state.date.replace(/[-]/g, "").getWeek())
                        .concat(")")}
                    </React.Fragment>
                  )}
                </small>
              </h5>
              <div className="text-right">
                <div className="pay-bg">
                  <ol className="pay-step">
                    <li className={this.state.step === 1 ? "active" : ""}>
                      1.인원
                    </li>
                    <li className={this.state.step === 2 ? "active" : ""}>
                      2.승선자
                    </li>
                    <li className={this.state.step === 3 ? "active" : ""}>
                      3.위치선정
                    </li>
                    <li className={this.state.step === 4 ? "active" : ""}>
                      4.결제
                    </li>
                  </ol>
                </div>
              </div>
            </div>

            {/** # >>>>> Step . 1 >>>>> START */}
            {this.state.step === 1 && (
              <React.Fragment>
                <div className="container nopadding bg-grey bg-grey-sm">
                  <div className="card-round-box-grey">
                    <h6 className="card-header-white text-center">
                      예약자 정보
                    </h6>
                    <form className="form-line mt-3">
                      <div className="form-group">
                        <label htmlFor="inputName" className="sr-only">
                          예약자 이름
                        </label>
                        <input
                          ref={this.reservePersonName}
                          type="text"
                          className="form-control"
                          placeholder="예약자 이름"
                          value={this.state.reservePersonName}
                          onChange={(e) =>
                            this.setState({ reservePersonName: e.target.value })
                          }
                        />
                      </div>
                      <div className="form-group">
                        <label htmlFor="inputName" className="sr-only">
                          휴대폰 번호
                        </label>
                        <input
                          ref={this.reservePersonPhone}
                          type="number"
                          className="form-control"
                          placeholder="휴대폰 번호"
                          value={this.state.reservePersonPhone}
                          onChange={(e) =>
                            this.setState({
                              reservePersonPhone: e.target.value,
                            })
                          }
                        />
                        <a className="text-link text-primary">변경</a>
                      </div>
                    </form>
                  </div>

                  <div className="card-round-box-grey">
                    <h6 className="card-header-white text-center">
                      {this.state.goodsName}
                    </h6>
                    <form className="form-line mt-3">
                      <div className="form-group row mb-1">
                        <div className="col-6">
                          <label htmlFor="inputName" className="sr-only">
                            상품금액
                          </label>
                          <input
                            type="text"
                            className="form-control no-line"
                            id="inputName"
                            placeholder=""
                            value={Intl.NumberFormat().format(
                              this.state.goodsPrice || 0
                            )}
                            disabled
                          />
                        </div>
                        <div className="col-6">
                          <ul className="sel_num d-flex align-items-center justify-content-center align-items-end">
                            <li>
                              <a
                                onClick={() => {
                                  if (this.state.personCount === 0) return;
                                  else
                                    this.setState({
                                      personCount: this.state.personCount - 1,
                                    });
                                }}
                                className="btn btn-num"
                              >
                                <img
                                  src="/assets/cust/img/svg/icon-minus.svg"
                                  alt="빼기"
                                />
                              </a>
                            </li>
                            <li>
                              <h4>
                                {Intl.NumberFormat().format(
                                  this.state.personCount
                                )}
                                명
                              </h4>
                            </li>
                            <li>
                              <a
                                onClick={() => {
                                  this.setState({
                                    personCount: this.state.personCount + 1,
                                  });
                                }}
                                className="btn btn-num"
                              >
                                <img
                                  src="/assets/cust/img/svg/icon-plus.svg"
                                  alt="더하기"
                                />
                              </a>
                            </li>
                          </ul>
                        </div>
                      </div>
                      <hr className="mt-0" />
                      <div className="form-group row mb-1">
                        <div className="col-6">
                          <label htmlFor="inputName" className="sr-only">
                            상품금액
                          </label>
                          <input
                            type="text"
                            className="form-control no-line"
                            id="inputName"
                            placeholder=""
                            value="금액"
                            disabled
                          />
                        </div>
                        <div className="col-6">
                          <label htmlFor="inputName" className="sr-only">
                            상품금액
                          </label>
                          <input
                            ref={this.personCount}
                            type="text"
                            className="form-control no-line form-price"
                            id="inputName"
                            placeholder=""
                            value={Intl.NumberFormat().format(
                              this.state.personCount *
                                (this.state.goodsPrice || 0)
                            )}
                            disabled
                          />
                        </div>
                      </div>
                      <hr className="mt-0" />
                    </form>
                  </div>
                </div>
              </React.Fragment>
            )}
            {/** # >>>>> Step . 1 >>>>> END */}

            {/** # >>>>> Step . 2 >>>>> START */}
            {this.state.step === 2 && (
              <React.Fragment>
                <div className="container nopadding bg-grey bg-grey-sm">
                  <h5 className="bullet">
                    승선자명부 작성 (
                    {Intl.NumberFormat().format(this.state.personCount)}명)
                  </h5>
                  <p>
                    <small>
                      배가 출항하기 전에 승선자 정보 기록을 위해 승선자명부를
                      작성해야 합니다.
                      <br />
                      해양경찰청 DB와 연동되므로 정확한 정보를 입력해 주세요.
                    </small>
                  </p>
                  <br />

                  {this.state.persons.map((data, index) => (
                    <div className="card-round-box-grey" key={index}>
                      <form className="form-line mt-3">
                        <div className="form-group">
                          <label htmlFor="inputName" className="sr-only">
                            홍길동
                          </label>
                          <input
                            type="text"
                            className="form-control"
                            id={`person-name-${index}`}
                            placeholder="이름을 입력하세요."
                          />
                        </div>
                        <div className="form-group">
                          <label htmlFor="inputPhone" className="sr-only">
                            휴대폰 번호
                          </label>
                          <input
                            type="number"
                            className="form-control"
                            id={`person-phone-${index}`}
                            placeholder="휴대폰 번호를 입력해 주세요."
                          />
                        </div>
                        <div className="form-group">
                          <label htmlFor="inputBirth" className="sr-only">
                            생년 월일을 입력해 주세요.
                          </label>
                          <input
                            type="number"
                            className="form-control"
                            id={`person-birthdate-${index}`}
                            placeholder="생년 월일을 입력해 주세요."
                          />
                        </div>
                      </form>
                    </div>
                  ))}
                </div>
              </React.Fragment>
            )}
            {/** # >>>>> Step . 2 >>>>> END */}

            {/** # >>>>> Step . 3 >>>>> START */}
            {this.state.step === 3 && this.state.boat?.type === 3 && (
              <ShipType3PositionView
                ref={this.ship}
                data={this.state.boat}
                count={this.state.personCount}
              />
            )}
            {this.state.step === 3 && this.state.boat?.type === 5 && (
              <ShipType5PositionView
                ref={this.ship}
                data={this.state.boat}
                count={this.state.personCount}
              />
            )}
            {this.state.step === 3 && this.state.boat?.type === 9 && (
              <ShipType9PositionView
                ref={this.ship}
                data={this.state.boat}
                count={this.state.personCount}
              />
            )}
            {/** # >>>>> Step . 3 >>>>> END */}

            {/** # >>>>> Step . 4 >>>>> START */}
            {this.state.step === 4 && (
              <div className="container nopadding bg-grey bg-grey-sm">
                <div className="card-round-box-grey">
                  <h6 className="card-header-white text-center">할인수단</h6>
                  <form className="form-line mt-3">
                    <div className="form-group">
                      <p className="text-muted text-center">
                        <span className="grey">적용 가능 쿠폰</span> | 상품 할인{" "}
                        <strong className="text-primary">
                          {this.state.coupons?.size || 0}장
                        </strong>
                      </p>
                      <hr className="pt-1 pb-1" />
                      <a
                        className="form-control form-select"
                        data-toggle="modal"
                        data-target="#selResevationCouponModal"
                      >
                        {this.state.couponName}
                      </a>
                    </div>
                  </form>
                </div>

                <div className="card-round-box-grey">
                  <h6 className="card-header-white text-center">결제금액</h6>
                  <form className="form-line mt-2">
                    <div className="form-group row mb-0">
                      <div className="col-6">
                        <input
                          type="text"
                          className="form-control no-line"
                          id="inputName"
                          placeholder=""
                          value="상품금액"
                          disabled
                        />
                      </div>
                      <div className="col-6">
                        <input
                          type="text"
                          className="form-control no-line form-price"
                          id="inputName"
                          placeholder=""
                          value={Intl.NumberFormat()
                            .format(
                              this.state.personCount *
                                (this.state.goodsPrice || 0)
                            )
                            .concat("원")}
                          disabled
                        />
                      </div>
                    </div>
                    <hr className="mt-0" />
                    <div className="form-group row mb-0">
                      <div className="col-6">
                        <input
                          type="text"
                          className="form-control no-line"
                          id="inputName"
                          placeholder=""
                          value="쿠폰 할인 금액"
                          disabled
                        />
                      </div>
                      <div className="col-6">
                        <input
                          type="text"
                          className="form-control no-line form-price"
                          id="inputName"
                          placeholder=""
                          value={Intl.NumberFormat().format(
                            this.state.discountPrice
                          )}
                          disabled
                        />
                      </div>
                    </div>
                    <hr className="mt-0" />
                    <div className="form-group row mb-0">
                      <div className="col-6">
                        <input
                          type="text"
                          className="form-control no-line"
                          id="inputName"
                          placeholder=""
                          value="최종 결제금액"
                          disabled
                        />
                      </div>
                      <div className="col-6">
                        <input
                          type="text"
                          className="form-control no-line form-price text-primary"
                          id="inputName"
                          placeholder=""
                          value={Intl.NumberFormat()
                            .format(
                              this.state.goodsPrice * this.state.personCount -
                                this.state.discountPrice
                            )
                            .concat("원")}
                          disabled
                        />
                      </div>
                    </div>
                    <hr className="mt-0" />
                  </form>
                </div>

                <div className="card-round-box-grey">
                  <h6 className="card-header-white text-center">결제수단</h6>
                  <ul className="nav nav-pills nav-fill nav-sel nav-box mt-3 mb-3">
                    <a
                      onClick={() => this.setState({ payMethod: "1000000000" })}
                      className={
                        "nav-link" +
                        (this.state.payMethod === "1000000000" ? " active" : "")
                      }
                    >
                      신용카드
                    </a>
                    <a
                      onClick={() => this.setState({ payMethod: "0100000000" })}
                      className={
                        "nav-link" +
                        (this.state.payMethod === "0100000000" ? " active" : "")
                      }
                    >
                      가상계좌
                    </a>
                    <a
                      onClick={() => this.setState({ payMethod: "0010000000" })}
                      className={
                        "nav-link" +
                        (this.state.payMethod === "0010000000" ? " active" : "")
                      }
                    >
                      계좌이체
                    </a>
                    <a
                      onClick={() => this.setState({ payMethod: "0000010000" })}
                      className={
                        "nav-link" +
                        (this.state.payMethod === "0000010000" ? " active" : "")
                      }
                    >
                      휴대폰결제
                    </a>
                  </ul>
                </div>

                <div className="card-round-box-grey">
                  <h6 className="card-header-white text-center">
                    개인정보 수집 및 이용에 대한 안내
                  </h6>
                  <ul className="list mt-3">
                    <li>
                      <a href="policy_terms.html">이용약관</a>
                    </li>
                    <li>
                      <a href="policy_privacy.html">
                        개인정보 수집 및 이용동의
                      </a>
                    </li>
                    <li>
                      <a href="policy_cancel.html">취소 및 환불 규정</a>
                    </li>
                    <li>
                      <a href="policy_agree.html">개인정보 제 3자 제공</a>
                    </li>
                  </ul>
                  <hr className="mt-3" />
                  <label className="control checkbox mt-1">
                    <input
                      type="checkbox"
                      className="add-contrast"
                      data-role="collar"
                      onChange={(e) =>
                        this.setState({ payAgree: e.target.checked })
                      }
                    />
                    <span className="control-indicator"></span>
                    <span className="control-text">
                      위 내용을 확인하였으며 결제에 동의합니다.
                    </span>
                  </label>
                </div>
                <p className="text-center">
                  <small className="grey">
                    ㈜투비는 통신판매중계자로서 통신판매의 당사자가 아니며,
                    <br />
                    상품의 예약, 이용 및 환불 등과 관련한 의무와 책임은 각
                    판매자에게 있습니다.
                  </small>
                </p>
                <p className="clearfix">
                  <br />
                </p>
              </div>
            )}
            {/** # >>>>> Step . 4 >>>>> END */}

            <p className="clearfix">
              <br />
              <br />
            </p>

            <div className="fixed-bottom">
              <div className="row no-gutters">
                <div className="col-12">
                  <a
                    onClick={this.onSubmit}
                    className="btn btn-primary btn-lg btn-block"
                  >
                    다음
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
