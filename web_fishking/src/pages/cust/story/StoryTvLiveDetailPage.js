/* global Hls, Player */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";
const {
  VIEW: { StoryTvLiveOtherListItemView },
} = Components;

export default inject(
  "PageStore",
  "APIStore",
  "NativeStore",
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.video = React.createRef(null);
        this.state = {
          connectionType: '',
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
          NativeStore,
          match: {
            params: { shipId, cameraId },
          },
        } = this.props;

        NativeStore.postMessage('Connections', {});
        document.addEventListener("message", event => {
          this.setState({ connectionType: event.data });
        });
        window.addEventListener("message", event => {
          this.setState({ connectionType: event.data });
        });

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
          if (url.startsWith("rtsp://")) {
            const player = new Player({ streamUrl: url });
            if (this.state.connectionType === 'wifi') {
              player.start();
            }
          } else if (Hls.isSupported()) {
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
            if (this.state.connectionType === 'wifi') {
              video.play();
            }
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
                    alt="????????????"
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
                    // autoPlay
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

            {/** ??????????????? */}
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

            {/** ????????? ????????? */}
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

            {/** ???????????? */}
            <div className="fixed-bottom">
              <div className="row no-gutters">
                <div className="col-6">
                  <a
                    onClick={() => PageStore.push(`/main/story/tv`)}
                    className="btn btn-secondary btn-lg btn-block"
                  >
                    ????????????
                  </a>
                </div>
                <div className="col-6">
                  <a
                    onClick={() =>
                      PageStore.push(`/company/boat/detail/${shipId}`)
                    }
                    className="btn btn-primary btn-lg btn-block"
                  >
                    ??????????????????
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
