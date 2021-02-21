/* global daum, kakao, $ */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout },
  MODAL: { MapCompanyInfoModal },
} = Components;

export default inject(
  "PageStore",
  "NativeStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          fishingType: "ship",
          selected: {},
        };
        this.container = React.createRef(null);
        this.map = null;
        this.markers = [];
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      componentDidMount() {
        const { PageStore } = this.props;
        const qp = PageStore.getQueryParams();
        let fishingType = qp.fishingType || "boat";
        if (fishingType === "boat") fishingType = "ship";
        else fishingType = "seaRocks";
        this.setState({ fishingType });

        this.map = new daum.maps.Map(this.container.current, {
          center: new daum.maps.LatLng(33.26935330872013, 126.37479180180777),
          lever: 5,
        });

        kakao.maps.event.addListener(this.map, "dragend", () => {
          this.loadPageData();
        });

        // this.loadPageData();
        // if (true) return;

        setTimeout(() => {
          if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition((position) => {
              const { latitude, longitude } = position.coords;
              this.map.setCenter(new kakao.maps.LatLng(latitude, longitude));
              this.loadPageData();
            });
          } else {
            this.loadPageData();
          }
        }, 800);
      }

      loadPageData = async () => {
        if (this.markers.length > 0) {
          for (let marker of this.markers) {
            marker.setMap(null);
          }
          this.markers = [];
        }

        const center = this.map.getCenter();
        const Lat = center.getLat();
        const Lng = center.getLng();
        const geocoder = new kakao.maps.services.Geocoder();
        geocoder.coord2Address(Lng, Lat, async (result) => {
          if (
            !(
              result[0] &&
              result[0]["address"] &&
              result[0]["address"]["address_name"]
            )
          )
            return;

          let sido = result[0]["address"]["address_name"] || "";
          sido = sido.split(" ")[0];

          const { APIStore } = this.props;
          const resolve = await APIStore._get("/v2/api/ships/map", {
            fishingType: this.state.fishingType,
            orderBy: "distance",
            size: 100,
            // sido,
            facilities: [],
            fishingDate: "",
            genres: [],
            hasRealTimeVideo: "",
            services: [],
            sigungu: "",
            species: [],
          });

          for (let item of resolve) {
            const {
              location: { latitude = null, longitude = null },
            } = item;
            if (latitude !== null && longitude !== null) {
              const position = new kakao.maps.LatLng(latitude, longitude);
              const marker = new kakao.maps.Marker({ position });
              marker.setMap(this.map);
              this.markers.push(marker);
              kakao.maps.event.addListener(marker, "click", () => {
                this.setState({ selected: item });
                $("#infoModal").modal("show");
              });
            }
          }
        });
      };
      onClick = (item) => {
        const { PageStore } = this.props;
        const qp = PageStore.getQueryParams();
        let fishingType = qp.fishingType || "boat";
        PageStore.push(`/cust/company/${fishingType}/detail/${item.id}`);
      };

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        return (
          <React.Fragment>
            <MapCompanyInfoModal
              id={"infoModal"}
              data={this.state.selected}
              onClick={this.onClick}
            />

            <NavigationLayout title={"지도보기"} showBackIcon={true} />

            <div className="container nopadding">
              <div className="mapwrap">
                <div ref={this.container} id="map"></div>
              </div>
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
