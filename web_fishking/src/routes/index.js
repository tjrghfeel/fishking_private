/* global Kakao, FB, naver, AppleID, $ */
import React, { useEffect } from "react";
import { inject, observer } from "mobx-react";
import {
  BrowserRouter,
  Switch,
  Route,
  Router,
  withRouter,
} from "react-router-dom";
import Components from "../components";

import CustRoute from "./cust";
import SmartSailRoute from "./smartsail";
import SmartFishingRoute from "./smartfishing";
import PoliceRoute from "./police";
import CommonRoute from "./common";
import BlankPage from "../pages/BlankPage";

const {
  MODAL: {
    AlertModal,
    ConfirmModal,
    CouponModal,
    SelectModal,
    SNSModal,
    InputModal,
  },
  LAYOUT: { LoadingLayout },
} = Components;

export default inject(
  "PageStore",
  "ModalStore"
)(
  observer(
    withRouter(({ PageStore, ModalStore }) => {
      useEffect(() => {
        window.deploymessage = "Refresh를 위한 스크롤 이벤트 추가";
        window.addEventListener("scroll", () => {
          if (window.pageYOffset === 0) {
            window.ReactNativeWebView?.postMessage(
              JSON.stringify({ process: "Refresh", data: { enabled: "Y" } })
            );
          } else {
            window.ReactNativeWebView?.postMessage(
              JSON.stringify({ process: "Refresh", data: { enabled: "N" } })
            );
          }
        });
        window.addEventListener("message", (message) => {
          const data = message.data;
          if (data === "goBack") {
            PageStore.goBack();
          }
        });
        (async () => {
          // # 카카오 라이브러리
          await PageStore.injectScript("/assets/cust/js/kakao.min.js", {
            global: true,
          });
          Kakao.init(process.env.REACT_APP_KAKAO_JAVASCRIPT_KEY);
          // # 페이스북 라이브러리
          window.fbAsyncInit = function () {
            FB.init({
              appId: process.env.REACT_APP_FACEBOOK_APP_ID,
              cookie: true,
              xfbml: true,
              version: "v9.0",
            });
            FB.AppEvents.logPageView();
          };
          (function (d, s, id) {
            var js,
              fjs = d.getElementsByTagName(s)[0];
            if (d.getElementById(id)) {
              return;
            }
            js = d.createElement(s);
            js.id = id;
            js.src = "https://connect.facebook.net/ko_KR/sdk.js";
            fjs.parentNode.insertBefore(js, fjs);
          })(document, "script", "facebook-jssdk");
          // # 네이버 라이브러리
          await PageStore.injectScript(
            "/assets/cust/js/naveridlogin_js_sdk_2.0.0.js",
            {
              global: true,
            }
          );
          let button = document.createElement("div");
          button.style = "display:none;";
          button.id = "naverIdLogin";
          document.querySelector("body").appendChild(button);
          new naver.LoginWithNaverId({
            clientId: process.env.REACT_APP_NAVER_CLIENT_ID,
            callbackUrl: process.env.REACT_APP_NAVER_REDIRECT_URI,
            isPopup: false,
            loginButton: {
              color: "green",
              type: 3,
              height: 60,
            } /* 로그인 버튼의 타입을 지정 */,
          }).init();
          // # 애플 라이브러리
          await PageStore.injectScript(
            "https://appleid.cdn-apple.com/appleauth/static/jsapi/appleid/1/en_US/appleid.auth.js",
            { global: true }
          );
          button = document.createElement("div");
          button.style = "display:none;";
          button.id = "appleid-signin";
          button.setAttribute("data-color", "black");
          button.setAttribute("data-border", "true");
          button.setAttribute("data-type", "sign in");
          document.querySelector("body").appendChild(button);
          AppleID.auth.init({
            clientId: process.env.REACT_APP_APPLE_CLIENT_ID,
            scope: "email",
            redirectURI: process.env.REACT_APP_APPLE_REDIRECT_URI,
            state: null,
            nonce: null,
            usePopup: false,
          });
        })();
      }, []);
      return (
        <React.Fragment>
          <Switch>
            {/** 빈화면 */}
            <Route exact path={`/`} component={BlankPage} />

            {/** 어복황제 */}
            <Route path={`/cust`} component={CustRoute} />

            {/** 승선 */}
            <Route path={`/smartsail`} component={SmartSailRoute} />

            {/** 출조 */}
            <Route path={`/smartfishing`} component={SmartFishingRoute} />

            {/** 해경 */}
            <Route path={`/police`} component={PoliceRoute} />

            {/** 공통 */}
            <Route path={`/common`} component={CommonRoute} />
          </Switch>
          <LoadingLayout />
          <AlertModal />
          <ConfirmModal />
          <SelectModal />
          <CouponModal />
          <SNSModal />
          <InputModal />
        </React.Fragment>
      );
    })
  )
);
