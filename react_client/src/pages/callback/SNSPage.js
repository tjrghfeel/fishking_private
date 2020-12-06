import React from "react";
import { inject, observer } from "mobx-react";
import { SNSStore } from "../../stores/common/SNSStore";

const SNSPage = inject(
  "RouteStore",
  "SNSStore"
)(
  observer(({ RouteStore, SNSStore, match, location }) => {
    const {
      params: { provider },
    } = match;

    // --> 인가코드
    let authoize_code = null;
    if (provider === "kakao") {
      authoize_code = RouteStore.getQueryParam("code");
    } else if (provider === "facebook") {
      authoize_code = RouteStore.getQueryParam("code");
    } else if (provider === "naver") {
      authoize_code = RouteStore.getQueryParam("code");
    }

    return (
      <div>
        {provider}
        <br />
        authorize_code:
        <br />
        {authoize_code}
      </div>
    );
  })
);

export default SNSPage;
