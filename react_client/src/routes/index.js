/* global Kakao, FB, naver, AppleID */
import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route, Redirect } from "react-router-dom";

import Modals from "../components/modals";

import MainRoute from "./MainRoute";
import CommonRoute from "./CommonRoute";
import CallbackRoute from "./CallbackRoute";

export default inject(
  "RouteStore",
  "PageStore"
)(
  observer(
    class Routes extends React.Component {
      constructor(props) {
        super(props);
        props.RouteStore.setRouteObject(props.history, props.location);
      }
      async componentDidMount() {
        const { PageStore } = this.props;
        // --> KAKAO Init
        await PageStore.injectScript(
          "/assets/js/kakao.min.js",
          () => {
            Kakao.init(process.env.REACT_APP_KAKAO_JAVASCRIPT_KEY);
          },
          null,
          { global: true }
        );
        // --> FACEBOOK Init
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
        // --> NAVER Init
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
        // --> APPLE Init
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
      }

      render() {
        return (
          <>
            <Switch>
              {/** 메인 라우트 :: BottomTab */}
              <Route path={`/main`} component={MainRoute} />
              {/** 공통 라우트 */}
              <Route path={`/common`} component={CommonRoute} />
              {/** 콜백 라우트 */}
              <Route path={`/callback`} component={CallbackRoute} />
              {/** 최초 리디렉트 */}
              <Redirect from={`*`} to={`/main/index`} />
            </Switch>
            <Modals />
          </>
        );
      }
    }
  )
);
