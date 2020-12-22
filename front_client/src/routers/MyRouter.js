import React from "react";
import { inject, observer } from "mobx-react";
import { Switch, Route } from "react-router-dom";

import BlankPage from "../views/BlankPage";
import MyReservationView from "../views/my/MyReservationView";
import MyCouponView from "../views/my/MyCouponView";
import MyAlarmView from "../views/my/MyAlarmView";
import MyStoryPostView from "../views/my/MyStoryPostView";
import MyStoryCommentView from "../views/my/MyStoryCommentView";
import MyStoryScrapView from "../views/my/MyStoryScrapView";
import MyStoryReviewView from "../views/my/MyStoryReviewView";

export default inject()(
  observer(({ match }) => {
    return (
      <>
        <Switch>
          {/** 마이 > 예약내역 */}
          <Route
            exact
            path={`${match.url}/reservation`}
            component={MyReservationView}
          />
          {/** 마이 > 내 쿠폰 */}
          <Route exact path={`${match.url}/coupon`} component={MyCouponView} />
          {/** 마이 > 알림 */}
          <Route exact path={`${match.url}/alarm`} component={MyAlarmView} />
          {/** 마이 > 내글관리 > 포스트 */}
          <Route
            exact
            path={`${match.url}/story/post`}
            component={MyStoryPostView}
          />
          {/** 마이 > 내글관리 > 댓글 */}
          <Route
            exact
            path={`${match.url}/story/comment`}
            component={MyStoryCommentView}
          />
          {/** 마이 > 내글관리 > 스크랩 */}
          <Route
            exact
            path={`${match.url}/story/scrap`}
            component={MyStoryScrapView}
          />
          {/** 마이 > 내글관리 > 리뷰 */}
          <Route
            exact
            path={`${match.url}/story/review`}
            component={MyStoryReviewView}
          />
          {/** 마이 > 찜한업체 */}
          <Route exact path={`${match.url}/zzim`} component={BlankPage} />
        </Switch>
      </>
    );
  })
);
