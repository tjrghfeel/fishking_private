/* global $ */
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
  "DataStore",
    "ModalStore"
)(
  observer(
    withRouter(
      class extends React.Component {
        constructor(props) {
          super(props);
          this.form = React.createRef(null);
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
            memberId: "",
          };
        }
        /********** ********** ********** ********** **********/
        /** function */
        /********** ********** ********** ********** **********/
        componentDidMount() {
          const { PageStore, ModalStore } = this.props;
          const {
            memberId = null,
            restore,
            iscompany = null,
              msg = null
          } = PageStore.getQueryParams();
          this.setState({ iscompany });
          const saved = localStorage.getItem("@signup-save") || null;
          if (memberId !== null) this.setState({ memberId });

          if (restore === "Y" && saved !== null) {
            const state = JSON.parse(saved);
            this.setState({
              ...state,
              password: state.pw,
              rePassword: state.pw,
              active: 3,
            });
          }
          
          if(msg != null){
              if(msg === 'niceResultParsingError'){ModalStore.openModal("Alert", { body: "??????????????? ?????????????????????" });}
              else if(msg === 'inValidSignUpValue'){ModalStore.openModal("Alert", { body: "????????? ??????????????? ???????????? ????????????" });}
              else if(msg === 'dupPhone'){ModalStore.openModal("Alert", { body: "?????? ????????? ????????? ???????????????" });}
              else if(msg === 'certificationFail'){ModalStore.openModal("Alert", { body: "??????????????? ?????????????????????" });}
          }
        }

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
            const {ModalStore} = this.props;
          const checked1 = this.active1_check1.current.checked;
          const checked2 = this.active1_check2.current.checked;

          if (checked1 && checked2) {this.setState({ active: 2 }); }
          else{ ModalStore.openModal("Alert", { body: "?????? ??????????????? ????????? ???????????????" });}
        };

        goToActive3 = async () => {
          const { APIStore, DataStore, ModalStore } = this.props;
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
             ModalStore.openModal("Alert", { body: "???????????? ???????????????." });
            return;
          }
          const checkNickNameDup = await APIStore._get(
            "/v2/api/signUp/checkNickNameDup",
            { nickName }
          );
          if (checkNickNameDup) {
            this.nickName.current?.classList.add("is-invalid");
              ModalStore.openModal("Alert", { body: "???????????? ???????????????." });
            return;
          }
          this.setState({ active: 3 });
        };

        requestPass = () => {
          const { PageStore } = this.props;
          const { memberId = null } = PageStore.getQueryParams();
          let json = {
              memberId : memberId,
              email: this.state.email,
              pw: this.state.password,
              nickName : this.state.nickName
          }
          localStorage.setItem(
            "@signup-save",
            JSON.stringify(json)
          );
          this.form.current.submit();
        };
        /********** ********** ********** ********** **********/
        /** render */
        /********** ********** ********** ********** **********/
        render() {
          const { PageStore } = this.props;
          const { memberId = null } = PageStore.getQueryParams();
          return (
            <React.Fragment>
              <form
                method={"POST"}
                ref={this.form}
                action={
                  this.state.iscompany == null
                    ? process.env.REACT_APP_PASS_REDIRECT_URI
                    : process.env.REACT_APP_SMARTFISHING_PASS_REDIRECT_URI
                }
              >
                <input
                  type={"hidden"}
                  name={"email"}
                  value={this.state.email}
                />
                <input
                  type={"hidden"}
                  name={"pw"}
                  value={this.state.password}
                />
                <input
                  type={"hidden"}
                  name={"nickName"}
                  value={this.state.nickName}
                />
                <input
                  type={"hidden"}
                  name={"memberId"}
                  value={memberId || ""}
                />
              </form>

              {/** Navigation */}
              <NavigationLayout title={"????????????"} showBackIcon={true} />

              {/** ?????? */}
              <div className="container nopadding mt-1">
                <h5 className="text-center">
                  <img
                    src={
                      this.state.iscompany == null
                        ? "/assets/cust/img/svg/logo.svg"
                        : "/assets/smartfishing/img/svg/logo.svg"
                    }
                    alt=""
                  />
                </h5>
                <div className="text-right">
                  <div className="pay-bg">
                    <ol className="pay-step">
                      <li className={this.state.active === 1 ? "active" : ""}>
                        1. ????????????
                      </li>
                      <li className={this.state.active === 2 ? "active" : ""}>
                        2. ????????????
                      </li>
                      <li className={this.state.active === 3 ? "active" : ""}>
                        3. ????????????
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
                        <strong>????????????</strong>
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
                        ???????????? ?????? <small className="red">(??????)</small>
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
                        ???????????? ?????? ??? ????????????{" "}
                        <small className="red">(??????)</small>
                        <br />
                        <small className="grey">
                          ?????? ??? ????????? ????????? ?????? ?????? ???????????? ??????
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
                        ???????????? ?????? ??? ????????????{" "}
                        <small className="grey">(??????)</small>
                        <br />
                        <small className="grey">
                          ????????? ?????? ?????? ?????? ????????? ?????? ???????????? ?????? ???
                          ??????
                        </small>
                      </span>
                    </label>
                    <hr className="full mt-2 mb-3" />
                    <small>
                      ??????????????? ????????? ???14??? ???????????? ???????????? ?????????.
                      <br />
                      ?????? ????????? ???????????? ????????? ???????????? ????????? ??? ????????????.{" "}
                    </small>
                    <div className="form-group mt-4">
                      <a
                        onClick={this.goToActive2}
                        className="btn btn-primary btn-lg btn-block"
                      >
                        ??????
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
                          ?????????
                        </label>
                        <input
                          ref={this.email}
                          type="email"
                          className="form-control"
                          placeholder="?????????"
                          value={this.state.email}
                          onChange={(e) =>
                            this.setState({ email: e.target.value })
                          }
                        />
                      </div>
                      <div className="form-group">
                        <label htmlFor="inputPassword" className="sr-only">
                          ????????????
                        </label>
                        <input
                          ref={this.password}
                          type="password"
                          className="form-control"
                          placeholder="???????????? (??????/??????/???????????? ??????, 8~15??? ??????)"
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
                          ???????????? ??????
                        </label>
                        <input
                          ref={this.rePassword}
                          type="password"
                          className="form-control"
                          placeholder="???????????? ??????"
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
                          ?????????
                        </label>
                        <input
                          ref={this.nickName}
                          type="text"
                          className="form-control"
                          placeholder="?????????"
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
                          ????????????
                        </a>
                      </div>
                    </form>
                  </div>
                </div>
              )}

              {this.state.active === 3 && (
                <div className="container nopadding mt-4">
                  <p className="text-center">
                    ?????? ??????, ?????? ??????, ????????? ?????? ?????? ???????????? ??????????????????
                    <br />
                    ?????? ????????? ?????? ????????? ????????? ???????????????.
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
                            ????????? ?????? ??????
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
