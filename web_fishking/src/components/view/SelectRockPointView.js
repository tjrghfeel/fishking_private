/* global daum, kakao */
import React from "react";
import { inject, observer } from "mobx-react";

export default inject()(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.container = React.createRef(null);
        this.map = null;
        this.markers = [];
        this.selected = [];
        this.state = props.data || {
          name: "",
          latitude: null,
          longitude: null,
          points: [],
        };
      }
      async componentDidMount() {
        // used
        await this.setState({ used: this.props.used });
        // 지도 중심점 이동
        if (this.state.points.length > 0) {
          this.map = new daum.maps.Map(this.container.current, {
            level: 2,
            center: new daum.maps.LatLng(
              this.state.points[0]["latitude"],
              this.state.points[0]["longitude"]
            ),
          });
        } else {
          this.map = new daum.maps.Map(this.container.current, {
            level: 2,
            center: new daum.maps.LatLng(
              this.state.latitude,
              this.state.longitude
            ),
          });
        }
        // 마커 표시
        for (let point of this.state.points) {
          if (this.state.used.includes("".concat(point["id"]))) continue;
          const marker = new kakao.maps.Marker({
            position: new kakao.maps.LatLng(
              point["latitude"],
              point["longitude"]
            ),
          });
          marker.setMap(this.map);
          const infoWindow = new kakao.maps.InfoWindow({
            content: `선택됨`,
            removable: false,
          });
          const { onClick } = this.props;
          kakao.maps.event.addListener(marker, "click", () => {
            if (this.selected.includes(infoWindow)) {
              infoWindow.close();
              const index = this.selected.indexOf(infoWindow);
              const bef = this.selected.slice(0, index);
              const aft = this.selected.slice(index + 1, this.selected.length);
              this.selected = bef.concat(aft);
              if (onClick) onClick("remove", point["id"]);
            } else {
              infoWindow.open(this.map, marker);
              this.selected.push(infoWindow);
              if (onClick) onClick("add", point["id"]);
            }
          });
          this.markers.push(marker);
        }
      }

      render() {
        return (
          <React.Fragment>
            <div style={{ fontWeight: "bold" }}>
              {this.state.name}&nbsp;<sub>{this.state.address}</sub>
            </div>
            <br />
            <div
              ref={this.container}
              style={{ width: "100%", height: "380px" }}
            >
              map
            </div>
          </React.Fragment>
        );
      }
    }
  )
);
