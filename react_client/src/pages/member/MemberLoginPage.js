/* eslint-disable jsx-a11y/anchor-is-valid */
import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";

export default inject(
  "ValidStore",
  "MemberStore",
  "SNSStore"
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
          validEmail: true,
          validPassword: true,
        };
      }
      /********** ********** ********** ********** **********/
      /** functions **/
      /********** ********** ********** ********** **********/
      login = async () => {
        const {
          ValidStore: { isEmail, isPassword },
          MemberStore: { REST_POST_login },
          history,
        } = this.props;
        const { email, password } = this.state;

        if (email === "" || !isEmail(email)) {
          this.setState({ validEmail: false });
          return;
        } else {
          this.setState({ validEmail: true });
        }
        if (password === "" || !isPassword(password)) {
          this.setState({ validPassword: false });
          return;
        } else {
          this.setState({ validPassword: true });
        }

        const resolve = await REST_POST_login(email, password);
        if (resolve !== null) {
          history.push(`/main/my`);
        } else {
          this.setState({ validPassword: false });
        }
      };

      /********** ********** ********** ********** **********/
      /** render **/
      /********** ********** ********** ********** **********/
      render() {
        const { history, SNSStore } = this.props;
        return (
          <>
            {/** Navigation */}
            <Navigation backgroundTheme={"blank"} visibleBackIcon={true} />

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
                      id="inputName"
                      placeholder="이메일"
                      value={this.state.email}
                      onChange={(e) => this.setState({ email: e.target.value })}
                    />
                    {!this.state.validEmail && (
                      <p className="text-muted">
                        <small className="red">이메일을 확인해주세요.</small>
                      </p>
                    )}
                  </div>
                  <div className="form-group">
                    <label htmlFor="inputPhone" className="sr-only">
                      비밀번호
                    </label>
                    <input
                      ref={this.password}
                      type="password"
                      className="form-control"
                      id="inputPhone"
                      placeholder="비밀번호 (영문/숫자/특수문자 조합, 8~15자 이내)"
                      value={this.state.password}
                      onChange={(e) =>
                        this.setState({
                          password: e.target.value.substr(0, 15),
                        })
                      }
                    />
                    {!this.state.validPassword && (
                      <p className="text-muted">
                        <small className="red">비밀번호를 확인해주세요.</small>
                      </p>
                    )}
                  </div>
                </form>
                <a
                  onClick={this.login}
                  className="btn btn-primary btn-lg btn-block"
                >
                  로그인
                </a>
                <p className="text-center mt-3">
                  <a onClick={() => history.push(`/member/findpw`)}>
                    <small className="grey">비밀번호를 잊으셨나요?</small>
                  </a>
                </p>
                <a
                  onClick={() => history.push(`/member/signup`)}
                  className="btn btn-grey btn-lg btn-block mt-4"
                >
                  이메일로 가입하기
                </a>

                <div className="row no-gutters-md row-padding mt-3">
                  <div className="col-3">
                    <a
                      onClick={() => SNSStore.authorizeKakao()}
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
                      onClick={() => SNSStore.authorizeFacebook()}
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
                      onClick={() => SNSStore.authorizeNaver()}
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
                      onClick={() => SNSStore.authorizeApple()}
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
                    onClick={() => history.push(`/docs/policy-terms`)}
                    className="text-primary"
                  >
                    이용약관
                  </a>{" "}
                  및{" "}
                  <a
                    onClick={() => history.push(`/docs/policy-privacy`)}
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
