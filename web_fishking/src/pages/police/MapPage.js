/* global daum, kakao, $ */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, PoliceMainTab },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.container = React.createRef(null);
        this.map = null;
        this.state = { list: [], selected: null };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        this.loadPageData();
      }
      loadPageData = async () => {
        const { APIStore } = this.props;
        const resolve = (await APIStore._get(`/v2/api/police/ships/map`)) || [];
        this.setState({ list: resolve });

        let lat = 36.252932;
        let lng = 127.724734;
        if (resolve && resolve.length > 0) {
          lat = resolve[0]["latitude"];
          lng = resolve[0]["longitude"];
        }

        this.map = new daum.maps.Map(this.container.current, {
          center: new daum.maps.LatLng(lat, lng),
          level: 7,
        });

        for (let item of resolve) {
          const marker = new kakao.maps.Marker({
            position: new kakao.maps.LatLng(
              item["latitude"],
              item["longitude"]
            ),
          });
          kakao.maps.event.addListener(marker, "click", () =>
            this.openInfoModal(item)
          );
          marker.setMap(this.map);
        }
      };
      openInfoModal = (item) => {
        console.log(`click -> ${JSON.stringify(item)}`);
        this.setState({ selected: item });
        $("#infoModal").modal("show");
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout title={"출항정보"} showBackIcon={true} />

            <div className="container nopadding">
              <div className="mapwrap">
                <div
                  ref={this.container}
                  style={{ width: "100%", height: "100vh" }}
                ></div>
              </div>
            </div>

            <PoliceMainTab activeIndex={1} />

            <div
              className="modal show modal-full-btm"
              id="infoModal"
              tabIndex="-1"
              aria-labelledby="infoModalLabel"
              aria-hidden="true"
              style={{ height: "170px" }}
            >
              <div className="modal-dialog">
                <div className="modal-content">
                  <div className="modal-body">
                    <a data-dismiss="modal" className="float-top-right">
                      <img
                        src="/assets/police/img/svg/icon_close_grey.svg"
                        alt=""
                      />
                    </a>
                    <a>
                      <div className="card card-sm">
                        <div className="row no-gutters d-flex align-items-center">
                          <div className="cardimgWrap">
                            <img
                              src="/assets/police/img/sample/boat5.jpg"
                              className="img-fluid"
                              alt=""
                            />
                          </div>
                          <div className="cardInfoWrap">
                            <div className="card-body pl-3">
                              <h6>{this.state.selected?.shipName}</h6>
                              <a
                                className="btn btn-primary btn-lg float-right"
                                data-dismiss="modal"
                                onClick={() =>
                                  PageStore.push(
                                    `/aboard?goodsId=${this.state.selected?.goodsId}`
                                  )
                                }
                              >
                                승선 명부
                              </a>
                              <p>
                                - 승선인원/탑승정원 :{" "}
                                <strong className="large text-primary">
                                  {Intl.NumberFormat().format(
                                    this.state.selected?.ridePersonnel || 0
                                  )}
                                </strong>
                                /
                                <strong className="large">
                                  {Intl.NumberFormat().format(
                                    this.state.selected?.maxPersonnel || 0
                                  )}
                                </strong>
                                <br />- 현재 상태 :{" "}
                                <strong className="large text-primary">
                                  {this.state.selected?.status}
                                </strong>
                              </p>
                            </div>
                          </div>
                        </div>
                      </div>
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
