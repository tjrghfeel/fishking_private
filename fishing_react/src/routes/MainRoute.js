import React from "react";
import { Switch, Route, Redirect } from "react-router-dom";

import IndexPage from "../pages/Main/IndexPage";
import MyPage from "../pages/Main/MyPage";
import MainTabs from "../components/tabs/MainTabs";

const MainRoute = ({ match, location, history }) => {
  const navigate = (pathname) => {
    history.push(pathname);
  };
  return (
    <>
      <Switch>
        {/** 홈 */}
        <Route exact path={`${match.url}/index`} component={IndexPage} />
        {/** 마이메뉴 */}
        <Route exact path={`${match.url}/my`} component={MyPage} />
        {/** 기본 리디렉션 */}
        <Redirect from={`*`} to={`${match.url}/index`} />
      </Switch>
      <MainTabs pathname={location.pathname} onClick={navigate} />
    </>
  );
};

export default MainRoute;
