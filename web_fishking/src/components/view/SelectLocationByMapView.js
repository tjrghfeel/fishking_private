/* global daum, kakao */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";

export default inject("PageStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.container = React.createRef(null);
        this.map = null;
        this.markers = [];
        this.geocoder = new kakao.maps.services.Geocoder();
        this.state = { address: "", lat: 0, lng: 0 };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const options = {
          center: new daum.maps.LatLng(36.252932, 127.724734),
          level: 7,
        };
        this.map = new daum.maps.Map(this.container.current, options);
        kakao.maps.event.addListener(this.map, "click", ({ latLng }) => {
          const lat = latLng.getLat();
          const lng = latLng.getLng();

          if (this.markers.length > 0) {
            for (let marker of this.markers) {
              marker.setMap(null);
            }
            this.markers = [];
          }

          // const image = new kakao.maps.MarkerImage(
          //   "/assets/cust/img/svg/marker-boat-white.svg",
          //   new kakao.maps.Size(64, 69),
          //   { offset: new kakao.maps.Point(27, 69) }
          // );

          this.geocoder.coord2Address(lng, lat, (result, status) => {
            if (status === kakao.maps.services.Status.OK) {
              const address = result[0]["address"]["address_name"];
              this.setState({ address, lat, lng });
            }
          });

          const position = new kakao.maps.LatLng(lat, lng);
          const marker = new kakao.maps.Marker({ position });
          marker.setMap(this.map);
          this.markers.push(marker);
        });
        setTimeout(() => {
          this.map.relayout();
        }, 0);
      }
      onSelected = () => {
        const { onSelected } = this.props;
        if (onSelected && this.state.address.length > 0) {
          onSelected({
            address: this.state.address,
            lat: this.state.lat,
            lng: this.state.lng,
          });
        }
      };

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <div className="container nopadding">
              <div className="mapwrap-sm">
                <div className="info-btm-round pt-2">
                  <div className="row no-gutters d-flex align-items-center pt-0">
                    <div className="col-7 text-center">
                      {this.state.address}
                    </div>
                    <div className="col-4 text-right">
                      <a
                        className="btn btn-primary btn-sm-nav-tab"
                        onClick={this.onSelected}
                      >
                        내 위치 선택하기
                      </a>
                    </div>
                  </div>
                </div>
                <div ref={this.container} id="map">
                  {/*<a*/}
                  {/*  href="#none"*/}
                  {/*  className="pointer img-sm"*/}
                  {/*  style={{ top: "310px", left: "225px" }}*/}
                  {/*>*/}
                  {/*  <img src="/assets/cust/img/svg/marker-boat-white.svg" />*/}
                  {/*</a>*/}
                </div>
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
