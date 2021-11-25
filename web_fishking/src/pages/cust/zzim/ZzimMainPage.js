import React from "react";
import { inject, observer } from "mobx-react";
import Components from "../../../components";

const {
  LAYOUT: { NavigationLayout, MainTab },
} = Components;

export default inject(
  "PageStore",
  "APIStore"
)(
  observer(
    class extends React.Component {
      constructor(props) {
        super(props);
        this.state = {
          shipCount: 0,
          sealocksCount: 0,
        };
      }
      /********** ********** ********** ********** **********/
      /** function */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const { APIStore } = this.props;
        const resolve = await APIStore._get("/v2/api/take/count");
        if (resolve) {
          this.setState({
            shipCount: resolve[0] || 0,
            sealocksCount: resolve[1] || 0,
          });
        }
      }
      /********** ********** ********** ********** **********/
      /** render */
      /********** ********** ********** ********** **********/
      render() {
        const { PageStore } = this.props;
        return (
          <React.Fragment>
            <NavigationLayout title={"찜한 업체"} showBackIcon={true} />

            <div className="container nopadding">
              <div className="row-zzim">
                <div
                  className="rows"
                  style={{
                    background:
                      "url(/assets/cust/img/bg_zzim1.jpg) no-repeat top left",
                    backgroundSize: "cover",
                  }}
                >
                  <a onClick={() => PageStore.push(`/zzim/boat`)}>
                    <h4>
                      <span className="icon-heart-white">
                        {Intl.NumberFormat().format(this.state.shipCount)}
                      </span>
                      <br />
                      선상 낚시
                    </h4>
                    <p>생생한 라이브 바다 낚시</p>
                  </a>
                </div>
              </div>
              <div className="row-zzim">
                <div
                  className="rows"
                  style={{
                    background:
                      "url(/assets/cust/img/bg_zzim2.jpg) no-repeat top left",
                    backgroundSize: "cover",
                  }}
                >
                  <a onClick={() => PageStore.push(`/zzim/rock`)}>
                    <h4>
                      <span className="icon-heart-white">
                        {Intl.NumberFormat().format(this.state.sealocksCount)}
                      </span>
                      <br />
                      갯바위 낚시
                    </h4>
                    <p>전국 인기 포인트를 한눈에!</p>
                  </a>
                </div>
              </div>
            </div>
            <MainTab activeIndex={4} />
          </React.Fragment>
        );
      }
    }
  )
);
