import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import DocMainView from "../views/doc/DocMainView";
import DocPolicyTermsView from "../views/doc/DocPolicyTermsView";
import DocPolicyPrivacyView from "../views/doc/DocPolicyPrivacyView";
import DocPolicyCancelView from "../views/doc/DocPolicyCancelView";
import DocPolicyLbsView from "../views/doc/DocPolicyLbsView";
import DocPolicyAgreeView from "../views/doc/DocPolicyAgreeView";
import ViewStore from "../stores/ViewStore";

export default inject("ViewStore")(
  observer(({ match, history }) => {
    ViewStore.setHistory(history);
    return (
      <>
        <Switch>
          {/** 문서 > 메인 (약관 및 정책) */}
          <Route exact path={`${match.url}/main`} component={DocMainView} />
          {/** 문서 > 이용약관 */}
          <Route
            exact
            path={`${match.url}/policy-terms`}
            component={DocPolicyTermsView}
          />
          {/** 문서 > 개인정보처리방침 */}
          <Route
            exact
            path={`${match.url}/policy-privacy`}
            component={DocPolicyPrivacyView}
          />
          {/** 문서 > 취소및환불규정 */}
          <Route
            exact
            path={`${match.url}/policy-cancel`}
            component={DocPolicyCancelView}
          />
          {/** 문서 > 위치기반서비스이용약관 */}
          <Route
            exact
            path={`${match.url}/policy-lbs`}
            component={DocPolicyLbsView}
          />
          {/** 문서 > 개인정보제3자제공동의 */}
          <Route
            exact
            path={`${match.url}/policy-agree`}
            component={DocPolicyAgreeView}
          />
        </Switch>
      </>
    );
  })
);
