import React from "react";
import { inject, observer } from "mobx-react";

export default inject(
  "PageStore",
  "APIStore",
  "ModalStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.profile = React.createRef(null);
        this.background = React.createRef(null);
      }

      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const { PageStore, ModalStore } = this.props;
        const {
          msg = null,
          token = null,
        } = PageStore.getQueryParams();
        if(msg === 'niceResultParsingError'){
          ModalStore.openModal("Alert", { body: "오류" });
        }
        else if(msg === 'success'){
          localStorage.setItem("@accessToken_cust", token);
          ModalStore.openModal("Alert", { body: "휴대폰 번호가 수정되었습니다." });
        }
        else if(msg === 'niceCertificationFail'){
          localStorage.setItem("@accessToken_cust", token);
          ModalStore.openModal("Alert", { body: "본인인증에 실패하였습니다." });
        }

        document.querySelector("body").classList.add("pofile");
        this.loadPageData();
      }
      componentWillUnmount() {
        document.querySelector("body").classList.remove("pofile");
      }

      loadPageData = async () => {
        const { APIStore, PageStore } = this.props;
        const resolve = await APIStore._get("/v2/api/profileManage");
        PageStore.setState(resolve);
      };

      openChangeProfile = () => {
        const { ModalStore } = this.props;
        ModalStore.openModal("Select", {
          title: "프로필 사진 변경",
          selectOptions: ["사진 선택", "기본 이미지로 변경"],
          onSelect: ({ selected, index }) =>
            this.changeProfile(selected, index),
        });
      };

      changeProfile = async (selected, index) => {
        const { APIStore } = this.props;
        if (index === 0) {
          this.profile.current?.click();
        } else {
          await APIStore._put("/v2/api/profileManage/noProfileImage");
          this.loadPageData();
        }
      };

      uploadProfile = async () => {
        if (this.profile.current?.files.length > 0) {
          const file = this.profile.current?.files[0];

          const form = new FormData();
          form.append("file", file);
          form.append("filePublish", "profile");

          const { APIStore } = this.props;
          const upload = await APIStore._post_upload(
            "/v2/api/filePreUpload",
            form
          );
          if (upload && upload.fileId) {
            const resolve = await APIStore._put(
              "/v2/api/profileManage/profileImage",
              {
                profileImgFileId: upload.fileId,
              }
            );
            if (resolve) {
              this.loadPageData();
            }
          }
        }
        this.profile.current.value = null;
      };

      openChangeBackground = () => {
        const { ModalStore } = this.props;
        ModalStore.openModal("Select", {
          title: "배경 사진 변경",
          selectOptions: ["사진 선택", "기본 이미지로 변경"],
          onSelect: ({ selected, index }) =>
            this.changeBackground(selected, index),
        });
      };

      changeBackground = async (selected, index) => {
        const { APIStore } = this.props;
        if (index === 0) {
          this.background.current?.click();
        } else {
          await APIStore._put("/v2/api/profileManage/noProfileBackgroundImage");
          this.loadPageData();
        }
      };

      uploadBackground = async () => {
        if (this.background.current?.files.length > 0) {
          const file = this.background.current?.files[0];

          const form = new FormData();
          form.append("file", file);
          form.append("filePublish", "profile");

          const { APIStore } = this.props;
          const upload = await APIStore._post_upload(
            "/v2/api/filePreUpload",
            form
          );
          if (upload && upload.fileId) {
            const resolve = await APIStore._put(
              "/v2/api/profileManage/profileBackgroundImage",
              {
                profileImgFileId: upload.fileId,
              }
            );
            if (resolve) {
              this.loadPageData();
            }
          }
        }
        this.profile.current.value = null;
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const {
          PageStore: { state },
          PageStore,
        } = this.props;
        return (
          <React.Fragment>
            <input
              ref={this.profile}
              type="file"
              accept="image/*"
              style={{ display: "none" }}
              onChange={this.uploadProfile}
            />
            <input
              ref={this.background}
              type="file"
              accept="image/*"
              style={{ display: "none" }}
              onChange={this.uploadBackground}
            />
            <div className="mt-0">
              <div className="profilewrap">
                <div className="float-top-left">
                  <a onClick={() => PageStore.goBack()}>
                    <img
                      src="/assets/cust/img/svg/navbar-back.svg"
                      alt="뒤로가기"
                    />
                  </a>
                </div>
                <a
                  onClick={this.openChangeBackground}
                  className="img-upload btn btn-circle btn-circle-sm btn-white float_btm_right"
                >
                  <img
                    src="/assets/cust/img/svg/icon-photo.svg"
                    alt=""
                    className="icon-photo"
                  />
                </a>
                <div className="imgWrap">
                  <img
                    src={
                      state.profileBackgroundImage ||
                      "/assets/cust/img/bg-profile04.jpg"
                    }
                    className="d-block w-100"
                    alt=""
                  />
                </div>
                <figure>
                  <span>
                    <img
                      className="media-object profile"
                      src={state.profileImage}
                      alt=""
                    />
                    <a
                      onClick={this.openChangeProfile}
                      className="img-upload btn btn-circle btn-circle-sm btn-white float_btn"
                    >
                      <img
                        src="/assets/cust/img/svg/icon-photo.svg"
                        alt=""
                        className="icon-photo"
                      />
                    </a>
                  </span>
                </figure>
              </div>
            </div>

            {/** 입력 */}
            <div className="container nopadding">
              <div className="pt-0">
                <hr className="full mt-3 mb-3" />
                <div className="row no-gutters align-items-center">
                  <div className="col-3 pl-2">
                    <small className="grey">계정</small>
                  </div>
                  <div className="col-8 text-right">{state.uid}</div>
                  <div className="col-1 text-right pl-1"></div>
                </div>
                <hr className="full mt-3 mb-3" />
                <a onClick={() => PageStore.push(`/set/profile/nickname`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">
                      <small className="grey">닉네임</small>
                    </div>
                    <div className="col-8 text-right">{state.nickName}</div>
                    <div className="col-1 text-right pl-1">
                      <img
                        src="/assets/cust/img/svg/cal-arrow-right.svg"
                        alt=""
                      />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
                <a onClick={() => PageStore.push(`/set/profile/status`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">
                      <small className="grey">상태메시지</small>
                    </div>
                    <div className="col-8 text-right">
                      {state.statusMessage || "상태메시지를 입력해 주세요."}
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
                <a href={"https://www.fishkingapp.com/v2/api/profileManage/phoneNum/niceRequest?token="+localStorage.getItem("@accessToken_cust")}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">
                      <small className="grey">휴대폰 번호</small>
                    </div>
                    <div className="col-8 text-right">
                      {(state.areaCode || "").concat(state.localNumber || "")}
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
                <a onClick={() => PageStore.push(`/set/profile/email`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">
                      <small className="grey">이메일 변경</small>
                    </div>
                    <div className="col-8 text-right">
                      {(state.email || "").maskEmail()}
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
                <a onClick={() => PageStore.push(`/set/profile/password`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">
                      <small className="grey">비밀번호 변경</small>
                    </div>
                    <div className="col-8 text-right"></div>
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
