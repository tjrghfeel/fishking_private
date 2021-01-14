/* global daum */
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
        setTimeout(() => {
          this.map.relayout();
        }, 0);
      }

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
                      전라남도 진도군 임회면 굴포리 144-7
                    </div>
                    <div className="col-4 text-right">
                      <a
                        className="btn btn-primary btn-sm-nav-tab"
                        href="#none"
                      >
                        내 위치 선택하기
                      </a>
                    </div>
                  </div>
                </div>
                <div ref={this.container} id="map">
                  <a
                    href="#none"
                    className="pointer img-sm"
                    style={{ top: "310px", left: "225px" }}
                  >
                    <img src="/assets/img/svg/marker-boat-white.svg" />
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
