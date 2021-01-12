import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";
import Pages from "../../pages";
const {
  Common: {
    Policy: {
      MainPage,
      TermsPage,
      PrivacyPage,
      CancelPage,
      LbsPage,
      AgreePage,
    },
  },
} = Pages;

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 공통 > 약관및정책 > 메인 */}
          <Route exact path={`${match.url}/main`} component={MainPage} />
          {/** 공통 > 약관및정책 > 이용약관 */}
          <Route exact path={`${match.url}/terms`} component={TermsPage} />
          {/** 공통 > 약관및정책 > 개인정보처리방침 */}
          <Route exact path={`${match.url}/privacy`} component={PrivacyPage} />
          {/** 공통 > 약관및정책 > 취소및환불규정 */}
          <Route exact path={`${match.url}/cancel`} component={CancelPage} />
          {/** 공통 > 약관및정책 > 위치기반서비스이용약관 */}
          <Route exact path={`${match.url}/lbs`} component={LbsPage} />
          {/** 공통 > 약관및정책 > 개인정보제3자제공동의 */}
          <Route exact path={`${match.url}/agree`} component={AgreePage} />
        </Switch>
      </BrowserRouter>
    );
  })
);
