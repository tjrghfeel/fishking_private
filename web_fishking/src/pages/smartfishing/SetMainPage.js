import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "ModalStore",
  "NativeStore"
)(
  observer(
    class extends React.Component {
      menus = [
        // { text: "프로필 관리", loggedIn: true, pathname: "/cust/set/profile" },
        { text: "알림설정", loggedIn: false, pathname: "/set/alarm" },
        { text: "정산 계좌 설정", loggedIn: false, pathname: "/set/paid" },
        { text: "접근 권한 설정", loggedIn: false, pathname: "" },
        // { text: "동영상 설정", loggedIn: false, pathname: "/cust/set/vod" },
        {
          text: "약관 및 정책",
          loggedIn: false,
          pathname: "/cust/policy/main",
        },
        { text: "휴대폰 본인인증", loggedIn: true, pathname: ""},
        // { text: "탈퇴하기", loggedIn: true, pathname: "/cust/member/signout" },
        { text: "로그아웃", loggedIn: true, pathname: "/member/logout" },
      ];
      constructor(props) {
        super(props);
        this.state = { menus: [] };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const { PageStore } = this.props;
        const menus = [];
        for (let menu of this.menus) {
          if ((menu.loggedIn && PageStore.loggedIn) || !menu.loggedIn) {
            menus.push(menu);
          }
        }
        this.setState({ menus });
      }

      requestLogout = async () => {
        const { APIStore, PageStore } = this.props;
        const resolve = APIStore._post("/v2/api/logout");
        if (resolve) {
          PageStore.setAccessToken(null, "smartfishing");
          PageStore.push(`/login`);
        }
      };

      niceRequest = ()=>{window.location.href = '/v2/api/smartfishing/setting/niceRequest?token='+localStorage.getItem("@accessToken")}

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore, NativeStore } = this.props;
        return (
          <React.Fragment>
            {/** Navigation */}
            <NavigationLayout title={"설정"} showBackIcon={true} />

            {/** 입력 */}
            <div className="container nopadding mt-0">
              <div className="pt-0">
                {this.state.menus.map((data, index) => (
                  <React.Fragment>
                    <hr
                      className={
                        "full" + (index === 0 ? " mt-0" : " mt-3") + " mb-3"
                      }
                    />
                    <a
                      onClick={() => {
                        if (data.text === "로그아웃") {
                          this.requestLogout();
                        } else if (data.text === "접근 권한 설정") {
                          NativeStore.postMessage("SetPermissions", {});
                        } else if (data.text === '휴대폰 본인인증'){
                            this.niceRequest();
                        } else if (data.pathname !== "") {
                          console.log(data.pathname)
                          PageStore.push(data.pathname);
                        }
                      }}
                    >
                      <div className="row no-gutters align-items-center">
                        <div className="col-7 pl-2">{data.text}</div>
                        <div className="col-4 text-right"></div>
                        <div className="col-1 text-right pl-1">
                          <img
                            src="/assets/cust/img/svg/cal-arrow-right.svg"
                            alt=""
                          />
                        </div>
                      </div>
                    </a>
                  </React.Fragment>
                ))}
                <hr className="full mt-3 mb-3" />
                <a>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">버전정보</div>
                    <div className="col-8 text-right">
                      <strong>1.7.7</strong> &nbsp;{" "}
                      <span className="status-icon status6">최신버전</span>
                    </div>
                    <div className="col-1 text-right pl-1">
                      <img
                        src="/assets/cust/img/svg/cal-arrow-right.svg"
                        alt=""
                      />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
