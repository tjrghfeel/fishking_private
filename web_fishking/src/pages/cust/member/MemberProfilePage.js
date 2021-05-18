import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";

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
        this.state = {};
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        document.querySelector("body").classList.add("pofile");
        this.loadPageData();
      }
      componentWillUnmount() {
        document.querySelector("body").classList.remove("pofile");
      }

      loadPageData = async () => {
        const {
          match: {
            params: { id: userId },
          },
          APIStore,
        } = this.props;
        const resolve = await APIStore._get("/v2/api/profile", { userId });
        this.setState(resolve);
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
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <input
              ref={this.profile}
              type="file"
              accept="image/*"
              style={{ display: "none" }}
              onChange={this.uploadProfile}
            />
            <div className="profilewrap">
              <div className="float-top-left">
                <a onClick={() => PageStore.goBack()}>
                  <img
                    src="/assets/cust/img/svg/navbar-back.svg"
                    alt="뒤로가기"
                  />
                </a>
              </div>
              <div className="imgWrap">
                <img
                  src={this.state.backgroundImage}
                  className="d-block w-100"
                  alt=""
                  style={{height:'40vh'}}
                />
              </div>
              <figure>
                <span>
                  <img
                    className="media-object profile"
                    src={this.state.profileImage}
                    alt=""
                  />
                  {this.state.isMe && (
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
                  )}
                </span>
              </figure>
            </div>

            <div className="profiletit">
              <h5>{this.state.nickName}</h5>
              <p className="clearfix mt-4"></p>

              <nav className="nav nav-pills nav-sel nav-my nav-justified mt-5 pt-0">
                {!this.state.isMe && (
                  <a className="nav-link border-right">
                    <h4 className="text-primary">
                      {Intl.NumberFormat().format(this.state.takeCount || 0)}
                    </h4>
                    <span>업체 찜 수</span>
                  </a>
                )}
                <a className="nav-link border-right">
                  <h4 className="text-primary">
                    {Intl.NumberFormat().format(this.state.postCount || 0)}
                  </h4>
                  <span>작성 글 수</span>
                </a>
                <a className="nav-link">
                  <h4 className="text-primary">
                    {Intl.NumberFormat().format(this.state.likeCount || 0)}
                  </h4>
                  <span>좋아요 수</span>
                </a>
              </nav>

              <nav className="nav nav-pills nav-sel nav-circle nav-justified mt-5">
                {this.state.isMe && (
                  <React.Fragment>
                    {/*<a*/}
                    {/*  className="nav-link"*/}
                    {/*  onClick={() => PageStore.push(`/set/profile`)}*/}
                    {/*>*/}
                    {/*  <figure>*/}
                    {/*    <img*/}
                    {/*      src="/assets/cust/img/svg/icon-profile.svg"*/}
                    {/*      alt=""*/}
                    {/*    />*/}
                    {/*  </figure>*/}
                    {/*  <span>프로필 관리</span>*/}
                    {/*</a>*/}
                    {/*<a*/}
                    {/*  className="nav-link"*/}
                    {/*  onClick={() => PageStore.push(`/story/add`)}*/}
                    {/*>*/}
                    {/*  <figure>*/}
                    {/*    <img*/}
                    {/*      src="/assets/cust/img/svg/mymenu-write.svg"*/}
                    {/*      alt=""*/}
                    {/*    />*/}
                    {/*  </figure>*/}
                    {/*  <span>글쓰기</span>*/}
                    {/*</a>*/}
                    {/*<a*/}
                    {/*  className="nav-link"*/}
                    {/*  onClick={() => PageStore.push(`/story/my/post`)}*/}
                    {/*>*/}
                    {/*  <figure>*/}
                    {/*    <img src="/assets/cust/img/svg/icon-post.svg" alt="" />*/}
                    {/*  </figure>*/}
                    {/*  <span>내 글 관리</span>*/}
                    {/*</a>*/}
                    {/*<a*/}
                    {/*  className="nav-link"*/}
                    {/*  onClick={() => PageStore.push(`/zzim/boat`)}*/}
                    {/*>*/}
                    {/*  <figure>*/}
                    {/*    <img src="/assets/cust/img/svg/icon-heart.svg" alt="" />*/}
                    {/*  </figure>*/}
                    {/*  <span>찜 목록</span>*/}
                    {/*</a>*/}
                  </React.Fragment>
                )}
                {!this.state.isMe && (
                  <React.Fragment>
                    {this.state.isCompany && (
                      <React.Fragment>
                        {/*<a className="nav-link">*/}
                        {/*    <figure>*/}
                        {/*        <img*/}
                        {/*            src="/assets/cust/img/svg/icon-home.svg"*/}
                        {/*            alt=""*/}
                        {/*        />*/}
                        {/*    </figure>*/}
                        {/*    <span>업체 바로가기</span>*/}
                        {/*</a>*/}
                      </React.Fragment>
                    )}
                    {/*<a*/}
                    {/*  className="nav-link"*/}
                    {/*  onClick={() => PageStore.push(`/search/all`)}*/}
                    {/*>*/}
                    {/*  <figure>*/}
                    {/*    <img*/}
                    {/*      src="/assets/cust/img/svg/icon-search-b.svg"*/}
                    {/*      alt=""*/}
                    {/*    />*/}
                    {/*  </figure>*/}
                    {/*  <span>검색하기</span>*/}
                    {/*</a>*/}
                    {/*<a className="nav-link">*/}
                    {/*  <figure>*/}
                    {/*    <img src="/assets/cust/img/svg/icon-post.svg" alt="" />*/}
                    {/*  </figure>*/}
                    {/*  <span>작성 글 보기</span>*/}
                    {/*</a>*/}
                    {this.state.isCompany && (
                      <React.Fragment>
                        {/*<a className="nav-link">*/}
                        {/*    <figure>*/}
                        {/*        <img*/}
                        {/*            src="/assets/cust/img/svg/icon-reserv.svg"*/}
                        {/*            alt=""*/}
                        {/*        />*/}
                        {/*    </figure>*/}
                        {/*    <span>예약하기</span>*/}
                        {/*</a>*/}
                      </React.Fragment>
                    )}
                  </React.Fragment>
                )}
              </nav>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
