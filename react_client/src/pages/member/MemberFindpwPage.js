/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";

export default inject("ValidStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.mobile = React.createRef(null);
        this.code = React.createRef(null);
        this.newPassword1 = React.createRef(null);
        this.newPassword2 = React.createRef(null);
        this.state = {
          mobile: "",
          code: "",
          isValidMobile: true,
          isValidCode: true,
          isValid: false,
          newPassword1: "",
          newPassword2: "",
        };
      }
      /********** ********** ********** ********** **********/
      /** functions **/
      /********** ********** ********** ********** **********/
      sendSMS = async () => {
        const {
          ValidStore: { isMobile },
        } = this.props;
        const { mobile } = this.state;

        if (!isMobile(mobile)) {
          return;
        }

        // TODO : SMS 발송 요청
      };
      requestValid = async () => {
        const {
          ValidStore: { isMobile },
        } = this.props;
        const { mobile, code } = this.state;

        if (!isMobile(mobile)) {
          return;
        }
        if (code === "") {
          this.code.current?.classList.add("is-invalid");
          return;
        } else {
          this.code.current?.classList.remove("is-invalid");
        }

        // TODO : 인증코드 확인 요청
        this.setState({ isValid: true });
      };
      update = async () => {
        const {
          ValidStore: { isPassword },
        } = this.props;
        const { newPassword1, newPassword2 } = this.state;

        if (newPassword1 === "" || !isPassword(newPassword1)) {
          this.newPassword1.current?.classList.add("is-invalid");
          return;
        } else {
          this.newPassword1.current?.classList.remove("is-invalid");
        }
        if (newPassword2 !== newPassword1 || !isPassword(newPassword2)) {
          this.newPassword2.current?.classList.add("is-invalid");
          return;
        } else {
          this.newPassword2.current?.classList.remove("is-invalid");
        }

        // TODO : 비밀번호 변경 요청
      };
      /********** ********** ********** ********** **********/
      /** render **/
      /********** ********** ********** ********** **********/
      render() {
        return (
          <>
            {/** Navigation */}
            <Navigation title={"비밀번호 재설정"} visibleBackIcon={true} />

            {/** 정보 */}
            <div className="container nopadding mt-1">
              <h5 className="text-center">
                <img src="/assets/img/svg/logo.svg" alt="" />
              </h5>
            </div>

            {/** 인증 > 입력 */}
            {!this.state.isValid && (
              <div className="container nopadding">
                <div className="mt-4">
                  <form className="form-line mt-	1">
                    <div className="form-group">
                      <label htmlFor="inputName" className="sr-only">
                        휴대폰 번호
                      </label>
                      <input
                        ref={this.mobile}
                        type="number"
                        className="form-control"
                        placeholder="휴대폰 번호를 입력해 주세요."
                        value={this.state.mobile}
                        onChange={(e) =>
                          this.setState({
                            mobile: e.target.value.substr(0, 13),
                          })
                        }
                      />
                      <a
                        onClick={this.sendSMS}
                        className="text-link text-primary"
                      >
                        발송
                      </a>
                    </div>
                    <div className="form-group">
                      <label htmlFor="inputPhone" className="sr-only">
                        인증번호
                      </label>
                      <input
                        ref={this.code}
                        type="number"
                        className="form-control"
                        placeholder="인증번호 6자리를 입력해 주세요."
                        value={this.state.code}
                        onChange={(e) =>
                          this.setState({ code: e.target.value.substr(0, 6) })
                        }
                      />
                    </div>
                    <div className="form-group pt-2">
                      <a
                        onClick={this.requestValid}
                        className={
                          "btn btn-lg btn-block" +
                          (this.state.mobile !== "" && this.state.code !== ""
                            ? " btn-primary"
                            : " btn-grey")
                        }
                      >
                        비밀번호 찾기
                      </a>
                    </div>
                  </form>
                </div>
              </div>
            )}

            {/** 비밀번호 재설정 > 입력 */}
            {this.state.isValid && (
              <div className="container nopadding">
                <div className="mt-4">
                  <form className="form-line mt-	1">
                    <div className="form-group">
                      <label htmlFor="inputName" className="sr-only">
                        새 비밀번호
                      </label>
                      <input
                        ref={this.newPassword1}
                        type="password"
                        className="form-control"
                        placeholder="새 비밀번호 (영문/숫자/특수문자 조합, 8~15자 이내)"
                        value={this.state.newPassword1}
                        onChange={(e) =>
                          this.setState({
                            newPassword1: e.target.value.substr(0, 15),
                          })
                        }
                      />
                    </div>
                    <div className="form-group">
                      <label htmlFor="inputPhone" className="sr-only">
                        새 비밀번호 확인
                      </label>
                      <input
                        ref={this.newPassword2}
                        type="password"
                        className="form-control"
                        placeholder="새 비밀번호 확인"
                        value={this.state.newPassword2}
                        onChange={(e) =>
                          this.setState({
                            newPassword2: e.target.value.substr(0, 15),
                          })
                        }
                      />
                    </div>
                    <div className="form-group pt-2">
                      <a
                        onClick={this.update}
                        className="btn btn-primary btn-lg btn-block"
                      >
                        비밀번호 변경
                      </a>
                    </div>
                  </form>
                </div>
              </div>
            )}
          </>
        );
      }
    }
  )
);
