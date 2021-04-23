/* global YT */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  VIEW: { StoryTvTubeListItemView },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.player = null;
        this.state = {
          tube_isPending: false,
          tube_list: [],
          tube_nextPageToken: null,
          item: null,
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const {
          match: {
            params: { id },
          },
          PageStore,
        } = this.props;
        const { data } = PageStore.getQueryParams();
        const parsed = JSON.parse(data.replace(/[ ]/g, "+").decrypt());
        this.setState({ ...parsed });
        this.init(id);
      }
      init = async (id, item) => {
        const { PageStore } = this.props;
        // > 영상 설정
        if (item) {
          this.setState({ ...item });
        }
        if (this.player === null) {
          setTimeout(() => {
            this.player = new YT.Player("player", {
              height: "360",
              width: "640",
              videoId: id,
              events: {
                onReady: () => console.log("onReady"),
                onStateChange: () => console.log("onStateChange"),
              },
            });
          }, 800);
        } else {
          this.player.loadVideoById(id, 5, "large");
        }
        // 추가 영상 조회
        PageStore.setScrollEvent(() => {
          this.loadPageDataForTube(true);
        });
        this.loadPageDataForTube(false);
      };
      loadPageDataForTube = async (nextPage = false) => {
        const { APIStore } = this.props;

        if (
          (nextPage && this.state.tube_nextPageToken === null) ||
          this.state.tube_isPending
        )
          return;

        this.setState({ tube_isPending: true });
        const {
          items: content = [],
          nextPageToken = null,
          playUrl = null,
        } = await APIStore._get(`/v2/api/tv/list`, {
          nextPageToken: nextPage ? this.state.tube_nextPageToken : null,
        });

        if (!nextPage) {
          // 첫 페이지
          this.setState({ tube_list: content });
          setTimeout(() => {
            window.scrollTo(0, 0);
          }, 100);
        } else {
          // 추가 페이지
          this.setState({
            tube_list: this.state.tube_list.concat(content),
          });
        }
        this.setState({
          tube_isPending: false,
          tube_nextPageToken: nextPageToken,
        });
      };
      onClickTube = async (item) => {
        this.init(item.id, item);
        // const { PageStore } = this.props;
        // PageStore.push(
        //   `/story/tv/${item.id}?data=${JSON.stringify(item).encrypt()}`
        // );
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <div
              id="carousel-visual-detail"
              className="carousel slide"
              data-ride="carousel"
            >
              <div className="float-top-left">
                <a onClick={() => PageStore.push(`/main/story/tv`)}>
                  <img
                    src="/assets/cust/img/svg/navbar-back.svg"
                    alt="뒤로가기"
                  />
                </a>
              </div>
              <div className="carousel-inner">
                <div className="carousel-item active">
                  <video
                    id="player"
                    muted
                    playsInline
                    // controls
                    style={{ width: "100%" }}
                  ></video>
                </div>
              </div>
            </div>

            {/** 상품타이틀 */}
            <div className="container nopadding">
              <div className="card mt-3">
                <h4>{this.state.snippet?.title}</h4>
                <p>
                  <span className="grey">
                    조회수{" "}
                    {Intl.NumberFormat().format(
                      this.state.statistics?.viewCount || 0
                    )}
                    회 |{" "}
                    {(this.state.snippet?.publishedAt || "")
                      .substr(0, 10)
                      .replace(/[.]/g, "-")}
                  </span>
                </p>
              </div>
              <p className="space mt-4 mb-4"></p>
            </div>

            {this.state.tube_list.length > 0 &&
              this.state.tube_list.map((data, index) => (
                <StoryTvTubeListItemView
                  key={index}
                  data={data}
                  onClick={this.onClickTube}
                />
              ))}
          </React.Fragment>
        );
      }
    }
  )
);
