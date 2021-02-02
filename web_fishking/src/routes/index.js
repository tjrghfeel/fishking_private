/* global Kakao, FB, naver, AppleID */
import React, { useEffect } from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";
import Components from "../components";

import MainRoute from "./main";
import MemberRoute from "./member";
import SetRoute from "./set";
import PolicyRoute from "./policy";
import CsRoute from "./cs";
import StoryRoute from "./story";
import ZzimRoute from "./zzim";
import CouponRoute from "./coupon";
import SearchRoute from "./search";
import ReservationRoute from "./reservation";

const {
  MODAL: { AlertModal, ConfirmModal, CouponModal, SelectModal, SNSModal },
  LAYOUT: { LoadingLayout },
} = Components;

export default inject("PageStore")(
  observer(({ PageStore, history }) => {
    PageStore.setHistory(history);
    // # SNS 로그인 콜백 체크
    const { loggedIn = false, accesstoken = null } = PageStore.getQueryParams();
    if (loggedIn && accesstoken !== null) {
      PageStore.setLogin(accesstoken);
    }
    // # 로그인 체크
    PageStore.loadLogin();
    useEffect(() => {
      (async () => {
        // # 카카오 라이브러리
        await PageStore.injectScript("/assets/js/kakao.min.js", {
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
          "/assets/js/naveridlogin_js_sdk_2.0.0.js",
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
          {/** 메인 */}
          <Route path={`/main`} component={MainRoute} />

          {/** 멤버 */}
          <Route path={`/member`} component={MemberRoute} />

          {/** 설정 */}
          <Route path={`/set`} component={SetRoute} />

          {/** 약관및정책 */}
          <Route path={`/policy`} component={PolicyRoute} />

          {/** 고객센터 */}
          <Route path={`/cs`} component={CsRoute} />

          {/** 스토리 */}
          <Route path={`/story`} component={StoryRoute} />

          {/** 찜 */}
          <Route path={`/zzim`} component={ZzimRoute} />

          {/** 쿠폰 */}
          <Route path={`/coupon`} component={CouponRoute} />

          {/** 검색 */}
          <Route path={`/search`} component={SearchRoute} />

          {/** 예약 */}
          <Route path={`/reservation`} component={ReservationRoute} />

          {/** 기본 리디렉션 */}
          <Redirect from={`*`} to={`/main/home`} />
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
