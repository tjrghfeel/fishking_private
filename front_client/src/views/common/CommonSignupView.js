import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layouts/Navigation";
import Http from "../../Http";

export default inject(
  "DataStore",
  "AlertStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.stage1_chk1 = React.createRef(null);
        this.stage1_chk2 = React.createRef(null);
        this.stage1_chk3 = React.createRef(null);
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
      /** functions */
      /********** ********** ********** ********** **********/
      stage1_check_all = (checked) => {
        const stage1_chk1 = this.stage1_chk1.current;
        const stage1_chk2 = this.stage1_chk2.current;
        const stage1_chk3 = this.stage1_chk3.current;
        stage1_chk1.checked = checked;
        stage1_chk2.checked = checked;
        stage1_chk3.checked = checked;
      };
      stage1_submit = () => {
        const checked1 = this.stage1_chk1.current?.checked;
        const checked2 = this.stage1_chk2.current?.checked;
        const checked3 = this.stage1_chk3.current?.checked;

        if (checked1 && checked2) {
          this.setState({ stage: 2 });
        } else {
          this.props.AlertStore.openAlert("필수약관에 동의해주세요.");
        }
      };
      stage2_submit = async () => {
        const { email, password1, password2, nickName } = this.state;
        const {
          DataStore: { isValidEmail, isValidPassword, isValidNickName },
        } = this.props;

        if (email === "" || !isValidEmail(email)) {
          this.email.current?.classList.add("is-invalid");
          return;
        } else {
          this.email.current?.classList.remove("is-invalid");
        }
        if (password1 === "" || !isValidPassword(password1)) {
          this.password1.current?.classList.add("is-invalid");
          return;
        } else {
          this.password1.current?.classList.remove("is-invalid");
        }
        if (password1 !== password2) {
          this.password2.current?.classList.add("is-invalid");
          return;
        } else {
          this.password2.current?.classList.remove("is-invalid");
        }
        if (nickName === "" || !isValidNickName(nickName)) {
          this.nickName.current?.classList.add("is-invalid");
          return;
        } else {
          this.nickName.current?.classList.remove("is-invalid");
        }

        // -> 아이디 중복 확인
        const resolveId = await Http._get("/v2/api/checkUidDup", {
          uid: email,
        });
        console.log(JSON.stringify(resolveId));
        if (resolveId !== 0) {
          this.props.AlertStore.openAlert("중복된 아이디 입니다.");
          return;
        }
        // -> 닉네임 중복 확인

        this.setState({ stage: 3 });
      };
      requestPass = () => {
        // TODO : [PUB-OK/API-NO] 회원가입 : 본인인증 요청
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <>
            {/** Navigation */}
            <Navigation title={"회원가입"} showBack={true} />

            {/** 정보 */}
            <div className="container nopadding mt-1">
              <h5 className="text-center">
                <img src="/assets/img/svg/logo.svg" alt="" />
              </h5>
              <div className="text-right">
                <div className="pay-bg">
                  <ol className="pay-step">
                    <li className={this.state.stage === 1 ? "active" : ""}>
                      1. 약관동의
                    </li>
                    <li className={this.state.stage === 2 ? "active" : ""}>
                      2. 정보입력
                    </li>
                    <li className={this.state.stage === 3 ? "active" : ""}>
                      3. 본인인증
                    </li>
                  </ol>
                </div>
              </div>
            </div>

            {/** 입력 > 1.약관동의 :: START */}
            {this.state.stage === 1 && (
              <div className="container nopadding mt-4">
                <form>
                  <label className="control radio mt-2">
                    <input
                      type="checkbox"
                      className="add-contrast"
                      data-role="collar"
                      onChange={(e) => this.stage1_check_all(e.target.checked)}
                    />
                    <span className="control-indicator"></span>
                    <span className="control-text">
                      <strong>전체동의</strong>
                    </span>
                  </label>
                  <br />
                  <label className="control radio">
                    <input
                      ref={this.stage1_chk1}
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
                      ref={this.stage1_chk2}
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
                      ref={this.stage1_chk3}
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
                      onClick={this.stage1_submit}
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
            {/** 입력 > 1.약관동의 :: END */}

            {/** 입력 > 2.정보입력 :: START */}
            {this.state.stage === 2 && (
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
                        ref={this.password1}
                        type="password"
                        className="form-control"
                        placeholder="비밀번호 (영문/숫자/특수문자 조합, 8~15자 이내)"
                        value={this.state.password1}
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
                        value={this.state.password2}
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
                        value={this.state.nickName}
                        onChange={(e) =>
                          this.setState({ nickName: e.target.value })
                        }
                      />
                    </div>
                    <div className="form-group pt-2">
                      <a
                        onClick={this.stage2_submit}
                        className="btn btn-grey btn-lg btn-block"
                      >
                        가입하기
                      </a>
                    </div>
                  </form>
                </div>
              </div>
            )}
            {/** 입력 > 2.정보입력 :: END */}

            {/** 입력 > 3.본인인증 :: START */}
            {this.state.stage === 3 && (
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
            {/** 입력 > 3.본인인증 :: END */}
          </>
        );
      }
    }
  )
);
