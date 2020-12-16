import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";
import StaticList01 from "../../components/list/StaticList01";

export default inject()(
  observer(() => {
    /** 설정 메뉴 데이터 */
    const list = [
      { text: "프로필 관리", navigateTo: "/set/profile" },
      { text: "알림설정", navigateTo: "/set/alarm" },
      { text: "접근 권한 설정", navigateTo: "" },
      { text: "동영상 설정", navigateTo: "/set/vod" },
      { text: "약관 및 정책", navigateTo: "/set/policy" },
      { text: "탈퇴하기", navigateTo: "/member/signout" },
      {
        text: "버전정보",
        navigateTo: "",
        contentChild: (
          <React.Fragment key={1}>
            <strong>{process.env.REACT_APP_VERSION}</strong> &nbsp;{" "}
            <span className="status-icon status6">최신버전</span>
          </React.Fragment>
        ),
      },
    ];
    return (
      <>
        {/** Navigation */}
        <Navigation title={"설정"} visibleBackIcon={true} />

        {/** 입력 */}
        <StaticList01 list={list} />
      </>
    );
  })
);
