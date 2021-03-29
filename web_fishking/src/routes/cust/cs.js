import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route, withRouter } from "react-router-dom";

import CsFaqPage from "../../pages/cust/cs/CsFaqPage";
import CsApplyPage from "../../pages/cust/cs/CsApplyPage";
import CsApplyEndPage from "../../pages/cust/cs/CsApplyEndPage";
import CsQnaAddPage from "../../pages/cust/cs/CsQnaAddPage";
import CsQnaListPage from "../../pages/cust/cs/CsQnaListPage";
import CsQnaDetailPage from "../../pages/cust/cs/CsQnaDetailPage";
import CsNoticeListPage from "../../pages/cust/cs/CsNoticeListPage";
import CsNoticeDetailPage from "../../pages/cust/cs/CsNoticeDetailPage";
import CsAlarmPage from "../../pages/cust/cs/CsAlarmPage";

export default inject()(
  observer(
    withRouter(({ match }) => {
      return (
        <Switch>
          {/** 고객센터 > FAQ */}
          <Route exact path={`${match.url}/faq`} component={CsFaqPage} />
          {/** 고객센터 > 업체등록요청 */}
          <Route exact path={`${match.url}/apply`} component={CsApplyPage} />
          {/** 고객센터 > 업체등록요청 > 완료 */}
          <Route
            exact
            path={`${match.url}/apply/end`}
            component={CsApplyEndPage}
          />
          {/** 고객센터 > 1:1문의하기 > 등록 */}
          <Route exact path={`${match.url}/qna/add`} component={CsQnaAddPage} />
          {/** 고객센터 > 1:1문의하기 > 내역 */}
          <Route
            exact
            path={`${match.url}/qna/list`}
            component={CsQnaListPage}
          />
          {/** 고객센터 > 1:1문의하기 > 상세 */}
          <Route
            exact
            path={`${match.url}/qna/detail/:id`}
            component={CsQnaDetailPage}
          />
          {/** 고객센터 > 공지사항 > 내역 */}
          <Route
            exact
            path={`${match.url}/notice/list`}
            component={CsNoticeListPage}
          />
          {/** 고객센터 > 공지사항 > 상세 */}
          <Route
            exact
            path={`${match.url}/notice/detail/:id`}
            component={CsNoticeDetailPage}
          />
          {/** 고객센터 > 알림 */}
          <Route exact path={`${match.url}/alarm`} component={CsAlarmPage} />
        </Switch>
      );
    })
  )
);
