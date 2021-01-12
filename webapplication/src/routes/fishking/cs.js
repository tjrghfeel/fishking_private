import React from "react";
import { inject, observer } from "mobx-react";
import { BrowserRouter, Switch, Route } from "react-router-dom";
import Pages from "../../pages";
const {
  BlankPage,
  Fishking: {
    Cs: { FaqPage, ApplyPage, QnaPage, QnaListPage, QnaDetailPage },
  },
} = Pages;

export default inject()(
  observer(({ match }) => {
    return (
      <BrowserRouter>
        <Switch>
          {/** 어복황제 > 고객센터 > 자주하는질문 */}
          <Route exact path={`${match.url}/faq`} component={FaqPage} />
          {/** 어복황제 > 고객센터 > 업체등록요청 */}
          <Route exact path={`${match.url}/apply`} component={ApplyPage} />
          {/** 어복황제 > 고객센터 > 1:1문의하기 */}
          <Route exact path={`${match.url}/qna`} component={QnaPage} />
          {/** 어복황제 > 고객센터 > 1:1문의하기 > 목록 */}
          <Route exact path={`${match.url}/qna/list`} component={QnaListPage} />
          {/** 어복황제 > 고객센터 > 1:1문의하기 > 상세 */}
          <Route
            exact
            path={`${match.url}/qna/:id`}
            component={QnaDetailPage}
          />
        </Switch>
      </BrowserRouter>
    );
  })
);
