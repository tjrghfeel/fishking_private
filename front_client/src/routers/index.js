/* global Kakao, FB, naver, AppleID */
import React, { useEffect } from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";

import MainRouter from "./MainRouter";
import BoatRouter from "./BoatRouter";
import MyRouter from "./MyRouter";
import StoryRouter from "./StoryRouter";

import SetRouter from "./SetRouter";
import SearchRouter from "./SearchRouter";
import DocRouter from "./DocRouter";
import CommonRouter from "./CommonRouter";

import AlertModal from "../components/modals/AlertModal";

export default inject(
  "AppStore",
  "DOMStore",
  "ViewStore"
)(
  observer(({ AppStore, DOMStore, ViewStore }) => {
    const { loadAppData } = AppStore;
    loadAppData();
    window.addEventListener("message", (e) => {
      if (e.isTrusted) {
        const { type = null, method = null } = e.data || {};
        if (type === "navigate") {
          if (method === "goBack") {
            console.log("goBack");
            ViewStore.goBack();
          }
        }
      }
    });
    /********** ********** ********** ********** **********/
    /** functions */
    /********** ********** ********** ********** **********/
    useEffect(() => {
      const { loadScript } = DOMStore;
      (async () => {
        // # KAKAO 초기화
        await loadScript("/assets/js/kakao.min.js", {
          global: true,
        });
        Kakao.init(process.env.REACT_APP_KAKAO_JAVASCRIPT_KEY);
        // # FACEBOOK 초기화
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
        // # NAVER 초기화
        await loadScript("/assets/js/naveridlogin_js_sdk_2.0.0.js", {
          global: true,
        });
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
        // # APPLE 초기화
        await loadScript(
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
    }, [DOMStore]);
    /********** ********** ********** ********** **********/
    /** render */
    /********** ********** ********** ********** **********/
    return (
      <BrowserRouter>
        <Switch>
          {/** 메인 */}
          <Route path={`/main`} component={MainRouter} />
          {/** 선상 */}
          <Route path={`/boat`} component={BoatRouter} />
          {/** 갯바위 */}
          <Route path={`/rock`} component={BoatRouter} />
          {/** 어복스토리 */}
          <Route path={`/story`} component={StoryRouter} />
          {/** 마이메뉴 */}
          <Route path={`/my`} component={MyRouter} />

          {/** 검색 */}
          <Route path={`/search`} component={SearchRouter} />
          {/** 설정 */}
          <Route path={`/set`} component={SetRouter} />
          {/** 문서 */}
          <Route path={`/doc`} component={DocRouter} />
          {/** 공통 */}
          <Route path={`/common`} component={CommonRouter} />
          {/** 리디렉트 */}
          <Redirect from={`*`} to={`/main/home`} />
        </Switch>
        <AlertModal />
      </BrowserRouter>
    );
  })
);
