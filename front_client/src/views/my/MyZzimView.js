import React from "react";
import { inject, observer } from "mobx-react";
import Navigation from "../../components/layouts/Navigation";
import Http from "../../Http";

export default inject()(
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
      /** functions */
      /********** ********** ********** ********** **********/
      async componentDidMount() {
        const resolve = await Http._get("/v2/api/take/count");
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
        const { history } = this.props;
        return (
          <>
            {/** Navigation */}
            <Navigation title={"찜한 업체"} showBack={true} />

            <div className="container nopadding">
              <div className="row-zzim">
                <div
                  className="rows"
                  style={{
                    background:
                      "url(/assets/img/bg_zzim1.jpg) no-repeat top left",
                    backgroundSize: "cover",
                  }}
                >
                  <a onClick={() => history.push(`/my/zzim/boat`)}>
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
                      "url(/assets/img/bg_zzim2.jpg) no-repeat top left",
                    backgroundSize: "cover",
                  }}
                >
                  <a onClick={() => history.push(`/my/zzim/rock`)}>
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
          </>
        );
      }
    }
  )
);
