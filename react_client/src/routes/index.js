/* global Kakao, FB, naver, AppleID */
import React, { useEffect } from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route, Redirect, BrowserRouter } from "react-router-dom";

import MainRoute from "./MainRoute";
import MemberRoute from "./MemberRoute";
import DocsRoute from "./DocsRoute";
import SearchRoute from "./SearchRoute";
import SetRoute from "./SetRoute";
import CouponRoute from "./CouponRoute";
import CommonRoute from "./CommonRoute";
import StoryRoute from "./StoryRoute";

export default inject(
  "PageStore",
  "MemberStore"
)(
  observer(({ PageStore, MemberStore }) => {
    /** 로그인여부 로드 */
    MemberStore.loadLoggedIn();
    /** 초기화 */
    useEffect(() => {
      (async () => {
        // --> 1. Kakao 초기화
        await PageStore.injectScript(
          "/assets/js/kakao.min.js",
          () => {
            Kakao.init(process.env.REACT_APP_KAKAO_JAVASCRIPT_KEY);
          },
          null,
          { global: true }
        );
        // --> 2. Facebook 초기화
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
        // --> 3. Naver 초기화
        await PageStore.injectScript(
          "/assets/js/naveridlogin_js_sdk_2.0.0.js",
          () => {
            const button = document.createElement("div");
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
          },
          null,
          { global: true }
        );
        // --> 4. Apple 초기화
        await PageStore.injectScript(
          "https://appleid.cdn-apple.com/appleauth/static/jsapi/appleid/1/en_US/appleid.auth.js",
          () => {
            const button = document.createElement("div");
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
          },
          null,
          { global: true }
        );
      })();
    }, [PageStore, MemberStore]);
    /** 렌더링 */
    return (
      <BrowserRouter>
        <Switch>
          {/** 메인 */}
          <Route path={`/main`} component={MainRoute} />
          {/** 멤버 */}
          <Route path={`/member`} component={MemberRoute} />
          {/** 문서 */}
          <Route path={`/docs`} component={DocsRoute} />
          {/** 검색 */}
          <Route path={`/search`} component={SearchRoute} />
          {/** 설정 */}
          <Route path={`/set`} component={SetRoute} />
          {/** 쿠폰 */}
          <Route path={`/coupon`} component={CouponRoute} />
          {/** 스토리 */}
          <Route path={`/story`} component={StoryRoute} />
          {/** 공통 및 기타 */}
          <Route path={`/common`} component={CommonRoute} />
          {/** 기본 라우팅 */}
          <Redirect from={`*`} to={"/main/home"} />
        </Switch>
      </BrowserRouter>
    );
  })
);
