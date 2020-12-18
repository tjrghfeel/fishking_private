import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import DocsPolicyTermsPage from "../pages/docs/DocsPolicyTermsPage";
import DocsPolicyPrivacyPage from "../pages/docs/DocsPolicyPrivacyPage";
import DocsPolicyCancelPage from "../pages/docs/DocsPolicyCancelPage";
import DocsPolicyLbsPage from "../pages/docs/DocsPolicyLbsPage";
import DocsPolicyAgreePage from "../pages/docs/DocsPolicyAgreePage";

export default inject()(
  observer(({ match }) => {
    return (
      <>
        <Switch>
          {/** 문서 > 이용약관 */}
          <Route
            exact
            path={`${match.url}/policy-terms`}
            component={DocsPolicyTermsPage}
          />
          {/** 문서 > 개인정보처리방침 */}
          <Route
            exact
            path={`${match.url}/policy-privacy`}
            component={DocsPolicyPrivacyPage}
          />
          {/** 문서 > 취소및환불규정 */}
          <Route
            exact
            path={`${match.url}/policy-cancel`}
            component={DocsPolicyCancelPage}
          />
          {/** 문서 > 위치기반서비스이용약관 */}
          <Route
            exact
            path={`${match.url}/policy-lbs`}
            component={DocsPolicyLbsPage}
          />
          {/** 문서 > 개인정보제3자제공동의 */}
          <Route
            exact
            path={`${match.url}/policy-agree`}
            component={DocsPolicyAgreePage}
          />
        </Switch>
      </>
    );
  })
);
