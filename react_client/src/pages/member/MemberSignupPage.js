/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";

export default inject("ValidStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.chk_1_1 = React.createRef(null);
        this.chk_1_2 = React.createRef(null);
        this.chk_1_3 = React.createRef(null);
        this.email = React.createRef(null);
        this.password1 = React.createRef(null);
        this.password2 = React.createRef(null);
        this.nickName = React.createRef(null);
        this.state = {
          stage: 1,
          email: "",
          password1: "",
          password2: "",
          nickName: "",
        };
      }
      /********** ********** ********** ********** **********/
      /** functions **/
      /********** ********** ********** ********** **********/
      goToStage2 = () => {
        if (
          this.chk_1_1.current?.checked &&
          this.chk_1_2.current?.checked &&
          this.chk_1_3.current?.checked
        ) {
          this.setState({ stage: 2 });
        }
      };
      goToStage3 = () => {
        const {
          ValidStore: { isEmail, isPassword },
        } = this.props;
        const { email, password1, password2, nickName } = this.state;

        if (email === "" || !isEmail(email)) {
          this.email.current?.classList.add("is-invalid");
          return;
        } else {
          this.email.current?.classList.remove("is-invalid");
        }
        if (password1 === "" || !isPassword(password1)) {
          this.password1.current?.classList.add("is-invalid");
          return;
        } else {
          this.password1.current?.classList.remove("is-invalid");
        }
        if (password2 !== password1) {
          this.password2.current?.classList.add("is-invalid");
          return;
        } else {
          this.password2.current?.classList.remove("is-invalid");
        }
        if (nickName === "") {
          this.nickName.current?.classList.add("is-invalid");
          return;
        } else {
          this.nickName.current?.classList.remove("is-invalid");
        }

        this.setState({ stage: 3 });
      };
      submit = () => {
        // TODO : 본인인증 요청
      };
      /********** ********** ********** ********** **********/
      /** render **/
      /********** ********** ********** ********** **********/
      render() {
        const { state } = this;
        return (
          <>
            {/** Navigation */}
            <Navigation title={"회원가입"} visibleBackIcon={true} />

            {/** 정보 */}
            <div className="container nopadding mt-1">
              <h5 className="text-center">
                <img src="/assets/img/svg/logo.svg" alt="" />
              </h5>
              <div className="text-right">
                <div className="pay-bg">
                  <ol className="pay-step">
                    <li className={state.stage === 1 ? "active" : ""}>
                      1. 약관동의
                    </li>
                    <li className={state.stage === 2 ? "active" : ""}>
                      2. 정보입력
                    </li>
                    <li className={state.stage === 3 ? "active" : ""}>
                      3. 본인인증
                    </li>
                  </ol>
                </div>
              </div>
            </div>

            {/** 1.약관동의 > 입력 */}
            {state.stage === 1 && (
              <div className="container nopadding mt-4">
                <form>
                  <label className="control radio mt-2">
                    <input
                      type="checkbox"
                      className="add-contrast"
                      data-role="collar"
                      onChange={(e) => {
                        const chk1_1 = this.chk_1_1.current;
                        const chk1_2 = this.chk_1_2.current;
                        const chk1_3 = this.chk_1_3.current;
                        if (e.target.checked) {
                          chk1_1.checked = true;
                          chk1_2.checked = true;
                          chk1_3.checked = true;
                        } else {
                          chk1_1.checked = false;
                          chk1_2.checked = false;
                          chk1_3.checked = false;
                        }
                      }}
                    />
                    <span className="control-indicator"></span>
                    <span className="control-text">
                      <strong>전체동의</strong>
                    </span>
                  </label>
                  <br />
                  <label className="control radio">
                    <input
                      ref={this.chk_1_1}
                      type="checkbox"
                      className="add-contrast"
                      data-role="collar"
                    />
                    <span className="control-indicator"></span>
                    <span className="control-text">
                      이용약관 동의 <small className="red">(필수)</small>
                    </span>
                  </label>
                  <br />
                  <label className="control radio">
                    <input
                      ref={this.chk_1_2}
                      type="checkbox"
                      className="add-contrast"
                      data-role="collar"
                    />
                    <span className="control-indicator"></span>
                    <span className="control-text">
                      개인정보 수집 및 이용동의{" "}
                      <small className="red">(필수)</small>
                      <br />
                      <small className="grey">
                        예약 및 서비스 이용을 위한 필수 개인정보 수집
                      </small>
                    </span>
                  </label>
                  <br />
                  <label className="control radio">
                    <input
                      ref={this.chk_1_3}
                      type="checkbox"
                      className="add-contrast"
                      data-role="collar"
                    />
                    <span className="control-indicator"></span>
                    <span className="control-text">
                      개인정보 수집 및 이용동의{" "}
                      <small className="grey">(선택)</small>
                      <br />
                      <small className="grey">
                        서비스 혜택 등의 정보 제공을 위한 개인정보 수집 및 활동
                      </small>
                    </span>
                  </label>
                  <hr className="full mt-2 mb-3" />
                  <small>
                    회원가입시 본인이 만14세 이상임에 동의하게 됩니다.
                    <br />
                    선택 항목을 동의하지 않아도 서비스를 이용할 수 있습니다.{" "}
                  </small>
                  <div className="form-group mt-4">
                    <a
                      onClick={this.goToStage2}
                      className="btn btn-primary btn-lg btn-block"
                    >
                      다음
                    </a>
                  </div>
                </form>
                <p className="clearfix">
                  <br />
                </p>
              </div>
            )}

            {/** 2.정보입력 > 입력 */}
            {state.stage === 2 && (
              <div className="container nopadding mt-4">
                <div>
                  <form className="form-line mt-3">
                    <div className="form-group">
                      <label htmlFor="inputName" className="sr-only">
                        이메일
                      </label>
                      <input
                        ref={this.email}
                        type="email"
                        className="form-control"
                        placeholder="이메일"
                        value={state.email}
                        onChange={(e) =>
                          this.setState({ email: e.target.value })
                        }
                      />
                    </div>
                    <div className="form-group">
                      <label htmlFor="inputPassword" className="sr-only">
                        비밀번호
                      </label>
                      <input
                        ref={this.password1}
                        type="password"
                        className="form-control"
                        placeholder="비밀번호 (영문/숫자/특수문자 조합, 8~15자 이내)"
                        value={state.password1}
                        onChange={(e) =>
                          this.setState({
                            password1: e.target.value.substr(0, 15),
                          })
                        }
                      />
                    </div>
                    <div className="form-group">
                      <label htmlFor="inputPasswordC" className="sr-only">
                        비밀번호 확인
                      </label>
                      <input
                        ref={this.password2}
                        type="password"
                        className="form-control"
                        placeholder="비밀번호 확인"
                        value={state.password2}
                        onChange={(e) =>
                          this.setState({
                            password2: e.target.value.substr(0, 15),
                          })
                        }
                      />
                    </div>
                    <div className="form-group">
                      <label htmlFor="inputNickname" className="sr-only">
                        닉네임
                      </label>
                      <input
                        ref={this.nickName}
                        type="text"
                        className="form-control"
                        placeholder="닉네임"
                        value={state.nickName}
                        onChange={(e) =>
                          this.setState({ nickName: e.target.value })
                        }
                      />
                    </div>
                    <div className="form-group pt-2">
                      <a
                        onClick={this.goToStage3}
                        className="btn btn-grey btn-lg btn-block"
                      >
                        가입하기
                      </a>
                    </div>
                  </form>
                </div>
              </div>
            )}

            {/** 3.본인인증 > 입력 */}
            {state.stage === 3 && (
              <div className="container nopadding mt-4">
                <p className="text-center">
                  낚시 예약, 예약 조회, 조행기 작성 등의 서비스를 이용하시려면
                  <br />
                  본인 확인을 위한 인증이 반드시 필요합니다.
                </p>
                <div className="row no-gutters mt-5">
                  <div className="col-4 offset-4">
                    <a onClick={this.submit}>
                      <div className="card-round-box">
                        <h6 className="text-center text-primary">
                          <img
                            src="/assets/img/svg/icon-phone.svg"
                            alt=""
                            className="vam mb-2"
                          />
                          <br />
                          휴대폰 본인 인증
                        </h6>
                      </div>
                    </a>
                  </div>
                </div>
              </div>
            )}
          </>
        );
      }
    }
  )
);
