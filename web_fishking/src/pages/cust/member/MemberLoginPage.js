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
            ModalStore.openModal("Alert", {body: "ID/PW를 확인해주세요."});
          }
        }
      };

      componentWillUnmount() {
          //render함수 첫 부분에 주석 참고.
          document.getElementsByTagName("body")[0].style.cssText=""
          document.getElementsByTagName("body")[0].style.setProperty("margin","0 auto");
          document.getElementsByTagName("body")[0].style.setProperty("padding-top", "43px", "important")
          document.getElementsByTagName("body")[0].style.setProperty("padding-bottom","45px","important")
          document.getElementsByTagName("body")[0].style.maxWidth = "640px";
      }

        // onLoginBySNS = (loginType) => {
      //   if (loginType === "kakao") {
      //     window.location.href =
      //       "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" +
      //       process.env["REACT_APP_KAKAO_JAVASCRIPT_KEY"] +
      //       "&redirect_uri=" +
      //       process.env["REACT_APP_KAKAO_REDIRECT_URI"];
      //   } else if (loginType === "facebook") {
      //     window.location.href =
      //       "https://www.facebook.com/v9.0/dialog/oauth?client_id=" +
      //       process.env["REACT_APP_FACEBOOK_APP_ID"] +
      //       "&redirect_uri=" +
      //       process.env["REACT_APP_FACEBOOK_REDIRECT_URI"] +
      //       "&state=";
      //   } else if (loginType === "naver") {
      //     window.location.href =
      //       "https://nid.naver.com/oauth2.0/authorize?client_id=" +
      //       process.env["REACT_APP_NAVER_CLIENT_ID"] +
      //       "&response_type=code&redirect_uri=" +
      //       process.env["REACT_APP_NAVER_REDIRECT_URI"] +
      //       "&state=";
      //   } else if (loginType === "apple") {
      //     AppleID.auth.signIn();
      //   }
      // };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;

        //어복2차 ui수정으로, 로그인 페이지 ui변경. 이때, 배경화면이 화면 전체에 꽉 차게 디자인이 나옴. 근데 어복 페이지 body태그 전체에
        //margin, padding, max-width 설정으로 인해 전체 채우기가 안됨. 하여 로그인 페이지 render시 이 부분을 수정하고 로그인 페이지
        //컴포넌트 unmount시 이 설정을 되돌려주고 있다. (componentWillUnmount에서 설정 되돌리고 있음)
        document.getElementsByTagName("body")[0].style.margin = "0";
        document.getElementsByTagName("body")[0].style.setProperty("padding-top", "0", "important")
        document.getElementsByTagName("body")[0].style.setProperty("padding-bottom","0","important")
        document.getElementsByTagName("body")[0].style.setProperty("max-width","none");

        return (
          <React.Fragment>
            <div className="loginPage_content">
                {/** Navigation */}
                <NavigationLayout transparent={true} showBackIcon={true} />

                <div className="loginPage_logo">
                  <a href="#">
                    <img src="/assets/cust/img/logo.png" alt="어복황제 로고"/>
                    <h1>어복황제</h1>
                  </a>
                </div>
                <h4>VER 2.0.0</h4>
                <div className="loginPage_input_box">
                  <input
                      type="text" name="email" id="email" placeholder="이메일"
                      ref={this.memberId}
                      value={this.state.memberId}
                      onChange={(e) =>
                          this.setState({ memberId: e.target.value })
                      }
                  />
                  <input
                      type="password"
                      name="pw"
                      id="pw"
                      placeholder="비밀번호 (영문/숫자/특수문자 조합, 8~15자 이내)"
                      value={this.state.password}
                      onChange={(e) =>
                          this.setState({
                              password: e.target.value.substr(0, 15),
                          })
                      }
                  />
                  <div className="loginPage_submit_btn" onClick={this.onLogin}>로 그 인</div>
                  <div className="loginPage_bottom_btns">
                    <p onClick={() => PageStore.push(`/member/signup`)}>어복황제 가입하기</p>
                    <p onClick={() => PageStore.push(`/member/findpw`)}>아이디 확인 및 비밀번호 재설정</p>
                  </div>

                </div>

            </div>

            {/* 정보*/ }
            {/*<div className="container nopadding mt-1">*/}
            {/*  <h5 className="text-center">*/}
            {/*    <img src="/assets/cust/img/svg/logo.svg" alt="" />*/}
            {/*  </h5>*/}
            {/*</div>*/}

            {/*입력*/}
            {/*<div class="container nopadding">*/}
            {/*  <div class="mt-4">*/}
            {/*    <form class="form-line mt-	1">*/}
            {/*      <div class="form-group">*/}
            {/*        <label for="inputName" class="sr-only">*/}
            {/*          이메일*/}
            {/*        </label>*/}
            {/*        <input*/}
            {/*          ref={this.memberId}*/}
            {/*          type="email"*/}
            {/*          class="form-control"*/}
            {/*          placeholder="이메일"*/}
            {/*          value={this.state.memberId}*/}
            {/*          onChange={(e) =>*/}
            {/*            this.setState({ memberId: e.target.value })*/}
            {/*          }*/}
            {/*        />*/}
            {/*      </div>*/}
            {/*      <div class="form-group">*/}
            {/*        <label for="inputPhone" class="sr-only">*/}
            {/*          비밀번호*/}
            {/*        </label>*/}
            {/*        <input*/}
            {/*          type="password"*/}
            {/*          class="form-control"*/}
            {/*          placeholder="비밀번호 (영문/숫자/특수문자 조합, 8~15자 이내)"*/}
            {/*          value={this.state.password}*/}
            {/*          onChange={(e) =>*/}
            {/*            this.setState({*/}
            {/*              password: e.target.value.substr(0, 15),*/}
            {/*            })*/}
            {/*          }*/}
            {/*        />*/}
            {/*      </div>*/}
            {/*    </form>*/}
            {/*    <a*/}
            {/*      onClick={this.onLogin}*/}
            {/*      class="btn btn-primary btn-lg btn-block"*/}
            {/*    >*/}
            {/*      로그인*/}
            {/*    </a>*/}
            {/*    <p class="text-center mt-3">*/}
            {/*      <a onClick={() => PageStore.push(`/member/findpw`)}>*/}
            {/*        <small class="grey">아이디확인 및 비밀번호재설정</small>*/}
            {/*      </a>*/}
            {/*    </p>*/}
            {/*    <a*/}
            {/*      onClick={() => PageStore.push(`/member/signup`)}*/}
            {/*      class="btn btn-grey btn-lg btn-block mt-4"*/}
            {/*    >*/}
            {/*      이메일로 가입하기*/}
            {/*    </a>*/}

                {/*<div class="row mt-3">*/}
                {/*  <div class="col-12 mb-2">*/}
                {/*    <a*/}
                {/*      onClick={() => this.onLoginBySNS("kakao")}*/}
                {/*      class="btn btn-sns-kakao btn-yellow btn-lg btn-block"*/}
                {/*      style={{padding: '11px 13px', fontSize:14}}*/}
                {/*    >*/}
                {/*      <img*/}
                {/*        src="/assets/cust/img/svg/icon-sns-kakao.svg"*/}
                {/*        alt=""*/}
                {/*        class="vam"*/}
                {/*        style={{float:'left', height:'100%', margin:'auto'}}*/}
                {/*      />*/}
                {/*      &nbsp;카카오로 로그인*/}
                {/*    </a>*/}
                {/*  </div>*/}
                {/*  <div class="col-12 mb-2">*/}
                {/*    <a*/}
                {/*      onClick={() => this.onLoginBySNS("facebook")}*/}
                {/*      class="btn btn-sns-facebook btn-lg btn-block"*/}
                {/*      style={{padding: '11px 13px', fontSize:14}}*/}
                {/*    >*/}
                {/*      <img*/}
                {/*        src="/assets/cust/img/svg/icon-sns-facebook.svg"*/}
                {/*        alt=""*/}
                {/*        class="vam"*/}
                {/*        style={{float:'left', height:'100%', margin:'auto'}}*/}
                {/*      />*/}
                {/*      &nbsp;Facebook으로 로그인*/}
                {/*    </a>*/}
                {/*  </div>*/}
                {/*  <div class="col-12 mb-2">*/}
                {/*    <a*/}
                {/*      onClick={() => this.onLoginBySNS("naver")}*/}
                {/*      class="btn btn-sns-naver btn-lg btn-block"*/}
                {/*      style={{padding: '11px 13px', fontSize:14}}*/}
                {/*    >*/}
                {/*      <img*/}
                {/*        src="/assets/cust/img/svg/icon-sns-naver.svg"*/}
                {/*        alt=""*/}
                {/*        class="vam"*/}
                {/*        style={{float:'left', height:'100%', margin:'auto'}}*/}
                {/*      />*/}
                {/*      &nbsp;네이버로 로그인*/}
                {/*    </a>*/}
                {/*  </div>*/}
                {/*  <div class="col-12 mb-2">*/}
                {/*    <a*/}
                {/*      onClick={() => this.onLoginBySNS("apple")}*/}
                {/*      class="btn btn-sns-apple btn-lg btn-block"*/}
                {/*      style={{padding: '11px 13px', fontSize:14}}*/}
                {/*    >*/}
                {/*      <img*/}
                {/*        src="/assets/cust/img/svg/icon-sns-apple.svg"*/}
                {/*        alt=""*/}
                {/*        class="vam"*/}
                {/*        style={{float:'left', height:'100%', margin:'auto'}}*/}
                {/*      />*/}
                {/*      &nbsp;Apple로 로그인*/}
                {/*    </a>*/}
                {/*  </div>*/}
                {/*</div>*/}
            {/*    <p class="text-center mt-4">*/}
            {/*      어복황제 로그인/회원가입시 <br />*/}
            {/*      <a*/}
            {/*        onClick={() => PageStore.push(`/policy/terms`)}*/}
            {/*        class="text-primary"*/}
            {/*      >*/}
            {/*        이용약관*/}
            {/*      </a>{" "}*/}
            {/*      및{" "}*/}
            {/*      <a*/}
            {/*        onClick={() => PageStore.push(`/policy/privacy`)}*/}
            {/*        class="text-primary"*/}
            {/*      >*/}
            {/*        개인정보취급방침*/}
            {/*      </a>*/}
            {/*      에 동의하게 됩니다.*/}
            {/*    </p>*/}
            {/*  </div>*/}
            {/*  <p class="clearfix">*/}
            {/*    <br />*/}
            {/*    <br />*/}
            {/*  </p>*/}
            {/*</div>*/}
          </React.Fragment>
        );
      }
    }
  )
);
