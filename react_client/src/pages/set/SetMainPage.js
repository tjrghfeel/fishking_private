import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layout/Navigation";
import StaticList01 from "../../components/list/StaticList01";

export default inject("MemberStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        const {
          MemberStore: { loggedIn },
        } = props;
        this.list = [];
        if (loggedIn)
          this.list.push({ text: "프로필 관리", navigateTo: "/set/profile" });
        this.list.push({ text: "알림설정", navigateTo: "/set/alarm" });
        this.list.push({ text: "접근 권한 설정", navigateTo: "" });
        this.list.push({ text: "동영상 설정", navigateTo: "/set/vod" });
        this.list.push({ text: "약관 및 정책", navigateTo: "/set/policy" });
        if (loggedIn)
          this.list.push({ text: "탈퇴하기", navigateTo: "/member/signout" });
        this.list.push({
          text: "버전정보",
          navigateTo: "",
          contentChild: (
            <React.Fragment key={1}>
              <strong>{process.env.REACT_APP_VERSION}</strong> &nbsp;{" "}
              <span className="status-icon status6">최신버전</span>
            </React.Fragment>
          ),
        });
      }
      /********** ********** ********** ********** **********/
      /** functions **/
      /********** ********** ********** ********** **********/

      /********** ********** ********** ********** **********/
      /** render **/
      /********** ********** ********** ********** **********/
      render() {
        return (
          <>
            {/** Navigation */}
            <Navigation title={"설정"} visibleBackIcon={true} />

            {/** 입력 */}
            <StaticList01 list={this.list} />
          </>
        );
      }
    }
  )
);
