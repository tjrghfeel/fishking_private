import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route, withRouter } from "react-router-dom";

import PolicyMainPage from "../../pages/cust/policy/PolicyMainPage";
import PolicyTermsPage from "../../pages/cust/policy/PolicyTermsPage";
import PolicyPrivacyPage from "../../pages/cust/policy/PolicyPrivacyPage";
import PolicyCancelPage from "../../pages/cust/policy/PolicyCancelPage";
import PolicyLbsPage from "../../pages/cust/policy/PolicyLbsPage";
import PolicyAgreePage from "../../pages/cust/policy/PolicyAgreePage";

export default inject()(
  observer(
    withRouter(({ match }) => {
      return (
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
      );
    })
  )
);
