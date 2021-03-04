/* global daum, kakao, $ */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout },
} = Components;

export default inject(
  "PageStore",
  "NativeStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {};
        this.container = React.createRef(null);
        this.map = null;
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const { PageStore } = this.props;
        const qp = PageStore.getQueryParams();
        this.setState({ ...qp });

        const options = {
          center: new daum.maps.LatLng(qp.lat, qp.lon),
          level: 7,
        };
        this.map = new daum.maps.Map(this.container.current, options);

        const geocoder = new kakao.maps.services.Geocoder();
        geocoder.coord2Address(qp.lon, qp.lat, (result) => {
          this.setState({ address: result[0]["address"]["address_name"] });
        });

        const markerPosition = new kakao.maps.LatLng(qp.lat, qp.lon);
        const marker = new kakao.maps.Marker({
          position: markerPosition,
        });
        marker.setMap(this.map);

        $("#infoModal").modal("show");
      }

      copyAddress = () => {
        const { NativeStore } = this.props;
        NativeStore.clipboardCopy(this.state.address);
      };
      findWay = () => {
        const { NativeStore } = this.props;
        NativeStore.openMap({
          lat: this.state.lat,
          lng: this.state.lon,
        });
      };
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <NavigationLayout title={this.state.name} showBackIcon={true} />

            <div className="container nopadding">
              <div className="mapwrap">
                <div ref={this.container} id="map"></div>
              </div>
              <div className="map-infowrap">
                <div className="container nopadding">
                  <div className="row no-gutters d-flex align-items-center">
                    <div className="col-8 text-left">
                      <span className="text-info">주소</span>
                      <br />
                      {this.state.address}
                    </div>
                    <div className="col-4 text-right">
                      <nav className="nav nav-pills nav-icon float-right">
                        <a className="nav-link" onClick={this.findWay}>
                          <figure>
                            <img
                              src="/assets/cust/img/svg/icon-map.svg"
                              alt=""
                            />
                          </figure>
                          길찾기
                        </a>
                        <a className="nav-link" onClick={this.copyAddress}>
                          <figure>
                            <img
                              src="/assets/cust/img/svg/icon-copy.svg"
                              alt=""
                            />
                          </figure>
                          주소복사
                        </a>
                      </nav>
                    </div>
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
