/* global Kakao, FB, naver, AppleID */
import React, { useEffect } from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";
import Components from "../components";

import CustRoute from "./cust";
import PoliceRoute from "./police";

const {
  MODAL: { AlertModal, ConfirmModal, CouponModal, SelectModal, SNSModal },
  LAYOUT: { LoadingLayout },
} = Components;

export default inject("PageStore")(
  observer(({ PageStore }) => {
    useEffect(() => {
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
      <BrowserRouter>
        <Switch>
          {/** 어복황제 */}
          <Route path={`/cust`} component={CustRoute} />

          {/** 해경 */}
          <Route path={`/police`} component={PoliceRoute} />

          {/** 기본 리디렉션 */}
          <Redirect from={`*`} to={`/cust/main/home`} />
        </Switch>
        <LoadingLayout />
        <AlertModal />
        <ConfirmModal />
        <SelectModal />
        <CouponModal />
        <SNSModal />
      </BrowserRouter>
    );
  })
);
