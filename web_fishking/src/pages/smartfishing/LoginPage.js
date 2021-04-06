import React from "react";
import { inject, observer } from "mobx-react";

export default inject(
  "PageStore",
  "DataStore",
  "ModalStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.memberId = React.createRef(null);
        this.password = React.createRef(null);
        this.state = {
          memberId: "",
          password: "",
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      onLogin = async () => {
        const { DataStore, APIStore, PageStore, ModalStore } = this.props;
        const { memberId, password } = this.state;

        if (!DataStore.isEmail(memberId)) {
          this.memberId.current?.classList.add("is-invalid");
          return;
        } else {
          this.memberId.current?.classList.remove("is-invalid");
        }
        if (!DataStore.isPassword(password)) {
          this.password.current?.classList.add("is-invalid");
          return;
        } else {
          this.password.current?.classList.remove("is-invalid");
        }

        try {
          const response = await APIStore._post("/v2/api/smartfishing/login", {
            memberId,
            password,
            registrationToken: window.fcm_token || null,
          });
          if (response) {
            PageStore.setAccessToken(response, "smartfishing", "Y");
            PageStore.push(`/dashboard`);
          } else {
            this.password.current?.classList.add("is-invalid");
          }
        } catch (err) {
          ModalStore.openModal("Alert", { body: "ID/PW를 확인해주세요." });
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <nav className="navbar fixed-top navbar-dark">
              <a onClick={() => PageStore.goBack()} className="nav-left">
                <img
                  src="/assets/smartfishing/img/svg/navbar-back-black.svg"
                  alt="뒤로가기"
                />
              </a>
              <span className="navbar-title"></span>
            </nav>

            <div className="container nopadding mt-1">
              <h5 className="text-center">
                <img src="/assets/smartfishing/img/svg/logo.svg" alt="" />
              </h5>
            </div>

            <div class="container nopadding">
              <div class="mt-4">
                <form class="form-line mt-	1">
                  <div class="form-group">
                    <label for="inputName" class="sr-only">
                      이메일
                    </label>
                    <input
                      type="email"
                      class="form-control"
                      placeholder="이메일"
                      value={this.state.memberId}
                      onChange={(e) =>
                        this.setState({ memberId: e.target.value })
                      }
                    />
                  </div>
                  <div class="form-group">
                    <label for="inputPhone" class="sr-only">
                      비밀번호
                    </label>
                    <input
                      type="password"
                      class="form-control"
                      placeholder="비밀번호 (영문/숫자/특수문자 조합, 8~15자 이내)"
                      value={this.state.password}
                      onChange={(e) =>
                        this.setState({
                          password: e.target.value.substr(0, 15),
                        })
                      }
                    />
                  </div>
                </form>
                <a
                  onClick={this.onLogin}
                  class="btn btn-primary btn-lg btn-block"
                >
                  로그인
                </a>
                <p class="text-center mt-3">
                  <a onClick={() => PageStore.push(`/findpw`)}>
                    <small class="grey">비밀번호를 잊으셨나요?</small>
                  </a>
                </p>
                <a
                  class="btn btn-grey btn-lg btn-block mt-4"
                  onClick={() => PageStore.push(`/apply`)}
                >
                  업체등록
                </a>

                <p class="text-center mt-4">
                  스마트출조 로그인/업체등록시 <br />
                  <a
                    class="text-primary"
                    onClick={() => PageStore.push(`/cust/policy/terms`)}
                  >
                    이용약관
                  </a>{" "}
                  및{" "}
                  <a
                    class="text-primary"
                    onClick={() => PageStore.push(`/cust/policy/privacy`)}
                  >
                    개인정보취급방침
                  </a>
                  에 동의하게 됩니다.
                </p>
              </div>
              <p class="clearfix">
                <br />
                <br />
              </p>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
