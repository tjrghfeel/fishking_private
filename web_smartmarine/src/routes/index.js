/* global Kakao */
import React, { useEffect } from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";
import Components from "../components";

import MainRoute from "./main";
import LoginPage from "../pages/LoginPage";

const {
  MODAL: { AlertModal, ConfirmModal, CouponModal, SelectModal, SNSModal },
  LAYOUT: { LoadingLayout },
} = Components;

export default inject("PageStore")(
  observer(({ PageStore, history }) => {
    PageStore.setHistory(history);
    // # 로그인 체크
    PageStore.loadLogin();
    useEffect(() => {
      (async () => {
        // # 카카오 라이브러리
        await PageStore.injectScript("/assets/js/kakao.min.js", {
          global: true,
        });
        Kakao.init(process.env.REACT_APP_KAKAO_JAVASCRIPT_KEY);
      })();
    }, []);
    console.log(PageStore.loggedIn);
    return (
      <BrowserRouter>
        <Switch>
          {PageStore.loggedIn && (
            <React.Fragment>
              {/** 메인 */}
              <Route path={`/main`} component={MainRoute} />
              {/** 기본 리디렉션 */}
              <Redirect from={`*`} to={`/main`} />
            </React.Fragment>
          )}
          {!PageStore.loggedIn && (
            <React.Fragment>
              {/** 로그인 */}
              <Route path={`/*`} component={LoginPage} />
            </React.Fragment>
          )}
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
