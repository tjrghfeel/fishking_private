/* global Hls */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  VIEW: { StoryTvLiveOtherListItemView },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.video = React.createRef(null);
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
          APIStore,
          match: {
            params: { shipId, cameraId },
          },
        } = this.props;

        const resolve = await APIStore._get(`/v2/api/tv/live`, {
          shipId,
          cameraId,
        });
        this.setState(resolve);

        const { cameraData = null, cameraList = [] } = resolve;
        if (cameraData?.liveVideo) {
          const video = this.video.current;
          video.setAttribute("poster", cameraData.thumbnailUrl);
          const url = cameraData.liveVideo;
          if (Hls.isSupported()) {
            const hls = new Hls({
              capLevelToPlayerSize: true,
              capLevelOnFPSDrop: true,
            });
            hls.attachMedia(video);
            hls.on(Hls.Events.MEDIA_ATTACHED, () => {
              hls.loadSource(url);
              hls.on(Hls.Events.MANIFEST_PARSED, (e, data) => {
                this.mediaError = false;
              });
              hls.on(Hls.Events.ERROR, (e, data) => {
                const { type, details, fatal } = data;

                if (type === Hls.ErrorTypes.NETWORK_ERROR) {
                  hls.startLoad();
                } else if (type === Hls.ErrorTypes.MEDIA_ERROR) {
                  hls.detachMedia();
                  setTimeout(() => {
                    video.src = url;
                  }, 800);
                  this.mediaError = true;
                } else {
                  console.error("MEDIA DESTROY");
                  hls.destroy();
                }
              });
            });
          } else {
            video.src = url;
            video.addEventListener("loadedmetadata", () => {});
          }
        }
      };
      onClickItem = async (item) => {
        const {
          PageStore,
          match: {
            params: { shipId },
          },
        } = this.props;
        PageStore.push(`/story/tv/${shipId}/${item.id}`);
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const {
          PageStore,
          match: {
            params: { shipId },
          },
        } = this.props;
        return (
          <React.Fragment>
            <div
              id="carousel-visual-detail"
              className="carousel slide"
              data-ride="carousel"
            >
              <div className="float-top-left">
                <a onClick={() => PageStore.goBack()}>
                  <img
                    src="/assets/cust/img/svg/navbar-back.svg"
                    alt="뒤로가기"
                  />
                </a>
              </div>
              <div className="carousel-inner">
                <div className="carousel-item active">
                  <video
                    ref={this.video}
                    id="video"
                    muted
                    playsInline
                    controls
                    autoPlay
                    style={{ width: "100%" }}
                  ></video>
                  <span
                    className="play-live"
                    style={{ marginBottom: "8px", marginRight: "8px" }}
                  >
                    LIVE
                  </span>
                </div>
              </div>
            </div>

            {/** 상품타이틀 */}
            <div className="container nopadding">
              <div className="card mt-3">
                <h4>{this.state.cameraData?.name}</h4>
                <small className="grey">
                  <strong className="text-primary">
                    {this.state.cameraData?.species}
                  </strong>
                  &nbsp; {this.state.cameraData?.address}
                </small>
              </div>
              <p className="space mt-4 mb-4"></p>
            </div>

            {/** 카메라 리스트 */}
            {(this.state.cameraList || []).length > 0 && (
              <div className="container nopadding">
                {this.state.cameraList.map((data, index) => (
                  <StoryTvLiveOtherListItemView
                    key={index}
                    data={data}
                    onClick={this.onClickItem}
                  />
                ))}
              </div>
            )}

            {/** 하단버튼 */}
            <div className="fixed-bottom">
              <div className="row no-gutters">
                <div className="col-6">
                  <a
                    onClick={() => PageStore.push(`/main/story/tv`)}
                    className="btn btn-secondary btn-lg btn-block"
                  >
                    목록보기
                  </a>
                </div>
                <div className="col-6">
                  <a
                    onClick={() =>
                      PageStore.push(`/company/boat/detail/${shipId}`)
                    }
                    className="btn btn-primary btn-lg btn-block"
                  >
                    선상상세정보
                  </a>
                </div>
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
