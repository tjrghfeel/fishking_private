import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route, withRouter } from "react-router-dom";

import TidePage from "../../pages/cust/tide/TidePage";
import TideDailyPage from "../../pages/cust/tide/TideDailyPage";

export default inject()(
  observer(
    withRouter(({ match }) => {
      return (
        <Switch>
          {/** 물때 > 오늘의물때정보 */}
          <Route exact path={`${match.url}/today`} component={TidePage} />
          {/** 물때 > 날짜별물때정보 */}
          <Route exact path={`${match.url}/daily`} component={TideDailyPage} />
        </Switch>
      );
    })
  )
);
