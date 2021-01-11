/* global AppleID */
import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/common/layout/Navigation";
import Http from "../../Http";

export default inject(
  "AppStore",
  "DataStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.email = React.createRef(null);
        this.password = React.createRef(null);
        this.state = {
          email: "",
          password: "",
        };
      }
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      requestLogin = async () => {
        const {
          AppStore: { loadAppData },
          DataStore: { isValidEmail, isValidPassword },
          history,
        } = this.props;
        const { email, password } = this.state;

        if (email === "" || !isValidEmail(email)) {
          this.email.current?.classList.add("is-invalid");
          return;
        } else {
          this.email.current?.classList.remove("is-invalid");
        }
        if (password === "" || !isValidPassword(password)) {
          this.password.current?.classList.add("is-invalid");
          return;
        } else {
          this.password.current?.classList.remove("is-invalid");
        }

        const resolve = await Http._post("/v2/api/login", {
          memberId: email,
          password,
        });
        if (resolve) {
          localStorage.setItem("@accessToken", resolve);
          loadAppData();
          history.push(`/main/my`);
        } else {
          this.password.current?.classList.add("is-invalid");
        }
      };

      requestSNSAuthorize = (loginType) => {
        if (loginType === "kakao") {
          window.location.href =
            "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" +
            process.env["REACT_APP_KAKAO_JAVASCRIPT_KEY"] +
            "&redirect_uri=" +
            process.env["REACT_APP_KAKAO_REDIRECT_URI"];
        } else if (loginType === "facebook") {
          window.location.href =
            "https://www.facebook.com/v9.0/dialog/oauth?client_id=" +
            process.env["REACT_APP_FACEBOOK_APP_ID"] +
            "&redirect_uri=" +
            process.env["REACT_APP_FACEBOOK_REDIRECT_URI"] +
            "&state=";
        } else if (loginType === "naver") {
          window.location.href =
            "https://nid.naver.com/oauth2.0/authorize?client_id=" +
            process.env["REACT_APP_NAVER_CLIENT_ID"] +
            "&response_type=code&redirect_uri=" +
            process.env["REACT_APP_NAVER_REDIRECT_URI"] +
            "&state=";
        } else if (loginType === "apple") {
          AppleID.auth.signIn();
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { history } = this.props;
        return (
          <>
            {/** Navigation */}
            <Navigation backgroundTheme={"blank"} showBack={true} />

            {/** 정보 */}
            <div className="container nopadding mt-1">
              <h5 className="text-center">
                <img src="/assets/img/svg/logo.svg" alt="" />
              </h5>
            </div>

            {/** 입력 */}
            <div className="container nopadding">
              <div className="mt-4">
                <form className="form-line mt-	1">
                  <div className="form-group">
                    <label htmlFor="inputName" className="sr-only">
                      이메일
                    </label>
                    <input
                      ref={this.email}
                      type="email"
                      className="form-control"
                      placeholder="이메일"
                      value={this.state.email}
                      onChange={(e) => this.setState({ email: e.target.value })}
                    />
                  </div>
                  <div className="form-group">
                    <label htmlFor="inputPhone" className="sr-only">
                      비밀번호
                    </label>
                    <input
                      ref={this.password}
                      type="password"
                      className="form-control"
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
                  onClick={this.requestLogin}
                  className="btn btn-primary btn-lg btn-block"
                >
                  로그인
                </a>
                <p className="text-center mt-3">
                  <a onClick={() => history.push(`/common/findpw`)}>
                    <small className="grey">비밀번호를 잊으셨나요?</small>
                  </a>
                </p>
                <a
                  onClick={() => history.push(`/common/signup`)}
                  className="btn btn-grey btn-lg btn-block mt-4"
                >
                  이메일로 가입하기
                </a>

                <div className="row no-gutters-md row-padding mt-3">
                  <div className="col-3">
                    <a
                      href="#none"
                      onClick={() => this.requestSNSAuthorize("kakao")}
                      className="btn btn-sns-kakao btn-yellow btn-lg btn-block"
                    >
                      <img
                        src="/assets/img/svg/icon-sns-kakao.svg"
                        alt=""
                        className="vam"
                      />
                      &nbsp;시작
                    </a>
                  </div>
                  <div className="col-3">
                    <a
                      href="#none"
                      onClick={() => this.requestSNSAuthorize("facebook")}
                      className="btn btn-sns-facebook btn-lg btn-block"
                    >
                      <img
                        src="/assets/img/svg/icon-sns-facebook.svg"
                        alt=""
                        className="vam"
                      />
                      &nbsp;시작
                    </a>
                  </div>
                  <div className="col-3">
                    <a
                      href="#none"
                      onClick={() => this.requestSNSAuthorize("naver")}
                      className="btn btn-sns-naver btn-lg btn-block"
                    >
                      <img
                        src="/assets/img/svg/icon-sns-naver.svg"
                        alt=""
                        className="vam"
                      />
                      &nbsp;시작
                    </a>
                  </div>
                  <div className="col-3">
                    <a
                      href="#none"
                      onClick={() => this.requestSNSAuthorize("apple")}
                      className="btn btn-sns-apple btn-lg btn-block"
                    >
                      <img
                        src="/assets/img/svg/icon-sns-apple.svg"
                        alt=""
                        className="vam"
                      />
                      &nbsp;시작
                    </a>
                  </div>
                </div>
                <p className="text-center mt-4">
                  어복황제 로그인/회원가입시 <br />
                  <a
                    onClick={() => history.push(`/doc/policy-terms`)}
                    className="text-primary"
                  >
                    이용약관
                  </a>{" "}
                  및{" "}
                  <a
                    onClick={() => history.push(`/doc/policy-privacy`)}
                    className="text-primary"
                  >
                    개인정보취급방침
                  </a>
                  에 동의하게 됩니다.
                </p>
              </div>
              <p className="clearfix">
                <br />
                <br />
              </p>
            </div>
          </>
        );
      }
    }
  )
);
