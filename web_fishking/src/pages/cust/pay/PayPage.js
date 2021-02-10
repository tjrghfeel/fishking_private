import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout },
} = Components;

export default inject("PageStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          step: 1,
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      onSubmit = () => {};
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <NavigationLayout title={"예약하기"} showBackIcon={true} />

            {/** 정보 */}
            <div className="container nopadding mt-1">
              <h5 className="text-center">
                어복황제1호
                <br />
                <small className="red">20.10.03 (토)</small>
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

            {/** 입력 :: 1단계 */}
            <div className="container nopadding bg-grey bg-grey-sm">
              <div className="card-round-box-grey">
                <h6 className="card-header-white text-center">예약자 정보</h6>
                <form className="form-line mt-3">
                  <div className="form-group">
                    <label htmlFor="inputName" className="sr-only">
                      홍길동
                    </label>
                    <input
                      type="email"
                      className="form-control"
                      id="inputName"
                      placeholder=""
                      value="홍길동"
                    />
                  </div>
                  <div className="form-group">
                    <label htmlFor="inputName" className="sr-only">
                      휴대폰 번호
                    </label>
                    <input
                      type="email"
                      className="form-control"
                      id="inputName"
                      placeholder=""
                      value="01012345678"
                    />
                    <a href="myaccount.html" className="text-link text-primary">
                      변경
                    </a>
                  </div>
                </form>
              </div>

              <div className="card-round-box-grey">
                <h6 className="card-header-white text-center">우럭(오후)</h6>
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
                        value="40,000원"
                        disabled
                      />
                    </div>
                    <div className="col-6">
                      <ul className="sel_num d-flex align-items-center justify-content-center align-items-end">
                        <li>
                          <a href="#none" className="btn btn-num">
                            <img
                              src="/assets/cust/img/svg/icon-minus.svg"
                              alt="빼기"
                            />
                          </a>
                        </li>
                        <li>
                          <h4>1명</h4>
                        </li>
                        <li>
                          <a href="#none" className="btn btn-num">
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
                        type="text"
                        className="form-control no-line form-price"
                        id="inputName"
                        placeholder=""
                        value="40,000원"
                        disabled
                      />
                    </div>
                  </div>
                  <hr className="mt-0" />
                </form>
              </div>

              <p className="clearfix">
                <br />
                <br />
              </p>
            </div>

            {/** 하단버튼 */}
            <div className="fixed-bottom">
              <div className="row no-gutters">
                <div className="col-12">
                  <a
                    onClick={this.onSubmit}
                    className="btn btn-primary btn-lg btn-block"
                  >
                    {this.state.step === 4 && "결제하기"}
                    {this.state.step !== 4 && "다음"}
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
