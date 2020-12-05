/* global Kakao */
import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route, Redirect } from "react-router-dom";

import Modals from "../components/modals";

import MainRoute from "./MainRoute";
import CommonRoute from "./CommonRoute";

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
      componentDidMount() {
        const { PageStore } = this.props;
        PageStore.injectScript("/assets/js/kakao.min.js", () => {
          Kakao.init(process.env.REACT_APP_KAKAO_JAVASCRIPT_KEY);
        });
      }

      render() {
        return (
          <>
            <Switch>
              {/** 메인 라우트 :: BottomTab */}
              <Route path={`/main`} component={MainRoute} />
              {/** 공통 라우트 */}
              <Route path={`/common`} component={CommonRoute} />
              {/** 최초 스크린 리디렉트 */}
              <Redirect from={`*`} to={`/main/index`} />
            </Switch>
            <Modals />
          </>
        );
      }
    }
  )
);
