/* global Kakao */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, PoliceMainTab },
} = Components;

export default inject(
  "PageStore",
  "ModalStore",
  "NativeStore"
)(
  observer(
    class extends React.Component {
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const { PageStore } = this.props;
        PageStore.loadSaved();
      }

      logout = () => {
        const { PageStore } = this.props;
        PageStore.setAccessToken(null, "police");
        PageStore.push(`/login`);
      };
      requestTalk = () => {
        Kakao.Channel.chat({
          channelPublicId: "_NzxabK",
        });
      };
      requestCall = () => {
        const { ModalStore, NativeStore } = this.props;
        ModalStore.openModal("Confirm", {
          title: "전화걸기",
          body: (
            <React.Fragment>
              <p>
                고객센터로 전화연결 하시겠습니까?
                <br />
                365일 10시 ~ 18시
                <br />
                (점심시간 12시 ~ 13시 30분)
              </p>
            </React.Fragment>
          ),
          textOk: "통화",
          onOk: () =>
            NativeStore.linking("tel:".concat(process.env.REACT_APP_CS_PHONE)),
        });
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout title={"더보기"} showBackIcon={true} />

            <div className="container padding">
              <div className="media d-flex align-items-center mt-2">
                <img
                  src="/assets/police/img/svg/profile.svg"
                  className="profile-thumb-md align-self-center mr-2"
                  alt="profile"
                />
                <div className="media-body">
                  <h6>
                    <strong>ID : {PageStore.saved.memberId}</strong>
                    <a
                      className="btn btn-round-grey btn-xs float-right"
                      onClick={this.logout}
                    >
                      로그아웃
                    </a>
                  </h6>
                  <p>계정과 관련된 문의는 고객센터를 이용해 주세요.</p>
                </div>
              </div>
              <p className="space mt-4"></p>
            </div>

            <div className="container nopadding">
              <h6 className="text-center mt-3">고객센터</h6>
              <p className="text-center">
                <small className="grey">
                  365일 10시~18시 운영 (점심시간 12시 ~ 13시 30분)
                </small>
              </p>
              <div className="row no-gutters no-gutters-cs d-flex align-items-center mt-4">
                <div className="col-6">
                  <a
                    className="btn btn-yellow btn-round btn-lg btn-block cs-padding"
                    onClick={this.requestTalk}
                  >
                    <img
                      src="/assets/police/img/svg/icon-talk.svg"
                      alt="Set"
                      className="vam mr-1"
                    />
                    카카오 상담톡
                  </a>
                </div>
                <div className="col-6" style={{ paddingRight: "0px" }}>
                  <a
                    className="btn btn-grey btn-round btn-lg btn-block cs-padding"
                    onClick={this.requestCall}
                  >
                    <img
                      src="/assets/police/img/svg/icon-call.svg"
                      alt="Set"
                      className="vam mr-1"
                    />
                    {process.env.REACT_APP_CS_PHONE}
                  </a>
                </div>
              </div>
            </div>

            <PoliceMainTab activeIndex={3} />
          </React.Fragment>
        );
      }
    }
  )
);
