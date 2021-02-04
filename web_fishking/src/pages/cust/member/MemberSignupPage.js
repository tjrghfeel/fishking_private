import React from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router-dom";
import qs from "qs";
import Components from "../../../components";

const {
  LAYOUT: { NavigationLayout },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "DataStore"
)(
  observer(
    withRouter(
      class extends React.Component {
        constructor(props) {
          super(props);
          this.active1_check0 = React.createRef(null);
          this.active1_check1 = React.createRef(null);
          this.active1_check2 = React.createRef(null);
          this.active1_check3 = React.createRef(null);
          this.email = React.createRef(null);
          this.password = React.createRef(null);
          this.rePassword = React.createRef(null);
          this.nickName = React.createRef(null);
          this.state = {
            active: 1,
            email: "",
            password: "",
            rePassword: "",
            nickName: "",
          };
        }
        /********** ********** ********** ********** **********/
        /** function */
        /********** ********** ********** ********** **********/
        toggleCheckbox = () => {
          if (this.active1_check0.current?.checked) {
            this.active1_check1.current.checked = true;
            this.active1_check2.current.checked = true;
            this.active1_check3.current.checked = true;
          } else {
            this.active1_check1.current.checked = false;
            this.active1_check2.current.checked = false;
            this.active1_check3.current.checked = false;
          }
        };

        goToActive2 = () => {
          const checked1 = this.active1_check1.current.checked;
          const checked2 = this.active1_check2.current.checked;

          if (checked1 && checked2) this.setState({ active: 2 });
        };

        goToActive3 = async () => {
          const { APIStore, DataStore } = this.props;
          const { email, password, rePassword, nickName } = this.state;

          if (email === "" || !DataStore.isEmail(email)) {
            this.email.current?.classList.add("is-invalid");
            return;
          } else {
            this.email.current?.classList.remove("is-invalid");
          }
          if (password === "" || !DataStore.isPassword(password)) {
            this.password.current?.classList.add("is-invalid");
            return;
          } else {
            this.password.current?.classList.remove("is-invalid");
          }
          if (password !== rePassword) {
            this.rePassword.current?.classList.add("is-invalid");
            return;
          } else {
            this.rePassword.current?.classList.remove("is-invalid");
          }
          if (nickName === "" || !DataStore.isNickName(nickName)) {
            this.nickName.current?.classList.add("is-invalid");
            return;
          } else {
            this.nickName.current?.classList.remove("is-invalid");
          }

          const checkUidDup = await APIStore._get(
            "/v2/api/signUp/checkUidDup",
            {
              uid: email,
            }
          );
          if (checkUidDup !== 0) {
            this.email.current?.classList.add("is-invalid");
            return;
          }
          const checkNickNameDup = await APIStore._get(
            "/v2/api/signUp/checkNickNameDup",
            { nickName }
          );
          if (checkNickNameDup) {
            this.nickName.current?.classList.add("is-invalid");
            return;
          }
          this.setState({ active: 3 });
        };

        requestPass = () => {
          const { PageStore } = this.props;
          const { memberId = null } = PageStore.getQueryParams();

          if (memberId === null) {
            PageStore.push(`${process.env.REACT_APP_PASS_REDIRECT_URI}`);
          } else {
            PageStore.push(
              `${
                process.env.REACT_APP_PASS_REDIRECT_URI
              }?state=${memberId.substr(0, memberId.indexOf("#"))}`
            );
          }
        };
        /********** ********** ********** ********** **********/
        /** render */
        /********** ********** ********** ********** **********/
        render() {
          return (
            <React.Fragment>
              {/** Navigation */}
              <NavigationLayout title={"회원가입"} showBackIcon={true} />

              {/** 정보 */}
              <div className="container nopadding mt-1">
                <h5 className="text-center">
                  <img src="/assets/cust/img/svg/logo.svg" alt="" />
                </h5>
                <div className="text-right">
                  <div className="pay-bg">
                    <ol className="pay-step">
                      <li className={this.state.active === 1 ? "active" : ""}>
                        1. 약관동의
                      </li>
                      <li className={this.state.active === 2 ? "active" : ""}>
                        2. 정보입력
                      </li>
                      <li className={this.state.active === 3 ? "active" : ""}>
                        3. 본인인증
                      </li>
                    </ol>
                  </div>
                </div>
              </div>

              {/** Active.1 */}
              {this.state.active === 1 && (
                <div className="container nopadding mt-4">
                  <form>
                    <label className="control radio mt-2">
                      <input
                        ref={this.active1_check0}
                        type="checkbox"
                        className="add-contrast"
                        data-role="collar"
                        onClick={this.toggleCheckbox}
                      />
                      <span className="control-indicator"></span>
                      <span className="control-text">
                        <strong>전체동의</strong>
                      </span>
                    </label>
                    <br />
                    <label className="control radio">
                      <input
                        ref={this.active1_check1}
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
                        ref={this.active1_check2}
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
                        ref={this.active1_check3}
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
                          서비스 혜택 등의 정보 제공을 위한 개인정보 수집 및
                          활동
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
                        onClick={this.goToActive2}
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

              {this.state.active === 2 && (
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
                          value={this.state.email}
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
                      <div className="form-group">
                        <label htmlFor="inputPasswordC" className="sr-only">
                          비밀번호 확인
                        </label>
                        <input
                          ref={this.rePassword}
                          type="password"
                          className="form-control"
                          placeholder="비밀번호 확인"
                          value={this.state.rePassword}
                          onChange={(e) =>
                            this.setState({
                              rePassword: e.target.value.substr(0, 15),
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
                          value={this.state.nickName}
                          onChange={(e) =>
                            this.setState({
                              nickName: e.target.value.substr(0, 7),
                            })
                          }
                        />
                      </div>
                      <div className="form-group pt-2">
                        <a
                          onClick={this.goToActive3}
                          className="btn btn-grey btn-lg btn-block"
                        >
                          가입하기
                        </a>
                      </div>
                    </form>
                  </div>
                </div>
              )}

              {this.state.active === 3 && (
                <div className="container nopadding mt-4">
                  <p className="text-center">
                    낚시 예약, 예약 조회, 조행기 작성 등의 서비스를 이용하시려면
                    <br />
                    본인 확인을 위한 인증이 반드시 필요합니다.
                  </p>
                  <div className="row no-gutters mt-5">
                    <div className="col-4 offset-4">
                      <a onClick={this.requestPass}>
                        <div className="card-round-box">
                          <h6 className="text-center text-primary">
                            <img
                              src="/assets/cust/img/svg/icon-phone.svg"
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
            </React.Fragment>
          );
        }
      }
    )
  )
);
