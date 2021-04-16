import React from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router-dom";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout },
  VIEW: { CommentListItemView },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "ModalStore"
)(
  observer(
    withRouter(
      class extends React.Component {
        constructor(props) {
          super(props);
          this.file = React.createRef(null);
          this.text = React.createRef(null);
          this.state = {
            file: null,
            parent: null,
            isEdit: false,
            edit: null,
          };
        }
        /********** ********** ********** ********** **********/
        /** function */
        /********** ********** ********** ********** **********/
        async componentDidMount() {
          this.loadPageData();
        }
        loadPageData = async (toScrollEnd = false) => {
          const {
            match: {
              params: { id },
            },
            APIStore,
            PageStore,
          } = this.props;

          const resolve = await APIStore._get(`/v2/api/fishingDiaryComment`, {
            fishingDiaryId: id,
          });
          this.setState({ ...resolve });

          if (toScrollEnd) {
            setTimeout(() => {
              window.scrollTo(0, document.scrollingElement.scrollHeight);
            }, 100);
          }
        };
        onClickReply = async (item) => {
          this.setState({ parent: item });
        };
        onClickMore = async (item) => {
          const { ModalStore } = this.props;
          ModalStore.openModal("Select", {
            selectOptions: ["수정하기", "삭제하기", "닫기"],
            onSelect: ({ index }) => this.onCallbackMore(item, index),
          });
        };
        onCallbackMore = async (item, index) => {
          const { APIStore, ModalStore } = this.props;
          if (index === 0) {
            // 수정하기
            this.text.current.value = item.content;
            this.setState({
              isEdit: true,
              edit: item,
            });
            if (item.fileUrl !== null) {
              this.setState({
                file: {
                  downloadUrl: item.fileUrl,
                  fileId: item.fileId,
                },
              });
            }
          } else if (index === 1) {
            // 삭제하기
            ModalStore.openModal("Confirm", {
              body: "삭제하시겠습니까?",
              onOk: async () => {
                const resolve = await APIStore._delete(
                  "/v2/api/fishingDiaryComment",
                  {
                    commentId: item.commentId,
                  }
                );
                if (resolve) this.loadPageData();
              },
            });
          }
        };
        onClickLike = async (item) => {
          const { APIStore } = this.props;
          if (item.isLikeTo) {
            // >>>>> 좋아요 취소
            const resolve = await APIStore._delete("/v2/api/loveto", {
              takeType: "comment",
              linkId: item.commentId,
            });
            if (resolve) this.loadPageData();
          } else {
            // >>>>> 좋아요
            const resolve = await APIStore._post("/v2/api/loveto", {
              takeType: "comment",
              linkId: item.commentId,
            });
            if (resolve) this.loadPageData();
          }
        };
        uploadImage = async () => {
          if (this.file.current?.files.length === 0) return;

          const file = this.file.current?.files[0];

          const form = new FormData();
          form.append("file", file);
          form.append("filePublish", "comment");

          const { APIStore, ModalStore } = this.props;
          const upload = await APIStore._post_upload(
            `/v2/api/filePreUpload`,
            form
          );
          if (upload) {
            this.setState({ file: upload });
          } else {
            ModalStore.openModal("Alert", {
              body: "업로드 중 에러가 발생하였습니다.",
            });
          }
          this.file.current.value = null;
        };
        onSubmit = async () => {
          const text = this.text.current?.value;
          if (text === "") return;

          const {
            APIStore,
            match: {
              params: { id, category },
            },
          } = this.props;

          let resolve = false;
          if (this.state.isEdit) {
            // 수정
            resolve = await APIStore._put(`/v2/api/fishingDiaryComment`, {
              commentId: this.state.edit.commentId,
              content: text,
              fileId: this.state.file?.fileId || null,
            });
          } else {
            // 등록
            resolve = await APIStore._post(`/v2/api/fishingDiaryComment`, {
              dependentType:
                category === "story" ? "fishingDiary" : "fishingBlog",
              fishingDiaryId: id,
              parentId: this.state.parent?.commentId || 0,
              content: text,
              fileId: this.state.file?.fileId || null,
            });
          }
          if (resolve) {
            this.text.current.value = "";
            this.setState({
              file: null,
              isEdit: false,
              edit: null,
              parent: null,
            });
            this.loadPageData(true);
          }
        };
        /********** ********** ********** ********** **********/
        /** render */
        /********** ********** ********** ********** **********/
        render() {
          return (
            <React.Fragment>
              <NavigationLayout title={"댓글"} showBackIcon={true} />

              {/** 타이틀 */}
              {this.state.title && (
                <div className="filterWrap">
                  <h5 className="mt-2 mb-1 text-center">{this.state.title}</h5>
                </div>
              )}

              {/** 요약 */}
              <div className="container nopadding">
                <div className="row no-gutters d-flex align-items-center">
                  <div className="col-6">
                    <h5 className="">
                      댓글{" "}
                      <span className="red">
                        {Intl.NumberFormat().format(
                          this.state.commentCount || 0
                        )}
                      </span>
                    </h5>
                  </div>
                </div>
                <hr className="full mt-2 mb-3" />
              </div>

              {/** 리스트 */}
              {this.state.commentList?.map((data, index) => (
                <React.Fragment>
                  <CommentListItemView
                    key={index}
                    data={data}
                    onClickReply={this.onClickReply}
                    onClickMore={this.onClickMore}
                    onClickLike={this.onClickLike}
                  />
                  {data.childList &&
                    data.childList.map((item, index2) => (
                      <CommentListItemView
                        key={index2}
                        data={item}
                        onClickReply={this.onClickReply}
                        onClickMore={this.onClickMore}
                        onClickLike={this.onClickLike}
                      />
                    ))}
                </React.Fragment>
              ))}

              {/** Tab Menu */}
              {this.state.file !== null && (
                <div className="tab_barwrap_photo">
                  <span className="photo-wrap">
                    <a
                      onClick={() => this.setState({ file: null })}
                      className="del"
                    >
                      <img src="/assets/cust/img/svg/icon_close_white.svg" />
                    </a>
                    <img
                      src={this.state.file.downloadUrl}
                      className="photo-img"
                      alt=""
                    />
                  </span>
                </div>
              )}
              <div className="tab_barwrap fixed-bottom">
                {(this.state.parent !== null || this.state.isEdit) && (
                  <h6>
                    <div className="container nopadding">
                      {this.state.isEdit && "댓글 수정중 ..."}
                      {!this.state.isEdit && (
                        <React.Fragment>
                          {this.state.parent.nickName}님께 답글 남기는 중...{" "}
                        </React.Fragment>
                      )}
                      <a
                        onClick={() =>
                          this.setState({ parent: null, isEdit: false })
                        }
                        className="del"
                      >
                        <img src="/assets/cust/img/svg/icon_close_grey.svg" />
                      </a>
                    </div>
                  </h6>
                )}
                <div className="container nopadding">
                  <form
                    className="form-line"
                    style={{ marginTop: "1px" }}
                    onSubmit={(e) => e.preventDefault()}
                  >
                    <div className="form-group row">
                      <div className="col-10">
                        <input
                          ref={this.file}
                          type="file"
                          accept="image/*"
                          style={{ display: "none" }}
                          onChange={this.uploadImage}
                        />
                        <a
                          className="float-photo"
                          onClick={() => this.file.current?.click()}
                        >
                          <img
                            src="/assets/cust/img/svg/icon-photo.svg"
                            alt="사진"
                            className="icon-sm"
                          />
                        </a>
                        <input
                          ref={this.text}
                          type="text"
                          className="form-control no-line ml-4"
                          placeholder="댓글을 입력해주세요."
                          onKeyDown={(e) => {
                            if (e.keyCode == 13) {
                              this.onSubmit();
                            }
                          }}
                        />
                      </div>
                      <div className="col-2 text-right">
                        <a
                          className="btn btn-primary btn-sm-nav-tab"
                          onClick={this.onSubmit}
                        >
                          등록
                        </a>
                      </div>
                    </div>
                  </form>
                </div>
              </div>
            </React.Fragment>
          );
        }
      }
    )
  )
);
