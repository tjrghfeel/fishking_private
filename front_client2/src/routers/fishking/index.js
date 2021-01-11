/* global Kakao, FB, naver, AppleID */
import React, { useEffect } from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import BlankPage from "../../views/BlankPage";
import MainRoute from "./main";

export default inject("DOMStore")(
  observer(({ match, DOMStore }) => {
    /********** ********** ********** ********** **********/
    /** function */
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
          <Route exact={`${match.url}/main`} component={MainRoute} />
        </Switch>
      </BrowserRouter>
    );
  })
);
