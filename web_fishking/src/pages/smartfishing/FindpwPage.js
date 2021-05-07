import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";

const {
  LAYOUT: { NavigationLayout },
} = Components;

export default inject(
  "APIStore",
  "DataStore",
  "PageStore",
  "ModalStore"
)(
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
          codeId: "",
          code: "",
          valid: false,
          newPassword1: "",
          newPassword2: "",
          userName: "",
          userId: "",
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      requestCode = async () => {
        const { APIStore, DataStore, ModalStore } = this.props;
        const { mobile } = this.state;

        if (!DataStore.isMobile(mobile)) return;

        try {
          const codeId = await APIStore._post("/v2/api/findPw/smsAuthReq", {
            areaCode: mobile.substr(0, 3),
            localNumber: mobile.substr(3, mobile.length),
          });
          this.setState({ codeId });
          const findInfo = await APIStore._put("/v2/api/findPw/uid", {
            phoneAuthId: codeId,
          });
          this.setState({
            userName: findInfo.memberName,
            userId: findInfo.uid,
          });
        } catch (err) {
          ModalStore.openModal("Alert", { body: "휴대폰번호를 확인해주세요." });
          return;
        }
      };

      requestValid = async () => {
        const { APIStore } = this.props;
        const { code: authNum, codeId: phoneAuthId } = this.state;

        if (authNum === "" || phoneAuthId === "") {
          this.code.current?.classList.add("is-invalid");
          return;
        } else {
          this.code.current?.classList.remove("is-invalid");
        }

        const response = await APIStore._post("/v2/api/checkSmsAuth", {
          authNum,
          phoneAuthId,
        });
        if (response) this.setState({ valid: true });
      };

      onChangePassword = async () => {
        const { APIStore, DataStore, PageStore } = this.props;
        const { newPassword1, newPassword2, codeId: phoneAuthId } = this.state;

        if (newPassword1 === "" || !DataStore.isPassword(newPassword1)) {
          this.newPassword1.current?.classList.add("is-invalid");
          return;
        } else {
          this.newPassword1.current?.classList.remove("is-invalid");
        }
        if (newPassword2 !== newPassword1) {
          this.newPassword2.current?.classList.add("is-invalid");
          return;
        } else {
          this.newPassword2.current?.classList.remove("is-invalid");
        }

        const response = await APIStore._put("/v2/api/findPw/updatePw", {
          newPw: newPassword1,
          phoneAuthId,
        });
        if (response) {
          PageStore.push(`/login`);
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            {/** Navigation */}
            <NavigationLayout
              title={"아이디확인 및 비밀번호재설정"}
              showBackIcon={true}
            />

            {/** 정보 */}
            <div className="container nopadding mt-1">
              <h5 className="text-center">
                <img src="/assets/smartfishing/img/svg/logo.svg" alt="" />
              </h5>
            </div>

            {/** 입력 */}
            {!this.state.valid && (
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
                        inputMode="numeric"
                        pattern="\d*"
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
                        inputMode="numeric"
                        pattern="\d*"
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
                        className="btn btn-grey btn-lg btn-block"
                      >
                        인증번호 확인
                      </a>
                    </div>
                  </form>
                </div>
              </div>
            )}

            {this.state.valid && (
              <div className="container nopadding">
                <div style={{ textAlign: "center" }}>
                  {this.state.userName}의 아이디는 {this.state.userId} 입니다.
                </div>
                <div className="form-group pt-2">
                  <a
                    onClick={() => PageStore.push(`/member/login`)}
                    className="btn btn-primary btn-lg btn-block"
                  >
                    확인
                  </a>
                </div>
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
                        onClick={this.onChangePassword}
                        className="btn btn-primary btn-lg btn-block"
                      >
                        비밀번호 재설정
                      </a>
                    </div>
                  </form>
                </div>
              </div>
            )}
          </React.Fragment>
        );
      }
    }
  )
);
