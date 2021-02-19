import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout },
  VIEW: { StoryDetailCommentListItemView },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "ModalStore",
  "DataStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.file = React.createRef(null);
        this.state = {
          detail: {},
          list: [],
          sort: "createdDate",
          file: null,
          uploaded: null,
          content: "",
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        this.loadPageData();
      }

      loadPageData = async () => {
        const {
          APIStore,
          match: {
            params: { id: fishingDiaryId, category },
          },
        } = this.props;
        if (category === "diary") this.setState({ category: "fishingDiary" });
        else if (category === "user")
          this.setState({ category: "fishingBlog" });

        let resolve = await APIStore._get("/v2/api/fishingDiary/detail", {
          fishingDiaryId,
        });
        this.setState({ detail: resolve });

        resolve = await APIStore._get("/v2/api/fishingDiaryComment", {
          fishingDiaryId,
        });
        this.setState({ list: resolve });
      };
      onClickItem = async (type, data, index) => {
        const { PageStore, ModalStore, APIStore, DataStore } = this.props;
        if (type === "profile") {
          PageStore.push(`/member/profile/${data.authorId}`);
        } else if (type === "more") {
          ModalStore.openModal("Select", {
            selectOptions: ["수정하기", "삭제하기", "닫기"],
            onSelect: (selected) => this.onClickMoreItem(selected, data),
          });
        } else if (type === "like") {
          let resolve = false;
          if (data.isLikeTo) {
            resolve = await APIStore._delete("/v2/api/loveto", {
              linkId: data.commentId,
              takeType: "comment",
            });
          } else {
            resolve = await APIStore._post("/v2/api/loveto", {
              linkId: data.commentId,
              takeType: "comment",
            });
          }
          if (resolve) {
            // const list = DataStore.updateItemOfArrayByKey(
            //   this.state.list,
            //   "commentId",
            //   data.commentId,
            //   { isLikeTo: !data.isLikeTo }
            // );
            // this.setState({ list });
            this.loadPageData();
          }
        }
      };
      onClickMoreItem = async (selected, data) => {
        if (selected.index === 0) {
          // 수정하기
        } else if (selected.index === 1) {
          // 삭제하기
          const { APIStore, DataStore, ModalStore } = this.props;
          ModalStore.openModal("Confirm", {
            title: "댓글삭제",
            body: "댓글을 삭제하시겠습니까?",
            onOk: async () => {
              const resolve = await APIStore._delete(
                "/v2/api/fishingDiaryComment",
                { commentId: data.commentId }
              );
              if (resolve) {
                // const list = DataStore.removeItemOfArrayByKey(
                //   this.state.list,
                //   "commentId",
                //   data.commentId
                // );
                // this.setState({ list });
                this.loadPageData();
              }
            },
          });
        }
      };
      uploadImage = async () => {
        if (this.file.current?.files.length > 0) {
          const file = this.file.current?.files[0];

          const form = new FormData();
          form.append("file", file);
          form.append("filePublish", "comment");

          const { APIStore } = this.props;
          const uploaded = await APIStore._post_upload(
            "/v2/api/filePreUpload",
            form
          );

          if (uploaded) {
            this.setState({ uploaded });
          }
          console.log(JSON.stringify(uploaded));
          this.file.current.value = null;
        }
      };
      postComment = async (parentId = 0) => {
        if (this.state.content === "" && this.state.uploaded === null) return;

        const {
          APIStore,
          match: {
            params: { id: fishingDiaryId },
          },
        } = this.props;
        try {
          const resolve = await APIStore._post("/v2/api/fishingDiaryComment", {
            fishingDiaryId: fishingDiaryId,
            dependentType: this.state.category,
            parentId,
            fileId:
              this.state.uploaded === null ? null : this.state.uploaded.fileId,
          });
          if (resolve) {
            this.loadPageData();
          }
        } catch (err) {
          console.log(err);
        }
      };

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <NavigationLayout title={"댓글"} showBackIcon={true} />

            <div className="filterWrap">
              <h5 className="mt-2 mb-1 text-center">
                {this.state.detail.title}
              </h5>
            </div>

            <div className="container nopadding">
              <div className="row no-gutters d-flex align-items-center">
                <div className="col-6">
                  <h5 className="">
                    댓글{" "}
                    <span className="red">
                      {Intl.NumberFormat().format(this.state.list.length)}
                    </span>
                  </h5>
                </div>
              </div>
              <hr className="full mt-2 mb-3" />
            </div>

            {this.state.list.map((data, index) => (
              <React.Fragment>
                <StoryDetailCommentListItemView
                  key={index}
                  index={index}
                  data={data}
                  onClick={this.onClickItem}
                />
                {data.childList &&
                  data.childList.length > 0 &&
                  data.childList.map((child, index2) => (
                    <StoryDetailCommentListItemView
                      key={index2}
                      index={index2}
                      data={child}
                      onClick={this.onClickItem}
                    />
                  ))}
              </React.Fragment>
            ))}

            <div className="tab_barwrap fixed-bottom">
              <div className="container nopadding">
                <form className="form-line" style={{ marginTop: "1px" }}>
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
                        onClick={() => this.file.current.click()}
                        className="float-photo"
                      >
                        <img
                          src="/assets/cust/img/svg/icon-photo.svg"
                          alt="사진"
                          className="icon-sm"
                        />
                      </a>
                      <input
                        type="text"
                        className="form-control no-line ml-4"
                        placeholder="댓글을 입력해주세요."
                        value={this.state.content}
                        onChange={(e) =>
                          this.setState({ content: e.target.value })
                        }
                      />
                    </div>
                    <div className="col-2 text-right">
                      <a
                        className="btn btn-primary btn-sm-nav-tab"
                        onClick={() => this.postComment(0)}
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
);
