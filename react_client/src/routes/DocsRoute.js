import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import DocsPolicyTermsPage from "../pages/docs/DocsPolicyTermsPage";
import DocsPolicyPrivacyPage from "../pages/docs/DocsPolicyPrivacyPage";
import BlankPage from "../pages/BlankPage";

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
        </Switch>
      </>
    );
  })
);
