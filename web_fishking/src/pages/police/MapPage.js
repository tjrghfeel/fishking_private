/* global daum, kakao */
import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../components";
const {
  LAYOUT: { NavigationLayout, PoliceMainTab },
} = Components;

export default inject("PageStore")(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.container = React.createRef(null);
        this.map = null;
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
      }

      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
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
          </React.Fragment>
        );
      }
    }
  )
);
