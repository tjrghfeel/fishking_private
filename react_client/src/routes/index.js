import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route, Redirect } from "react-router-dom";

import Modals from "../components/modals";
import MainRoute from "./MainRoute";

export default inject("RouteStore")(
  observer(
    class Routes extends React.Component {
      constructor(props) {
        super(props);
        props.RouteStore.setHistory(props.history);
      }
      render() {
        return (
          <>
            <Switch>
              {/** 메인 라우트 :: BottomTab */}
              <Route path={`/main`} component={MainRoute} />
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
