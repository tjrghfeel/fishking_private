import React from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router-dom";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout, StoryDetailTab },
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
          this.state = {};
        }
        /********** ********** ********** ********** **********/
        /** function */
        /********** ********** ********** ********** **********/
        componentDidMount() {
          this.loadPageData();
        }

        loadPageData = async () => {
          let {
            match: {
              params: { id: fishingDiaryId, category },
            },
            APIStore,
          } = this.props;
          const resolve = await APIStore._get("/v2/api/fishingDiary/detail", {
            fishingDiaryId,
          });
          if (resolve.fishingDiaryType === "조행기") category = "fishingBlog";
          else category = "fishingDiary";

          this.setState({ ...resolve, category });

          console.log(JSON.stringify(resolve));
        };

        onClickLike = async () => {
          const { APIStore } = this.props;
          const { isLikeTo, fishingDiaryId } = this.state;
          let resolve = false;
          if (isLikeTo) {
            resolve = await APIStore._delete("/v2/api/loveto", {
              linkId: fishingDiaryId,
              takeType: this.state.category,
            });
          } else {
            resolve = await APIStore._post("/v2/api/loveto", {
              linkId: fishingDiaryId,
              takeType: this.state.category,
            });
          }
          if (resolve) {
            this.loadPageData();
          }
        };
        onClickComment = () => {
          const {
            PageStore,
            match: {
              params: { category, id },
            },
          } = this.props;
          PageStore.push(`/story/${category}/comment/${id}`);
        };
        onClickShare = () => {
          const { ModalStore } = this.props;
          ModalStore.openModal("SNS", {
            onSelect: (selected) => {
              console.log(selected);
            },
          });
        };
        onClickReservation = () => {
          const { PageStore } = this.props;
          if (this.state.shipId == null) {
            const fishingSpeciesCodeList = this.state.fishingSpeciesCodeList;
            PageStore.push(
              `/main/company/boat?species=${fishingSpeciesCodeList.join("__")}`
            );
          } else {
            let fishingType = this.state.fishingType;
            if (fishingType === "선상") fishingType = "boat";
            else fishingType = "rock";
            PageStore.push(
              `/company/${fishingType}/detail/${this.state.shipId}`
            );
          }
        };
        onSelectFunction = async (selected) => {
          const { APIStore, ModalStore, PageStore } = this.props;
          if (selected.selected.includes("수정")) {
            PageStore.push(`/story/add?put=${this.state.fishingDiaryId}`);
          } else if (selected.selected.includes("삭제")) {
            ModalStore.openModal("Confirm", {
              body: "삭제하시겠습니까?",
              onOk: async () => {
                const resolve = await APIStore._delete(`/v2/api/fishingDiary`, {
                  fishingDiaryId: this.state.fishingDiaryId,
                });
                if (resolve) {
                  const { from = null } = PageStore.getQueryParams();
                  if (from == "smartfishing") {
                    PageStore.push(`/smartfishing/fish`);
                  } else {
                    PageStore.push(`/main/story/user`);
                  }
                }
              },
            });
          } else if (selected.selected.includes("스크랩")) {
            // scrap
            let resolve = null;
            if (this.state.isScraped) {
              resolve = await APIStore._delete("/v2/api/fishingDiary/scrap", {
                fishingDiaryId: this.state.fishingDiaryId,
              });
            } else {
              resolve = await APIStore._post("/v2/api/fishingDiary/scrap", {
                fishingDiaryId: this.state.fishingDiaryId,
              });
            }
            if (resolve) {
              ModalStore.openModal("Alert", { body: "처리되었습니다." });
              this.loadPageData();
            }
          } else if (selected.selected.includes("신고")) {
            // alert
            const resolve = await APIStore._post("/v2/api/addAccuse", {
              linkId: this.state.fishingDiaryId,
              targetType: "fishingDiary",
            });
            if (resolve) {
              ModalStore.openModal("Alert", { body: "신고되었습니다." });
            }
          }
        };
        /********** ********** ********** ********** **********/
        /** render */
        /********** ********** ********** ********** **********/
        render() {
          const { ModalStore } = this.props;
          return (
            <React.Fragment>
              <NavigationLayout
                title={
                  <React.Fragment>
                    <div className="d-flex align-items-center">
                      <img
                        src={this.state.profileImage}
                        alt=""
                        className="profile-xs"
                      />{" "}
                      {this.state.nickName}
                    </div>
                  </React.Fragment>
                }
                showBackIcon={true}
                customButton={
                  <React.Fragment>
                    <a
                      onClick={() =>
                        ModalStore.openModal("Select", {
                          selectOptions: [
                            this.state.isMine ? "수정하기" : null,
                            this.state.isMine ? "삭제하기" : null,
                            !this.state.isMine && this.state.isScraped
                              ? "스크랩 취소하기"
                              : !this.state.isMine && !this.state.isScraped
                              ? "스크랩하기"
                              : null,
                            !this.state.isMine ? "신고하기" : null,
                            "닫기",
                          ],
                          onSelect: this.onSelectFunction,
                        })
                      }
                      className="fixed-top-right"
                    >
                      <img
                        src="/assets/cust/img/svg/icon-ellipsis-white.svg"
                        alt="Search"
                      />
                    </a>
                  </React.Fragment>
                }
              />

              {/** 타이틀 */}
              <div className="container nopadding">
                <div className="mt-4">
                  <p>
                    <span className="tag-orange">현장실시간</span>{" "}
                    <span className="tag">선상</span>
                  </p>
                  <h5 className="mt-2 mb-1">{this.state.title}</h5>
                  <small className="grey">
                    {this.state.createdDate && (
                      <React.Fragment>
                        {this.state.createdDate.substr(0, 4).concat("년 ")}
                        {this.state.createdDate.substr(5, 2).concat("월 ")}
                        {this.state.createdDate.substr(8, 2).concat("일")}
                      </React.Fragment>
                    )}
                  </small>
                </div>
              </div>

              {/** 낚시정보 */}
              <div className="container nopadding">
                <div className="mt-4">
                  <hr />
                  <dl className="dl-horizontal-round">
                    <dt>어종</dt>
                    <dd>{this.state.fishingSpecies}</dd>
                    <dt>날짜</dt>
                    <dd>
                      {this.state.fishingDate && (
                        <React.Fragment>
                          {this.state.fishingDate.substr(0, 4).concat("년 ")}
                          {this.state.fishingDate.substr(5, 2).concat("월 ")}
                          {this.state.fishingDate.substr(8, 2).concat("일 ")}
                          {this.state.fishingDate
                            .replace(/[-]/g, "")
                            .getWeek()
                            .concat("요일")}
                        </React.Fragment>
                      )}
                    </dd>
                    <dt>물때</dt>
                    <dd>{this.state.tide}</dd>
                    <dt>미끼</dt>
                    <dd>{this.state.fishingLure}</dd>
                    <dt>낚시기법</dt>
                    <dd>
                      {this.state.fishingType} - {this.state.fishingTechnic}
                    </dd>
                  </dl>
                  <hr />
                </div>
                <div className="container nopadding mt-3">
                  <p>{this.state.content}</p>
                  <br />
                  {this.state.videoUrl !== null && (
                    <video width={"100%"} controls src={this.state.videoUrl} />
                  )}
                  {this.state.imageUrlList &&
                    this.state.imageUrlList.map((data, index) => (
                      <React.Fragment key={index}>
                        <img src={data} className="d-block w-100" alt="" />
                        <br />
                      </React.Fragment>
                    ))}
                </div>
              </div>
              <p className="clearfix">
                <br />
                <br />
              </p>

              <StoryDetailTab
                isLikeTo={this.state.isLikeTo}
                likeCount={this.state.likeCount}
                commentCount={this.state.commentCount}
                onClickLike={this.onClickLike}
                onClickComment={this.onClickComment}
                onClickShare={this.onClickShare}
                onClickReservation={this.onClickReservation}
              />
            </React.Fragment>
          );
        }
      }
    )
  )
);
