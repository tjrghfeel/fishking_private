import React, { useEffect } from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";

/**
 * Root Router
 */
import FishkingRoute from "./fishking";
import SmartFishingRoute from "./smartfishing";
import SmartSailRoute from "./smartsail";
import CommonRoute from "./common";

import Components from "../components";
const {
  Common: { AlertModal, ConfirmModal },
} = Components;

export default inject("ViewStore")(
  observer(({ ViewStore, history }) => {
    /********** ********** ********** ********** **********/
    /** function */
    /********** ********** ********** ********** **********/
    useEffect(() => {
      ViewStore.setHistory(history);
    }, [ViewStore]);
    /********** ********** ********** ********** **********/
    /** render */
    /********** ********** ********** ********** **********/
    return (
      <BrowserRouter>
        <Switch>
          {/** 어복황제 */}
          <Route path={`/fishking`} component={FishkingRoute} />
          {/** 스마트승선 */}
          <Route path={`/smartfishing`} component={SmartFishingRoute} />
          {/** 스마트출조 */}
          <Route path={`/smartsail`} component={SmartSailRoute} />
          {/** 공통 */}
          <Route path={`/common`} component={CommonRoute} />
          {/** 기본 리디렉션 */}
          <Redirect from={`*`} to={`/fishking/main/home`} />
        </Switch>
        {/** 공통 모달 */}
        <AlertModal />
        <ConfirmModal />
      </BrowserRouter>
    );
  })
);
