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
          this.state = {};
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
              params: { eventId },
            },
            APIStore,
            PageStore,
          } = this.props;

          const resolve = await APIStore._get(`/v2/api/comment`, {
            dependentType: "event",
            linkId: eventId,
          });
          this.setState(resolve);

          if (toScrollEnd) {
            setTimeout(() => {
              window.scrollTo(0, document.scrollingElement.scrollHeight);
            }, 100);
          }
        };
        onClickReply = async (item) => {
          console.log(JSON.stringify(item));
          const { ModalStore } = this.props;
          ModalStore.openModal("Input", {
            onOk: async (text) => {
              if (text !== "") {
                const {
                  APIStore,
                  match: {
                    params: { eventId },
                  },
                } = this.props;
                const resolve = await APIStore._post(`/v2/api/comment`, {
                  dependentType: "event",
                  linkId: eventId,
                  parentId: item.commentId,
                  content: text,
                  fileId: null,
                });
                if (resolve) {
                  this.loadPageData();
                }
              }
            },
          });
        };
        onClickMore = async (item) => {
          const { ModalStore } = this.props;
          ModalStore.openModal("Select", {
            selectOptions: ["삭제하기", "닫기"],
            onSelect: ({ index }) => this.onCallbackMore(item, index),
          });
        };
        onCallbackMore = async (item, index) => {
          const { APIStore } = this.props;
          if (index === 0) {
            // 삭제하기
            const resolve = await APIStore._delete("/v2/api/comment", {
              commentId: item.commentId,
            });
            if (resolve) this.loadPageData();
          }
        };
        onClickLike = async (item) => {
          const { APIStore } = this.props;
          if (item.isLikeTo) {
            // >>>>> 좋아요 취소
            const resolve = await APIStore._delete("/v2/api/loveto", {
              takeType: "commonComment",
              linkId: item.commentId,
            });
            if (resolve) this.loadPageData();
          } else {
            // >>>>> 좋아요
            const resolve = await APIStore._post("/v2/api/loveto", {
              takeType: "commonComment",
              linkId: item.commentId,
            });
            if (resolve) this.loadPageData();
          }
        };
        onSubmit = async () => {
          const text = this.text.current?.value;
          if (text === "") return;

          const {
            APIStore,
            match: {
              params: { eventId },
            },
          } = this.props;
          const resolve = await APIStore._post(`/v2/api/comment`, {
            dependentType: "event",
            linkId: eventId,
            parentId: 0,
            content: text,
            fileId: null,
          });
          if (resolve) {
            this.text.current.value = "";
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
              <div className="tab_barwrap fixed-bottom">
                <div className="container nopadding">
                  <form className="form-line" style={{ marginTop: "1px" }}>
                    <div className="form-group row">
                      <div className="col-10">
                        {/*<input*/}
                        {/*  ref={this.file}*/}
                        {/*  type="file"*/}
                        {/*  accept="image/*"*/}
                        {/*  style={{ display: "none" }}*/}
                        {/*/>*/}
                        {/*<a*/}
                        {/*  className="float-photo"*/}
                        {/*  onClick={() => this.file.current?.click()}*/}
                        {/*>*/}
                        {/*  <img*/}
                        {/*    src="/assets/cust/img/svg/icon-photo.svg"*/}
                        {/*    alt="사진"*/}
                        {/*    className="icon-sm"*/}
                        {/*  />*/}
                        {/*</a>*/}
                        <input
                          ref={this.text}
                          type="text"
                          className="form-control no-line ml-4"
                          placeholder="댓글을 입력해주세요."
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
