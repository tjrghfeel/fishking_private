import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/common/layout/Navigation";

export default inject("DataStore")(
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
          newPassword1: "",
          newPassword2: "",
          isValidCode: false,
        };
      }
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      requestCode = () => {
        const { mobile } = this.state;
        const {
          DataStore: { isValidMobile },
        } = this.props;

        if (mobile === "" || !isValidMobile(mobile)) return;

        // TODO : [PUB-OK/API-NO] 비밀번호변경 : 인증문자 발송 요청
      };
      requestValidCode = () => {
        const { mobile, code } = this.state;
        const {
          DataStore: { isValidMobile },
        } = this.props;

        if (mobile == "" || !isValidMobile(mobile)) return;
        if (code === "") {
          this.code.current?.classList.add("is-invalid");
          return;
        } else {
          this.code.current?.classList.remove("is-invalid");
        }

        // TODO : [PUB-OK/API-NO] 비밀번호 변경 : 인증문자 인증 요청
      };
      requestUpdatePassword = () => {
        const { newPassword1, newPassword2 } = this.state;
        const {
          DataStore: { isValidPassword },
        } = this.props;

        if (newPassword1 === "" || !isValidPassword(newPassword1)) {
          this.newPassword1.current?.classList.add("is-invalid");
          return;
        } else {
          this.newPassword1.current?.classList.remove("is-invalid");
        }
        if (newPassword1 !== newPassword2) {
          this.newPassword2.current?.classList.add("is-invalid");
          return;
        } else {
          this.newPassword2.current?.classList.remove("is-invalid");
        }

        // TODO : [PUB-OK/API-NO] 비밀번호 변경 : 비밀번호 변경 요청
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <>
            {/** Navigation */}
            <Navigation title={"비밀번호 재설정"} showBack={true} />

            {/** 정보 */}
            <div className="container nopadding mt-1">
              <h5 className="text-center">
                <img src="/assets/img/svg/logo.svg" alt="" />
              </h5>
            </div>

            {/** 입력 > 1.휴대폰번호인증 */}
            {!this.state.isValidCode && (
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
                          this.setState({ mobile: e.target.value })
                        }
                      />
                      <a
                        onClick={this.requestCode}
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
                        onClick={this.requestValidCode}
                        className="btn btn-grey btn-lg btn-block"
                      >
                        비밀번호 찾기
                      </a>
                    </div>
                  </form>
                </div>
              </div>
            )}
            {/** 입력 > 2.비밀번호변경 */}
            {this.state.isValidCode && (
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
                        onClick={this.requestUpdatePassword}
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
