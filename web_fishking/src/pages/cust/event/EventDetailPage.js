import React from "react";
import { inject, observer } from "mobx-react";
import { withRouter } from "react-router-dom";
import Components from "../../../components";
const {
  LAYOUT: { NavigationLayout },
  VIEW: { EventListItemView },
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
          const { PageStore } = this.props;
          this.loadPageData();
        }
        loadPageData = async () => {
          const {
            APIStore,
            match: {
              params: { eventId },
            },
          } = this.props;
          const resolve = await APIStore._get(`/v2/api/event/detail`, {
            eventId,
          });
          this.setState(resolve);
        };

        onClickLike = async () => {
          const { APIStore } = this.props;
          let resolve = false;
          if (this.state.isLikeTo) {
            resolve = await APIStore._delete(`/v2/api/loveto`, {
              takeType: "event",
              linkId: this.state.eventId,
            });
            if (resolve) this.loadPageData();
          } else {
            resolve = await APIStore._post(`/v2/api/loveto`, {
              takeType: "event",
              linkId: this.state.eventId,
            });
            if (resolve) this.loadPageData();
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
        /********** ********** ********** ********** **********/
        /** render */
        /********** ********** ********** ********** **********/
        render() {
          const { PageStore } = this.props;
          return (
            <React.Fragment>
              <NavigationLayout title={"이벤트"} showBackIcon={true} />

              {/** 타이틀 */}
              <div class="container nopadding">
                <div class="pt-0">
                  <hr class="full mt-0 mb-3" />
                  <div class="row no-gutters align-items-center">
                    <div class="col-12 text-center">
                      <h5 class="mb-1">
                        <strong class="text-primary">
                          {this.state.eventTitle}
                        </strong>
                      </h5>
                      <small class="grey">
                        {this.state.createdDate &&
                          this.state.createdDate.replace(/[-]/g, ".")}
                      </small>
                    </div>
                  </div>
                  <hr class="full mt-3 mb-0" />
                </div>
              </div>

              {/** 이미지 */}
              <div className="mt-0">
                {this.state.imageUrlList &&
                  this.state.imageUrlList.map((data, index) => (
                    <img key={index} src={data} alt="" className="img-fluid" />
                  ))}
              </div>

              {/** 텍스트 */}
              <div className="container nopadding">
                <div className="pt-4 pb-4">
                  <div className="row no-gutters align-items-center">
                    <div className="col-12 pl-2">{this.state.content}</div>
                  </div>
                </div>
              </div>

              {/** Tab Menu */}
              <div className="tab_barwrap fixed-bottom">
                <div className="container nopadding">
                  <nav className="nav nav-pills nav-tab nav-justified">
                    <a
                      onClick={this.onClickLike}
                      className={
                        "nav-link" + (this.state.isLikeTo ? " active" : "")
                      }
                    >
                      <span className="icon icon-good"></span>{" "}
                      {Intl.NumberFormat().format(this.state.likeCount || 0)}
                    </a>
                    <a
                      onClick={() =>
                        PageStore.push(`/event/comment/${this.state.eventId}`)
                      }
                      className="nav-link"
                    >
                      <span className="icon icon-comment"></span>{" "}
                      {Intl.NumberFormat().format(this.state.commentCount || 0)}
                    </a>
                    <a onClick={this.onClickShare} className="nav-link">
                      <span className="icon icon-share"></span>
                    </a>
                    <a
                      className="nav-link btn btn-third btn-sm-nav-tab"
                      onClick={() => PageStore.goBack()}
                    >
                      목록으로
                    </a>
                  </nav>
                </div>
              </div>
            </React.Fragment>
          );
        }
      }
    )
  )
);
