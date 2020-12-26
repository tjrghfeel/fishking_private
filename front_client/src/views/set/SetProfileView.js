import React from "react";
import { inject, observer } from "mobx-react";
import Http from "../../Http";

export default inject()(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.profile = React.createRef(null);
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
      onChangeProfile = async () => {
        if (this.profile.current?.files.length > 0) {
          const file = this.profile.current?.files[0];

          const form = new FormData();
          form.append("profileImage", file);

          const resolve = await Http._put_upload(
            "/v2/api/profileManage/profileImage",
            form
          );
          if (resolve) {
            this.loadPageData();
          }
        }
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        // TODO : 설정 > 프로필관리 : 퍼블상 배경이미지도 변경이 가능한 것 같은데, API 필요함
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
                  onChange={this.onChangeProfile}
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
                      onClick={() => this.profile.current?.click()}
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
          </>
        );
      }
    }
  )
);
