import React from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router-dom";
import Components from "../../components";
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
          const {
            match: {
              params: { id: fishingDiaryId },
            },
            APIStore,
          } = this.props;
          const resolve = await APIStore._get("/v2/api/fishingDiary/detail", {
            fishingDiaryId,
          });
          this.setState(resolve);
        };

        onClickLike = async () => {
          const { APIStore } = this.props;
          const { isLikeTo, fishingDiaryId } = this.state;
          let resolve = false;
          if (isLikeTo) {
            resolve = await APIStore._delete("/v2/api/loveto", {
              linkId: fishingDiaryId,
              takeType: "fishingDiary",
            });
          } else {
            resolve = await APIStore._post("/v2/api/loveto", {
              linkId: fishingDiaryId,
              takeType: "fishingDiary",
            });
          }
          if (resolve) {
            this.setState({ isLikeTo: !isLikeTo });
          }
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
          console.log("reservation");
        };
        /********** ********** ********** ********** **********/
        /** render */
        /********** ********** ********** ********** **********/
        render() {
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
                    <dd>{this.state.fishingTechnic}</dd>
                  </dl>
                  <hr />
                </div>
                <div className="container nopadding mt-3">
                  <p>{this.state.content}</p>
                  <br />
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
