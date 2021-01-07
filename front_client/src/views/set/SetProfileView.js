import React from "react";
import { inject, observer } from "mobx-react";
import SelectModal from "../../components/modals/SelectModal";
import Http from "../../Http";

export default inject("AlertStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.profile = React.createRef(null);
        this.background = React.createRef(null);
        this.state = {};
      }
      /********** ********** ********** ********** **********/
      /** functions */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        document.querySelector("body").classList.add("pofile");
        this.loadPageData();
      }
      componentWillUnmount() {
        document.querySelector("body").classList.remove("pofile");
      }

      loadPageData = async () => {
        const resolve = await Http._get("/v2/api/profileManage");
        resolve.emailMask = resolve.email
          .substr(0, 2)
          .concat("****")
          .concat(
            resolve.email.substr(
              resolve.email.indexOf("@"),
              resolve.email.length
            )
          );
        this.setState(resolve);
      };
      onChangeImage = async (changeType) => {
        if (this.profile.current?.files.length > 0) {
          const file = this.profile.current?.files[0];

          const form = new FormData();
          form.append("file", file);
          form.append("filePublish", "profile");

          const upload = await Http._post_upload("/v2/api/filePreUpload", form);

          if (upload && upload.fileId) {
            let resolve;
            if (changeType === "profile") {
              resolve = await Http._put("/v2/api/profileManage/profileImage", {
                profileImgFileId: upload.fileId,
              });
            } else if (changeType === "background") {
              resolve = await Http._put(
                "/v2/api/profileManage/profileBackgroundImage",
                {
                  profileImgFileId: upload.fileId,
                }
              );
            }
            if (resolve) {
              this.props.AlertStore.openAlert("알림", "변경되었습니다.");
              this.loadPageData();
            }
          }
          const profile = this.profile.current;
          profile.value = null;
        }
      };
      onSelectChangeProfile = async (item) => {
        if (item.value === "album") {
          this.profile.current?.click();
        } else if (item.value === "default") {
          const resolve = await Http._put(
            "/v2/api/profileManage/noProfileImage"
          );
          if (resolve) {
            this.props.AlertStore.openAlert("알림", "변경되었습니다.");
            this.loadPageData();
          }
        }
      };
      onSelectChangeBackground = async (item) => {
        if (item.value === "album") {
          this.background.current?.click();
        } else if (item.value === "default") {
          // TODO : 설정 > 프로필관리 : 배경이미지 없애는 요청
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { history } = this.props;
        return (
          <>
            {/** Top */}
            <div className="mt-0">
              <div className="profilewrap">
                <input
                  ref={this.profile}
                  type="file"
                  accept="image/*"
                  style={{ display: "none" }}
                  onChange={() => this.onChangeImage("profile")}
                />
                <input
                  ref={this.background}
                  type="file"
                  accept="image/*"
                  style={{ display: "none" }}
                  onChange={() => this.onChangeImage("background")}
                />
                <div className="float-top-left">
                  <a onClick={() => history.goBack()}>
                    <img src="/assets/img/svg/navbar-back.svg" alt="뒤로가기" />
                  </a>
                </div>
                <a className="img-upload btn btn-circle btn-circle-sm btn-white float_btm_right">
                  <img
                    src="/assets/img/svg/icon-photo.svg"
                    alt=""
                    onClick={() => this.profile.current?.click()}
                    className="icon-photo"
                  />
                </a>
                <div className="imgWrap">
                  <img
                    src="/assets/img/bg-profile04.jpg"
                    className="d-block w-100"
                    alt=""
                  />
                </div>
                <figure>
                  <span>
                    <img
                      className="media-object profile"
                      src={this.state.profileImage}
                      alt=""
                    />
                    <a
                      data-toggle="modal"
                      data-target="#selModal"
                      // onClick={() => this.profile.current?.click()}
                      className="img-upload btn btn-circle btn-circle-sm btn-white float_btn"
                    >
                      <img
                        src="/assets/img/svg/icon-photo.svg"
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
                  <div className="col-8 text-right">{this.state.email}</div>
                  <div className="col-1 text-right pl-1"></div>
                </div>
                <hr className="full mt-3 mb-3" />
                <a onClick={() => history.push(`/set/profile/nickName`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">
                      <small className="grey">닉네임</small>
                    </div>
                    <div className="col-8 text-right">
                      {this.state.nickName}
                    </div>
                    <div className="col-1 text-right pl-1">
                      <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
                <a onClick={() => history.push(`/set/profile/statusMessage`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">
                      <small className="grey">상태메시지</small>
                    </div>
                    <div className="col-8 text-right">
                      {this.state.statusMessage ||
                        "상태메시지를 입력해 주세요."}
                    </div>
                    <div className="col-1 text-right pl-1">
                      <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
                <a onClick={() => history.push(`/set/profile/mobile`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">
                      <small className="grey">휴대폰 번호</small>
                    </div>
                    <div className="col-8 text-right">
                      {this.state.areaCode + "" + this.state.localNumber}
                    </div>
                    <div className="col-1 text-right pl-1">
                      <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
                <a onClick={() => history.push(`/set/profile/email`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">
                      <small className="grey">이메일 변경</small>
                    </div>
                    <div className="col-8 text-right">
                      {this.state.emailMask}
                    </div>
                    <div className="col-1 text-right pl-1">
                      <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
                <a onClick={() => history.push(`/set/profile/password`)}>
                  <div className="row no-gutters align-items-center">
                    <div className="col-3 pl-2">
                      <small className="grey">비밀번호 변경</small>
                    </div>
                    <div className="col-8 text-right"></div>
                    <div className="col-1 text-right pl-1">
                      <img src="/assets/img/svg/cal-arrow-right.svg" alt="" />
                    </div>
                  </div>
                </a>
                <hr className="full mt-3 mb-3" />
              </div>
            </div>

            {/** 모달팝업 */}
            <SelectModal
              id={"selModal"}
              title={"프로필 사진 변경"}
              options={[
                // { text: "사진촬영", value: "camera" },
                { text: "사진 선택", value: "album" },
                { text: "기본 이미지로 변경", value: "default" },
              ]}
              onClick={this.onSelectChangeProfile}
            />
            <SelectModal
              id={"selBackModal"}
              title={"프로필 배경 사진 변경"}
              options={[
                { text: "사진촬영", value: "camera" },
                { text: "앨범에서 사진 선택", value: "album" },
                { text: "기본 이미지로 변경", value: "default" },
              ]}
              onClick={this.onSelectChangeBackground}
            />
          </>
        );
      }
    }
  )
);
