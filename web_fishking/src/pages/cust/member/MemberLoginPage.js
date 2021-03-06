/* global AppleID, Kakao */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
import ModalStore from "../../../stores/ModalStore";
const {
  LAYOUT: { NavigationLayout },
} = Components;

export default inject(
  "DataStore",
  "APIStore",
  "PageStore",
  "ModalStore"
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
      componentDidMount() {
        const { PageStore } = this.props;
        if (PageStore.loggedIn) PageStore.goBack();
      }

      onLogin = async () => {
        const { DataStore, APIStore, PageStore, ModalStore } = this.props;
        const { memberId, password } = this.state;

        if (!DataStore.isEmail(memberId)) {
          this.memberId.current?.classList.add("is-invalid");
          return;
        } else {
          this.memberId.current?.classList.remove("is-invalid");
        }
        // if (!DataStore.isPassword(password)) {
        //   this.password.current?.classList.add("is-invalid");
        //   return;
        // } else {
        //   this.password.current?.classList.remove("is-invalid");
        // }

        try {
          const response = await APIStore._post("/v2/api/login", {
            memberId,
            password,
            registrationToken: window.fcm_token || null,
          });
          if (response) {
            const {token, auth, memberId} = response;
            if (!auth) {
              window.location.href = `/v2/api/niceRequest?memberId=${memberId}`;
            }else{
              PageStore.setAccessToken(token, "cust", "Y");
              const url = sessionStorage.getItem("@redirect-url");
              if (url === null) {
                PageStore.push(`/main/my`);
              } else {
                sessionStorage.removeItem("@redirect-url");
                window.location.href = url;
                return;
              }
            }
          }
        } catch (err) {
          if(err.response.data.msg !== undefined){
            ModalStore.openModal("Alert", { body: err.response.data.msg })
          }
          else {
            ModalStore.openModal("Alert", {body: "ID/PW??? ??????????????????."});
          }
        }
      };

      onLoginBySNS = (loginType) => {
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
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            {/** Navigation */}
            <NavigationLayout transparent={true} showBackIcon={true} />

            {/** ?????? */}
            <div className="container nopadding mt-1">
              <h5 className="text-center">
                <img src="/assets/cust/img/svg/logo.svg" alt="" />
              </h5>
            </div>

            {/** ?????? */}
            <div class="container nopadding">
              <div class="mt-4">
                <form class="form-line mt-	1">
                  <div class="form-group">
                    <label for="inputName" class="sr-only">
                      ?????????
                    </label>
                    <input
                      ref={this.memberId}
                      type="email"
                      class="form-control"
                      placeholder="?????????"
                      value={this.state.memberId}
                      onChange={(e) =>
                        this.setState({ memberId: e.target.value })
                      }
                    />
                  </div>
                  <div class="form-group">
                    <label for="inputPhone" class="sr-only">
                      ????????????
                    </label>
                    <input
                      type="password"
                      class="form-control"
                      placeholder="???????????? (??????/??????/???????????? ??????, 8~15??? ??????)"
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
                  ?????????
                </a>
                <p class="text-center mt-3">
                  <a onClick={() => PageStore.push(`/member/findpw`)}>
                    <small class="grey">??????????????? ??? ?????????????????????</small>
                  </a>
                </p>
                <a
                  onClick={() => PageStore.push(`/member/signup`)}
                  class="btn btn-grey btn-lg btn-block mt-4"
                >
                  ???????????? ????????????
                </a>

                <div class="row no-gutters-md row-padding mt-3">
                  <div class="col-3">
                    <a
                      onClick={() => this.onLoginBySNS("kakao")}
                      class="btn btn-sns-kakao btn-yellow btn-lg btn-block"
                      style={{padding: '11px 13px'}}
                    >
                      <img
                        src="/assets/cust/img/svg/icon-sns-kakao.svg"
                        alt=""
                        class="vam"
                      />
                      &nbsp;??????
                    </a>
                  </div>
                  <div class="col-3">
                    <a
                      onClick={() => this.onLoginBySNS("facebook")}
                      class="btn btn-sns-facebook btn-lg btn-block"
                      style={{padding: '11px 13px'}}
                    >
                      <img
                        src="/assets/cust/img/svg/icon-sns-facebook.svg"
                        alt=""
                        class="vam"
                      />
                      &nbsp;??????
                    </a>
                  </div>
                  <div class="col-3">
                    <a
                      onClick={() => this.onLoginBySNS("naver")}
                      class="btn btn-sns-naver btn-lg btn-block"
                      style={{padding: '11px 13px'}}
                    >
                      <img
                        src="/assets/cust/img/svg/icon-sns-naver.svg"
                        alt=""
                        class="vam"
                      />
                      &nbsp;??????
                    </a>
                  </div>
                  <div class="col-3">
                    <a
                      onClick={() => this.onLoginBySNS("apple")}
                      class="btn btn-sns-apple btn-lg btn-block"
                      style={{padding: '11px 13px'}}
                    >
                      <img
                        src="/assets/cust/img/svg/icon-sns-apple.svg"
                        alt=""
                        class="vam"
                      />
                      &nbsp;??????
                    </a>
                  </div>
                </div>
                <p class="text-center mt-4">
                  ???????????? ?????????/??????????????? <br />
                  <a
                    onClick={() => PageStore.push(`/policy/terms`)}
                    class="text-primary"
                  >
                    ????????????
                  </a>{" "}
                  ???{" "}
                  <a
                    onClick={() => PageStore.push(`/policy/privacy`)}
                    class="text-primary"
                  >
                    ????????????????????????
                  </a>
                  ??? ???????????? ?????????.
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
