import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";

export default inject(
  "PageStore",
  "ModalStore",
  "NativeStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.id = React.createRef(null);
        this.password = React.createRef(null);
        this.state = {
          id: "",
          password: "",
          auto: "N",
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      onClickLogin = async () => {
        const { id, password } = this.state;
        if (id === "") {
          this.id.current?.classList.add("is-invalid");
          return;
        } else {
          this.id.current?.classList.remove("is-invalid");
        }
        if (password === "") {
          this.password.current?.classList.add("is-invalid");
          return;
        } else {
          this.password.current?.classList.remove("is-invalid");
        }

        const { ModalStore, PageStore } = this.props;
        ModalStore.openModal("Alert", {
          title: "로그인 안내",
          body: (
            <React.Fragment>
              <p>
                ID/PW가 일치하지 않습니다. <br />
                ㈜투비에서 제공한 해경용 아이디를 <br />
                확인하신 후 다시 시도해 주세요.
                <br />
                (문의: 하단 고객센터)
              </p>
            </React.Fragment>
          ),
          onOk: () => {
            PageStore.setAccessToken("a", "police", this.state.auto);
            PageStore.push(`/dashboard`);
          },
        });
      };
      onClickKakao = async () => {};
      onClickCall = async () => {
        const { ModalStore, NativeStore } = this.props;
        ModalStore.openModal("Confirm", {
          title: "전화걸기",
          body: (
            <React.Fragment>
              <p>
                고객센터로 전화연결 하시겠습니까?
                <br />
                365일 10시 ~ 18시
                <br />
                (점심시간 12시 ~ 13시 30분)
              </p>
            </React.Fragment>
          ),
          textOk: "통화",
          onOk: () =>
            NativeStore.linking("tel:".concat(process.env.REACT_APP_CS_PHONE)),
        });
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <nav className="navbar fixed-top navbar-dark">
              <span className="navbar-title"></span>
            </nav>

            <div className="container nopadding mt-1">
              <h5 className="text-center">
                <img src="/assets/police/img/svg/logo-smartmarine.svg" alt="" />
              </h5>
            </div>

            {/** 입력 */}
            <div className="container nopadding">
              <div className="mt-4">
                <form className="form-line mt-	1">
                  <div className="form-group">
                    <label htmlFor="inputName" className="sr-only">
                      아이디
                    </label>
                    <input
                      ref={this.id}
                      type="text"
                      className="form-control"
                      placeholder="아이디를 입력해 주세요."
                      value={this.state.id}
                      onChange={(e) => this.setState({ id: e.target.value })}
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
                      placeholder="비밀번호를 입력해 주세요."
                      value={this.state.password}
                      onChange={(e) =>
                        this.setState({ password: e.target.value })
                      }
                    />
                  </div>

                  <div className="form-group ">
                    <label className="control checkbox">
                      <input
                        type="checkbox"
                        className="add-contrast"
                        data-role="collar"
                        onChange={(e) =>
                          this.setState({ auto: e.target.checked ? "Y" : "N" })
                        }
                      />
                      <span className="control-indicator"></span>
                      <span className="control-text">자동 로그인</span>
                    </label>
                  </div>
                </form>
                <a
                  onClick={this.onClickLogin}
                  className="btn btn-primary btn-lg btn-block"
                >
                  로그인
                </a>

                <p className="text-center mt-4">
                  ㈜투비에서 제공한 해경용 아이디로 로그인 후 이용해 주시기
                  바랍니다.{" "}
                </p>
              </div>
              <p className="clearfix"></p>
            </div>
            <div className="space mb-4"></div>

            <div className="container nopadding">
              <h6 className="text-center mt-3">고객센터</h6>
              <p className="text-center">
                <small className="grey">
                  365일 10시~18시 운영 (점심시간 12시 ~ 13시 30분)
                </small>
              </p>
              <div className="row no-gutters no-gutters-cs d-flex align-items-center mt-4">
                <div className="col-6">
                  <a
                    onClick={this.onClickKakao}
                    className="btn btn-yellow btn-round btn-lg btn-block cs-padding"
                  >
                    <img
                      src="/assets/police/img/svg/icon-talk.svg"
                      alt="Set"
                      className="vam mr-1"
                    />
                    카카오 상담톡
                  </a>
                </div>
                <div className="col-6" style={{ paddingRight: "0px" }}>
                  <a
                    onClick={this.onClickCall}
                    className="btn btn-grey btn-round btn-lg btn-block cs-padding"
                    data-toggle="modal"
                    data-target="#callModal"
                  >
                    <img
                      src="/assets/police/img/svg/icon-call.svg"
                      alt="Set"
                      className="vam mr-1"
                    />
                    {process.env.REACT_APP_CS_PHONE}
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
