import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";

import PolicyMainPage from "../pages/policy/PolicyMainPage";
import PolicyTermsPage from "../pages/policy/PolicyTermsPage";
import PolicyPrivacyPage from "../pages/policy/PolicyPrivacyPage";
import PolicyCancelPage from "../pages/policy/PolicyCancelPage";
import PolicyLbsPage from "../pages/policy/PolicyLbsPage";
import PolicyAgreePage from "../pages/policy/PolicyAgreePage";

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 약관및정책 > 메인 */}
          <Route exact path={`${match.url}/main`} component={PolicyMainPage} />
          {/** 어복황제 > 약관및정책 > 이용약관 */}
          <Route
            exact
            path={`${match.url}/terms`}
            component={PolicyTermsPage}
          />
          {/** 약관및정책 > 개인정보처리방침 */}
          <Route
            exact
            path={`${match.url}/privacy`}
            component={PolicyPrivacyPage}
          />
          {/** 약관및정책 > 취소및환불규정 */}
          <Route
            exact
            path={`${match.url}/cancel`}
            component={PolicyCancelPage}
          />
          {/** 약관및정책 > 위치기반서비스이용약관 */}
          <Route exact path={`${match.url}/lbs`} component={PolicyLbsPage} />
          {/** 약관및정책 > 개인정보제3자제공동의 */}
          <Route
            exact
            path={`${match.url}/agree`}
            component={PolicyAgreePage}
          />
        </Switch>
      </BrowserRouter>
    );
  })
);
